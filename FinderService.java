package com.devops.toolbox.finder;

import com.devops.toolbox.config.Settings;
import com.devops.toolbox.util.CmfTemplatesHelper;
import com.devops.toolbox.util.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;

import static com.devops.toolbox.util.StandardConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinderService {

    private final Settings settings;
    private final Helper helper;

    /**
     * Returns <code>a map<Filename,List of patterns></code>
     * @param pathInputFile file to process
     * @param patterns list of patterns to search
     * @return <code>a map<Filename,List of patterns></code>
     */
    public Map<String, List<Map<String, List<String>>>> findPatternsInFile(Path pathInputFile,
                                                              List<String> patterns){
        try {
            return helper.findPatternsInFile(pathInputFile, patterns);
        } catch (IOException e) {
            throw new RuntimeException(
                    MessageFormat.format("Cannot find patterns in file [{0}]. IOException : {1}",
                            pathInputFile.getFileName(),
                            e.getMessage()));
        }
    }

    /**
     * Returns <code>a map<Filename,List of patterns></code>
     * @param pathInputDirectory directory of files to process
     * @param patterns list of patterns to search
     * @return <code>a map<Filename,List of patterns></code>
     */
    public List<Map<String, List<Map<String, List<String>>>>> findPatternsInFiles(Path pathInputDirectory,
                                                              List<String> patterns){
        log.debug("--start [{}] ...", Thread.currentThread().getStackTrace()[1].getMethodName());

        List<Map<String, List<Map<String, List<String>>>>> listFilesWithMatchingPatterns = new ArrayList<>();
        IOFileFilter filter;
        if (StringUtils.hasText(settings.getFileFilterWildCard()))
            filter = new WildcardFileFilter(settings.getFileFilterWildCard());
        else filter = TrueFileFilter.INSTANCE;

        List<File> listChildrenFiles = (List<File>) FileUtils.listFiles(pathInputDirectory.toFile(), filter, TrueFileFilter.INSTANCE);

        Map<String, Collection<String>> map = new HashMap<>();
        listChildrenFiles.forEach(file -> listFilesWithMatchingPatterns.add(findPatternsInFile(file.toPath(), patterns)));

        log.debug("--end [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        return listFilesWithMatchingPatterns;
    }

    /**
     * Returns a list of tradingBankingBookReport objects
     * @param businessObject business object, e.g., Datamart
     * @param pathInputDirectory path to input files
     * @param patterns list of patterns
     * @return a list of tradingBankingBookReport objects
     */
    public List<TradingBankingBookReport> getTradingBankingBookReport(String businessObject,
                                                                      Path pathInputDirectory,
                                                                      List<String> patterns){
        log.debug("--start [{}] ...", Thread.currentThread().getStackTrace()[1].getMethodName());

        List<TradingBankingBookReport> tradingBankingBookReports = new ArrayList<>();
        List<Map<String, List<Map<String, List<String>>>>> mapList = findPatternsInFiles(pathInputDirectory, patterns);
        Map<String, Collection<String>> mapFindPatternsInFiles = new HashMap<>();
        Map<String, String> mapPortfolios = settings.getMapPortfolios();

        for (Map<String, List<Map<String, List<String>>>> map: mapList) {
            for(String key: map.keySet()){
                log.debug("processing file [{}]", key);
                List<Map<String, List<String>>> mapSubList = map.get(key);

                for (Map<String, List<String>> mapMatchedLines: mapSubList){

                    for(String pattern: mapMatchedLines.keySet()){

                        TradingBankingBookReport tradingBankingBookReport = getTradingBankingBookReport(businessObject, key, pattern, mapPortfolios, mapMatchedLines);
                        tradingBankingBookReports.add(tradingBankingBookReport);

                    }
                }
            }

        }
        log.debug("--end [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        return tradingBankingBookReports;
    }

    /**
     * Returns a TradingBankingBookReport object
     *
     * @param businessObject
     * @param key
     * @param pattern
     * @param mapPortfolios
     * @param mapMatchedLines
     * @return a TradingBankingBookReport object
     */
    private TradingBankingBookReport getTradingBankingBookReport(String businessObject,
                                                                 String key,
                                                                 String pattern,
                                                                 Map<String, String> mapPortfolios,
                                                                 Map<String, List<String>> mapMatchedLines) {
        return TradingBankingBookReport.builder()
                .businessObject(businessObject)
                .shortName(getShortname(key).toUpperCase())
                .nbInstances(mapMatchedLines.get(pattern).size())
                .mapMatchedLines(mapMatchedLines)
                .foundPatterns(pattern)
                .sourcePortfolio(pattern)
                .targetPortfolio(mapPortfolios.get(pattern))
                .item(Strings.EMPTY)
                .status("In Progress")
                .action("Analyzing")
                .comments(Strings.EMPTY)
                .absolutePath(key)
                .build();
    }

    /**
     * Writes a report for a single business object having its configuration files within a specific directory
     *
     * @param businessObject     the business object, e.g., Datamart
     * @param pathInputDirectory the directory to locate oll business object configuration files
     * @param patterns           the patterns to search
     * @param pathOutputFile     the report output file
     */
    public void writeReportFoundPatterns(String businessObject,
                                         Path pathInputDirectory,
                                         List<String> patterns,
                                         Path pathOutputFile) {
        log.info("--start [{}] businessObject [{}]...", Thread.currentThread().getStackTrace()[1].getMethodName(), businessObject);

        List<TradingBankingBookReport> tradingBankingBookReports = getTradingBankingBookReport( businessObject,
                                                                                                pathInputDirectory,
                                                                                                patterns);
        if(tradingBankingBookReports.isEmpty()){
            return;
        }

        CmfTemplatesHelper.writeToExcel(tradingBankingBookReports, pathOutputFile);
        log.info("--end [{}] businessObject [{}]", Thread.currentThread().getStackTrace()[1].getMethodName(), businessObject);
    }



    /**
     * Writes a report for each business object having its configuration files within a specific directory
     * @param mapFindPatterns a map<BusinessObject name, Path to related configuration files>
     */
    public void writeReportFoundPatterns(Map<String, String> mapFindPatterns){
        List<String> sourcePortfolios = new ArrayList<>(settings.getMapPortfolios().keySet());

        mapFindPatterns.forEach((businessObject, path) -> {
            try {
                writeReportFoundPatterns(
                        businessObject,
                        Paths.get(path),
                        sourcePortfolios,
                        settings.getPathOutputDirectory()
                                .resolve(settings.getExportFilename(businessObject, XLSX_FILE_EXTENSION)));
            } catch (Exception e) {
                throw new RuntimeException(
                        MessageFormat.format("Cannot write report for businessObject [{0}]. Exception : {1}",
                                businessObject,
                                e.getMessage()));
            }
        });
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
