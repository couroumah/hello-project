package com.westpac.murex.devops.cmftemplates.service.impl;

import com.westpac.murex.devops.cem.utils.JAXBUtils;
import com.westpac.murex.devops.cem.utils.xml.FileUtilities;
import com.westpac.murex.devops.cmftemplates.service.LookupTablesService;
import com.westpac.murex.devops.model.cmftemplates.ConfigurationItem;
import com.westpac.murex.devops.model.cmftemplates.ConfigurationTemplate;
import com.westpac.murex.devops.model.lookupTable.Criteria;
import com.westpac.murex.devops.model.lookupTable.DecisionTree;
import com.westpac.murex.devops.model.lookupTable.Result;
import com.westpac.murex.devops.model.lookupTable.UserDefinedField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.westpac.murex.devops.cmftemplates.cicd.util.CommonUtils.toUNZipFromFile;
import static com.westpac.murex.devops.cmftemplates.cicd.util.Constant.DEFAULT_STATUS_OK;

@Service
@Slf4j
public class LookupTablesServiceImpl implements LookupTablesService {
    /**
     * Unpackages CTT ZIP file to Continuous Integration format <br>
     *
     * @param pathZip                   the path to input CTT ZIP file
     * @param pathUnZippedFolder        the path to unzipped items folder
     * @param pathUnpackagedItemsFolder the path to unpackaged items folder
     * @param fileExtension             the output file extension, e.g., XSL
     * @param configurationTemplate
     * @return the status of unpackage process, OK if successful, NOT OK otherwise
     */
    @Override
    public String unpackage(Path pathZip, Path pathUnZippedFolder, Path pathUnpackagedItemsFolder, String fileExtension, ConfigurationTemplate configurationTemplate) {
        log.info("--start [unpackage] - Extensions Lookup Tables");

        List<String> unpackagedFiles = new ArrayList<>();
        try {
            // Unzip file
            toUNZipFromFile(pathZip, pathUnZippedFolder);
            List<File> files = (List<File>) FileUtils.listFiles(pathUnZippedFolder.toFile(), TrueFileFilter.INSTANCE,
                    TrueFileFilter.INSTANCE);
            for (File file : files) {
                boolean hasXmlExtension = FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("xml");
                String filename = hasXmlExtension ? FilenameUtils.removeExtension(file.getName()) : file.getName();
                String filenameWithDot = FileUtilities.getFilenameWithDot(filename);
                String formulaLabelFormattedWithFileSeparator = FileUtilities.convertFilenameWithDotToFilenameWithSeparator(filenameWithDot);

                Path pathTargetFormulaLabel = pathUnpackagedItemsFolder.resolve(formulaLabelFormattedWithFileSeparator + fileExtension);
                Path pathUnpackagedItemsSubFolder = pathTargetFormulaLabel.getParent();

                // Create parent directory
                pathUnpackagedItemsSubFolder.toFile().mkdirs();
                unpackagedFiles.add(filenameWithDot);

                String prettyPrintXML = JAXBUtils.prettyPrint(FileUtils.readFileToString(pathUnZippedFolder.resolve(formulaLabelFormattedWithFileSeparator + fileExtension).toFile()
                        , Charset.defaultCharset()), false);

                // Copy files
                FileUtils.writeStringToFile(pathTargetFormulaLabel.toFile(), prettyPrintXML, Charset.defaultCharset());
                log.debug("   Copy to [{}]", pathTargetFormulaLabel);

                log.info("   export Lookup Table file[{}]", filename);
            }//for
        } catch (IOException e) {
            log.error("Cannot unzip file[{}]", pathZip);
        } catch (SAXException e) {
            log.error("Cannot pretty print XML file. SAXException: {}", e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        log.info("   export Lookup Tables to [{}]", pathUnpackagedItemsFolder);
        log.info("--end [unpackage] - Extensions Lookup Tables");

        return DEFAULT_STATUS_OK;
    }

    /**
     * Repackages Continuous Integration format files as example below:<br>
     *
     * @param pathInput      the path to input folder/files
     * @param pathOutputFile the path to output repackaged CTT ZIP file <code>CM.173.zip</code>
     * @return the status of repackage process, OK if successful, NOT_OK otherwise
     */
    @Override
    public String repackage(Path pathInput, Path pathOutputFile) {
        return null;
    }

    /**
     * Returns CTT zip content as ConfigurationItem object
     *
     * @param pathZip    the path to input CTT ZIP file
     * @param pathOutput
     * @return the CTT zip content as ConfigurationItem object
     */
    @Override
    public ConfigurationItem toCmfTemplate(Path pathZip, Path pathOutput) {
        return null;
    }

    /**
     * Returns a Map of LookpTable name and list of UserDefinedFields
     *
     * @param decisionTrees
     * @return
     */
    @Override
    public Collection<Map<String, Collection<UserDefinedField>>> getUserDefinedFields(Collection<DecisionTree> decisionTrees) {
        List<Map<String, Collection<UserDefinedField>>> rc = new ArrayList<>();
        decisionTrees.forEach(decisionTree -> rc.add(getUserDefinedField(decisionTree)));
        return rc;
    }

    /**
     * Returns a Map of LookpTable name and list of UserDefinedFields
     *
     * @param decisionTree
     * @return
     */
    @Override
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

    private List<UserDefinedField> getCriteriaUserDefinedField(DecisionTree decisionTree){
        List<UserDefinedField> userDefinedFields = new ArrayList<>();
        decisionTree.getRules().getRuleCollection().stream()
                .forEach(rule -> {
                    rule.getCriterias()
                            .getCriteriaCollection().stream()
                            .map(Criteria::getUserDefinedField)
                            .forEach(userDefinedFields::add);
                });

        return userDefinedFields;
    }

    private List<UserDefinedField> getResultUserDefinedField(DecisionTree decisionTree){
        List<UserDefinedField> userDefinedFields = new ArrayList<>();
        decisionTree.getRules().getRuleCollection().stream()
                .forEach(rule -> {
                    rule.getResults()
                            .getResultCollection().stream()
                            .map(Result::getUserDefinedField)
                            .forEach(userDefinedFields::add);
                });

        return userDefinedFields;
    }

    /**
     * Returns a Map<LookupTableName, List of UserDefinedFieldFields> where the pattern is found
     *
     * @param decisionTree
     * @param pattern
     * @return
     */
    @Override
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
    @Override
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
}
