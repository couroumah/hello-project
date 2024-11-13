package com.devops.toolbox.finder;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.devops.toolbox.util.Messages.getString;
import static com.devops.toolbox.util.StandardConstants.*;

@Service
@RequiredArgsConstructor
public class FinderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinderService.class);

    private final FinderEntityRepository repository;
    private final FinderEntityMapper mapper;

    /**
     * Returns <code>a map<Filename,List of patterns></code>
     * @param pathInputFile file to process
     * @param patterns list of patterns to search
     * @return <code>a map<Filename,List of patterns></code>
     */
    public Map<String, List<Map<String, List<String>>>> findPatternsInFile(Path pathInputFile,
                                                              List<String> patterns){
        Map<String, List<Map<String, List<String>>>> rc = new HashMap<>();
        try {
            return FinderHelper.findPatternsInFile(pathInputFile, patterns, Charset.defaultCharset());
        } catch (IOException e) {
//            throw new RuntimeException(
//                    MessageFormat.format("[findPatternsInFile] - Cannot find patterns in file [{0}]. IOException : {1}",
//                            pathInputFile,
//                            e.getMessage()));
            LOGGER.error(MessageFormat.format("[findPatternsInFile] - Cannot find patterns in file [{0}]. IOException : {1}",
                    pathInputFile,
                    e.getMessage()));
            return rc;
        }
    }

    /**
     * Returns <code>a map<Filename,List of patterns></code>
     *
     * @param pathInputDirectory directory of files to process
     * @param patterns           list of patterns to search
     * @param fileFilterWildCard
     * @return <code>a map<Filename,List of patterns></code>
     */
    public List<Map<String, List<Map<String, List<String>>>>> findPatternsInFiles(Path pathInputDirectory,
                                                                                  List<String> patterns,
                                                                                  String fileFilterWildCard){
        LOGGER.debug("--start [{}] ...", Thread.currentThread().getStackTrace()[1].getMethodName());

        List<Map<String, List<Map<String, List<String>>>>> listFilesWithMatchingPatterns = new ArrayList<>();
        IOFileFilter filter;
        if (StringUtils.hasText(fileFilterWildCard))
            filter = new WildcardFileFilter(fileFilterWildCard);
        else filter = TrueFileFilter.INSTANCE;

        List<File> listChildrenFiles = (List<File>) FileUtils.listFiles(pathInputDirectory.toFile(), filter, TrueFileFilter.INSTANCE);

        listChildrenFiles.forEach(file -> listFilesWithMatchingPatterns.add(findPatternsInFile(file.toPath(), patterns)));

        LOGGER.debug("--end [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        return listFilesWithMatchingPatterns;
    }

    /**
     * Returns a list of tradingBankingBookReport objects
     * @param businessObject business object, e.g., Datamart
     * @param pathInputDirectory path to input files
     * @param patterns list of patterns
     * @return a list of tradingBankingBookReport objects
     */
    public List<FinderEntityDto> getFinderEntityDtos(String businessObject,
                                                     Path pathInputDirectory,
                                                     List<String> patterns,
                                                     Map<String, String> mapLookupValues,
                                                     String fileFilterWildCard){
        LOGGER.debug("--start [{}] ...", Thread.currentThread().getStackTrace()[1].getMethodName());

        List<TradingBankingBookReport> tradingBankingBookReports = new ArrayList<>();
        List<FinderEntityDto> finderEntityDtos = new ArrayList<>();

        List<Map<String, List<Map<String, List<String>>>>> mapList = findPatternsInFiles(pathInputDirectory, patterns, fileFilterWildCard);

        for (Map<String, List<Map<String, List<String>>>> map: mapList) {
            for(String key: map.keySet()){
                LOGGER.debug("processing file [{}]", key);
                List<Map<String, List<String>>> mapSubList = map.get(key);

                for (Map<String, List<String>> mapMatchedLines: mapSubList){

                    for(String pattern: mapMatchedLines.keySet()){

                        FinderEntity finderEntity = getFinderEntity(businessObject, key, pattern, mapLookupValues, mapMatchedLines);
                        repository.save(finderEntity);
                        LOGGER.info("Persisted FinderEntity [{}]", finderEntity);
                        finderEntityDtos.add(mapper.toDto(finderEntity));

//                        repository.findAll().stream().forEach(finderEntityFromDB -> {
//                            LOGGER.info("FinderEntity id[{}]", finderEntityFromDB.getID());
//                            List<FinderEntityBody> finderMatchedBodies = finderEntityFromDB.getFinderEntityBodies();
//                            finderMatchedBodies.stream()
//                                    .forEach(finderEntityBody -> {
//                                        String dataAsString = new String(finderEntityBody.getData(), StandardCharsets.UTF_8);
//                                        LOGGER.info("--finderMatchedLine id[{}] - text [{}]", finderEntityBody.getID(), dataAsString);
//                                    });
//                        });

                    }
                }
            }

        }
        LOGGER.debug("--end [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        return finderEntityDtos;
    }

    private FinderEntity getFinderEntity(String businessObject,
                                         String key,
                                         String pattern,
                                         Map<String, String> mapLookupValues,
                                         Map<String, List<String>> mapMatchedLines){

        List<String> listOfMatchedLines = mapMatchedLines.get(pattern);

        FinderEntity finderEntity = FinderEntity.builder()
                .businessObject(businessObject)
                .shortName(getShortname(key).toUpperCase())
                .nbInstances(mapMatchedLines.get(pattern).size())
                .foundPatterns(pattern)
                .sourcePattern(pattern)
                .targetPattern(mapLookupValues.get(pattern))
                .absolutePath(key)
                .action(getString("FinderService.finderEntity.action"))
                .item(getString("FinderService.finderEntity.item"))
                .status(getString("FinderService.finderEntity.status"))
                .comments(getString("FinderService.finderEntity.comments"))
                .build();

        listOfMatchedLines.stream()
                .forEach(matchedLine -> {
                    finderEntity.addFinderEntityBody(new FinderEntityBody(matchedLine.getBytes()));
                });

        return finderEntity;
    }


    /**
     * Writes a report for a single business object having its configuration files within a specific directory
     *
     * @param businessObject     the business object, e.g., Datamart
     * @param pathInputDirectory the directory to locate oll business object configuration files
     * @param patterns           the patterns to search
     * @param pathOutputFile     the report output file
     */
    private List<FinderEntityDto> doWriteReportFoundPatterns(String businessObject,
                                            Path pathInputDirectory,
                                            List<String> patterns,
                                            Path pathOutputFile,
                                            Map<String, String> mapLookupValues,
                                            String fileFilterWildCard) {
        LOGGER.info("--start [{}] businessObject [{}]...", Thread.currentThread().getStackTrace()[1].getMethodName(), businessObject);

        List<FinderEntityDto> finderEntityDtos = getFinderEntityDtos(
                                                                    businessObject,
                                                                    pathInputDirectory,
                                                                    patterns,
                                                                    mapLookupValues,
                                                                    fileFilterWildCard);
        if(finderEntityDtos.isEmpty()){
            LOGGER.info("--no match found");
            LOGGER.info("--end [{}] businessObject [{}]", Thread.currentThread().getStackTrace()[1].getMethodName(), businessObject);
            return new ArrayList<>();
        }

        FinderHelper.writeToExcel(finderEntityDtos, pathOutputFile);
        LOGGER.info("--end [{}] businessObject [{}]", Thread.currentThread().getStackTrace()[1].getMethodName(), businessObject);
        return finderEntityDtos;
    }

    /**
     * Writes a report for each business object having its configuration files within a specific directory
     * @param finderSettings finder settings
     */
    public List<FinderEntityDto> writeReport(FinderSettings finderSettings) {
        List<FinderEntityDto> finderEntityDtos = new ArrayList<>();
        Map<String,String> mapLookupValues = finderSettings.getMapLookupValues();
        List<String> lookupValues = mapLookupValues.keySet().stream().toList();
        Map<String,String> mapLookupDirectories = finderSettings.getMapLookupDirectories();
        Path pathOutputDirectory = Paths.get(finderSettings.getPathOutputDirectory());
        String fileFilterWildCard = finderSettings.getFileFilterWildCard();

        if (!pathOutputDirectory.toFile().exists()){
            pathOutputDirectory.toFile().mkdirs();
        }

        for (Map.Entry<String, String> entry : mapLookupDirectories.entrySet()) {
            String businessObject = entry.getKey();
            String path = entry.getValue();
            Path pathLookupDirectory = Paths.get(path);
            Path pathOutputFile = pathOutputDirectory.resolve(FinderHelper.getExportFilename(businessObject, XLSX_FILE_EXTENSION));

            try {
                finderEntityDtos.addAll(
                        doWriteReportFoundPatterns(businessObject, pathLookupDirectory, lookupValues, pathOutputFile, mapLookupValues, fileFilterWildCard)
                );

            } catch (Exception e) {
//                throw new RuntimeException(
//                        MessageFormat.format("Cannot write report for businessObject [{0}]. Exception : {1}",
//                                businessObject,
//                                e.getMessage()));
                LOGGER.error(MessageFormat.format("Cannot write report for businessObject [{0}]. Exception : {1}",
                        businessObject,
                        e.getMessage()));
            }
        }

        return finderEntityDtos;
    }

    /**
     * Returns short name i-e without the file extension
     * @param longName file long name, i-e with extension , e.g., file.txt
     * @return short name i-e without the file extension
     */
    private String getShortname(String longName) {
        int index = longName.lastIndexOf(File.separator);
        String rc = longName.substring(index + 1);

        if (    (FilenameUtils.getExtension(rc).equalsIgnoreCase(XSL_FILE_EXTENSION)) ||
                (FilenameUtils.getExtension(rc).equalsIgnoreCase(XML_FILE_EXTENSION)) ||
                (FilenameUtils.getExtension(rc).equalsIgnoreCase(MSL_FILE_EXTENSION)) ||
                (FilenameUtils.getExtension(rc).equalsIgnoreCase(HTML_FILE_EXTENSION))
        ){
            return FilenameUtils.removeExtension(rc);
        } else {
            return rc;
        }
    }
}
