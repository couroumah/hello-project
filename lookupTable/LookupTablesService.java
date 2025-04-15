package com.westpac.murex.devops.lookupTable.service;

import com.westpac.murex.devops.lookupTable.dto.LookupTableDto;
import com.westpac.murex.devops.lookupTable.model.*;
import com.westpac.murex.devops.util.JAXBUtils;
import com.westpac.murex.devops.util.Messages;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LookupTablesService {

    private final LookupTableMapper mapper;
    private final LookupTableSpreadsheetService spreadsheetService;

    @Value("${spring.datasource.name}")
    private String environment;

    public LookupTablesService(LookupTableMapper mapper,
                               LookupTableSpreadsheetService spreadsheetService) {
        this.mapper = mapper;
        this.spreadsheetService = spreadsheetService;
    }

    public void generateReport(Path pathFile, List<String> patterns) throws IOException, JAXBException {
        String datePattern = "yyyyMMdd_HHmmss";
        String localDateTimeAsString = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(datePattern));

        LookupTable lookupTable = JAXBUtils.convertXMLToObject(LookupTable.class, Files.readString(pathFile));
        Collection<DecisionTree> decisionTrees = lookupTable.getTemplateConfiguration()
                .getLookupConfiguration()
                .getDecisionTrees()
                .getDecisionTreeCollection();

        List<Map<String, List<UserDefinedField>>> rc = searchPatterns(decisionTrees, patterns);

        Path pathExcelOutputFile = Paths.get(
                Messages.getString(
                        "lut.excel.outputfile",
                        environment.toUpperCase(),
                        localDateTimeAsString)
        );

        List<LookupTableDto> dtos = mapper.toDto(rc, pathFile.toString());

        spreadsheetService.writeExcel(dtos, pathExcelOutputFile, environment);


//        rc.stream().forEach(stringListMap -> stringListMap.entrySet().stream()
//                .forEach(stringListEntry -> LOGGER.info("LookupTable[{}] - Pattern[{}] - Occurrences[{}]", stringListEntry.getKey()
//                        ,stringListEntry.getValue().stream().findFirst().get().getFieldValue(),stringListEntry.getValue().size())));
    }

    /**
     * Returns a Map of LookpTable name and list of UserDefinedFields
     *
     * @param decisionTree
     * @return
     */
    public Map<String, Collection<UserDefinedField>> getUserDefinedField(DecisionTree decisionTree) {

        Map<String, Collection<UserDefinedField>> mapUserDefinedField = new HashMap<>();
        List<UserDefinedField> userDefinedFields = new ArrayList<>();

        userDefinedFields.addAll(getCriteriaUserDefinedField(decisionTree));
        userDefinedFields.addAll(getResultUserDefinedField(decisionTree));

        String lookupTableFullname = decisionTree.getBusinessObjectId().getMefClassInstanceType()
                + "." + decisionTree.getBusinessObjectId().getDisplayLabel();

        mapUserDefinedField.put(lookupTableFullname, userDefinedFields);
        return mapUserDefinedField;
    }

    /**
     * Returns a Map<LookupTableName, List of UserDefinedFieldFields> where the pattern is found
     *
     * @param decisionTree
     * @param pattern
     * @return
     */
    public Map<String, List<UserDefinedField>> searchPattern(DecisionTree decisionTree, String pattern) {

        Map<String, List<UserDefinedField>> rc = new HashMap<>();
        Map<String, Collection<UserDefinedField>> mapLookupTable = getUserDefinedField(decisionTree);

        for (String key : mapLookupTable.keySet()) {

            Collection<UserDefinedField> userDefinedFields = mapLookupTable.get(key);
            List<UserDefinedField> userDefinedFieldFields = userDefinedFields
                    .stream()
                    .filter(userDefinedField -> userDefinedField.getFieldValue().equalsIgnoreCase(pattern))
                    .collect(Collectors.toList());

            if(userDefinedFieldFields.size() > 0){
                rc.put(key, userDefinedFieldFields);
            }


        }

        return rc;
    }

    /**
     * Returns a Map<LookupTableName, List of UserDefinedFieldFields> where the patterns are found
     *
     * @param decisionTrees
     * @param patterns
     * @return
     */
    public List<Map<String, List<UserDefinedField>>> searchPatterns(Collection<DecisionTree> decisionTrees, Collection<String> patterns) {

        List<Map<String, List<UserDefinedField>>> rc = new ArrayList<>();
        for (DecisionTree decisionTree: decisionTrees) {

            for (String pattern: patterns) {
                Map<String, List<UserDefinedField>> listMap = searchPattern(decisionTree,pattern);

                if(listMap.entrySet().size() > 0){
                    rc.add(listMap);
                }
            }
        }
        return rc;
    }

    private List<UserDefinedField> getCriteriaUserDefinedField(DecisionTree decisionTree){
        List<UserDefinedField> userDefinedFields = new ArrayList<>();

        Collection<Rule> ruleCollection = decisionTree.getRules().getRuleCollection();

        if(ruleCollection != null){

            ruleCollection.stream()
                    .forEach(rule -> {
                        rule.getCriterias()
                                .getCriteriaCollection().stream()
                                .map(Criteria::getUserDefinedField)
                                .forEach(userDefinedFields::add);
                    });
        }

        return userDefinedFields;
    }

    private List<UserDefinedField> getResultUserDefinedField(DecisionTree decisionTree){
        List<UserDefinedField> userDefinedFields = new ArrayList<>();
        Collection<Rule> ruleCollection = decisionTree.getRules().getRuleCollection();

        if(ruleCollection != null){
            ruleCollection.stream()
                    .forEach(rule -> {
                        rule.getResults()
                                .getResultCollection().stream()
                                .map(Result::getUserDefinedField)
                                .forEach(userDefinedFields::add);
                    });
        }

        return userDefinedFields;
    }
}
