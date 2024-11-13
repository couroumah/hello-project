package com.devops.toolbox.finder;

import com.devops.toolbox.util.DateUtils;
import com.devops.toolbox.util.StandardConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.devops.toolbox.util.Messages.getString;

public class FinderHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinderHelper.class);
    public static DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    private FinderHelper(){

    }

    public static FinderSettings convertFromJson(Path pathJson){
        Gson gson = new Gson();
        FinderSettings finderSettings;

        // Converts JSON file to Java object
        try (Reader reader = new FileReader(pathJson.toFile())) {
            // Convert JSON File to Java Object
            finderSettings = gson.fromJson(reader, FinderSettings.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return finderSettings;
    }

    public static void convertToJson(FinderSettings finderSettings, Path pathOutputJson){
        // enable pretty print
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Converts Java object to File
        try (Writer writer = new FileWriter(pathOutputJson.toFile())) {
            gson.toJson(finderSettings, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getExportFilename(String businessObject, String fileExtension){
        return businessObject + "_" +
                DateUtils.getCurrentDateTime().format(DateTimeFormatter.ofPattern(DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS)) + "." +
                fileExtension;
    }

    /**
     * Returns <code>a map<Filename,List of patterns></code>
     *
     * @param pathInputFile file to process
     * @param patterns      list of patterns to search
     * @param charset       Charset
     * @return <code>a map<Filename,List of patterns></code>
     */
    public static Map<String, List<Map<String, List<String>>>> findPatternsInFile(Path pathInputFile,
                                                                           @NonNull List<String> patterns,
                                                                           Charset charset) throws IOException {
        LOGGER.debug("--start [{}] patterns #{}...", Thread.currentThread().getStackTrace()[1].getMethodName(), patterns.size());

        Map<String, List<Map<String, List<String>>>> rc = new HashMap<>();
        LOGGER.debug("--start read file [{}]...", pathInputFile);
        List<String> lines = Files.readAllLines(pathInputFile, charset);
        LOGGER.debug("--end read file [{}], nb lines [{}]", pathInputFile, decimalFormat.format(lines.size()));
        List<Map<String, List<String>>> listOfMapOfLinesMatchingWithPattern = new ArrayList<>();

        // Looping across patterns
        for (String pattern : patterns) {
            LOGGER.debug("--processing pattern [{}]...", pattern);
            Map<String, List<String>> mapOfLinesMatchingWithPattern = findLinesMatchingWithPattern(lines, pattern);
            boolean hasData = false;
            int counter = 0;

            for (String key : mapOfLinesMatchingWithPattern.keySet()) {
                List<String> lines1 = mapOfLinesMatchingWithPattern.get(key);
                counter += lines1.size();
                hasData = !lines1.isEmpty();
            }
            if (hasData) {
                LOGGER.info("--found [#{}] matches for pattern [{}]", decimalFormat.format(counter), pattern);
                listOfMapOfLinesMatchingWithPattern.add(mapOfLinesMatchingWithPattern);
            }

            LOGGER.debug("--processed pattern [{}]", pattern);
        }

        if (!listOfMapOfLinesMatchingWithPattern.isEmpty()) {
            rc.put(pathInputFile.toString(), listOfMapOfLinesMatchingWithPattern);
            LOGGER.info("--file [{}] total pattern(s) found [{}]", pathInputFile, decimalFormat.format(listOfMapOfLinesMatchingWithPattern.size()));
        }

        LOGGER.debug("--end [{}] patterns #{}", Thread.currentThread().getStackTrace()[1].getMethodName(), patterns.size());
        return rc;
    }

    /**
     * Returns a map with the pattern as key and a list of matching lines
     *
     * @param lines
     * @param pattern
     * @return Map<Key: pattern, Value: list of matching lines>
     */
    public static Map<String, List<String>> findLinesMatchingWithPattern(List<String> lines,
                                                                  String pattern) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> matchingLines = new ArrayList<>();
        for (String line : lines) {
            if (!findLineMatchingWithPattern(line, pattern).isEmpty()) {
                matchingLines.add(findLineMatchingWithPattern(line, pattern));
                LOGGER.debug("Pattern[{}] found at line: {}", pattern, line);
            }
        }

        map.put(pattern, matchingLines);
        return map;
    }

    /**
     * Returns a collection of found patterns from a line and a single pattern
     *
     * @param line    current line
     * @param pattern pattern to search
     * @return a collection of found patterns from a line and a single pattern
     */
    public static String findLineMatchingWithPattern(String line,
                                                     @NonNull String pattern) {
        LOGGER.debug("--start [{}] pattern [{}]...", Thread.currentThread().getStackTrace()[1].getMethodName(), pattern);

        String lineMatchingWithPattern = StringUtils.EMPTY;
        Pattern patternDd = Pattern.compile(pattern);
        Matcher matcher = patternDd.matcher(line);

        while (matcher.find()) {
            lineMatchingWithPattern = line;
            LOGGER.debug("--pattern found [{}] in line: {}", pattern, line);
        }

        LOGGER.debug("--end [{}] pattern [{}]", Thread.currentThread().getStackTrace()[1].getMethodName(), pattern);
        return lineMatchingWithPattern;
    }

    public static String writeToExcel(List<FinderEntityDto> dtos,
                                      Path pathOutputFile) {
        LOGGER.debug("--start [{}] #{} item(s) at {}...",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                dtos.size(),
                DateUtils.getCurrentDateTime());

        if (!Files.exists(pathOutputFile.getParent())) {
            try {
                FileUtils.forceMkdir(pathOutputFile.getParent().toFile());
            } catch (IOException e) {
                throw new RuntimeException("Cannot create directory [%s]. IOException: %s".formatted(pathOutputFile.getParent(), e.getMessage()));
            }
        }

        List<String> mainSheetHeaders = Arrays.asList(getString("CmfTemplatesHelper.excel.sheet.main.HEADER_TITLE")
                .split(","));
        List<String> detailedSheetHeaders = Arrays.asList(getString("CmfTemplatesHelper.excel.sheet.detailed.HEADER_TITLE")
                .split(","));
        int nbColumnsMainSheet = mainSheetHeaders.size();
        int nbColumnsDetailedSheet = detailedSheetHeaders.size();
        int areaReferenceStartMainSheet = 0;
        int areaReferenceStartDetailedSheet = 0;
        int maxRows = getTotalItems(dtos) + areaReferenceStartMainSheet;
        int maxRowsDetailedSheet = getTotalDetailedItems(dtos) + areaReferenceStartMainSheet;

        AreaReference dataRangeMainSheet = new AreaReference(
                new CellReference(areaReferenceStartMainSheet, 0),
                new CellReference(maxRows, nbColumnsMainSheet - 1),
                SpreadsheetVersion.EXCEL2007
        );

        AreaReference dataRangeDetailedSheet = new AreaReference(
                new CellReference(areaReferenceStartDetailedSheet, 0),
                new CellReference(maxRowsDetailedSheet, nbColumnsDetailedSheet - 1),
                SpreadsheetVersion.EXCEL2007
        );

        Workbook workbook = new XSSFWorkbook();
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setFontName(getString("Util.EXCEL_FONT_NAME"));
        style.setFont(font);

        createSheetMainSheet(dtos,
                workbook,
                dataRangeMainSheet,
                nbColumnsMainSheet,
                areaReferenceStartMainSheet,
                mainSheetHeaders);

//        createSheetDetailedSheet(dtos,
//                workbook,
//                dataRangeDetailedSheet,
//                nbColumnsDetailedSheet,
//                areaReferenceStartDetailedSheet,
//                detailedSheetHeaders);

        //Close file
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathOutputFile.toFile())) {
            workbook.write(fileOutputStream);

            LOGGER.info("\tFile [{}] generated at time [{}]", pathOutputFile, DateUtils.getCurrentDateTime());

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file [%s]. FileNotFoundException: %s".formatted(pathOutputFile, e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException("Cannot write file [%s]. IOException: %s".formatted(pathOutputFile, e.getMessage()));
        }

        LOGGER.debug("--end [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        return StandardConstants.STATUS_OK;
    }

    private static int getTotalItems(List<FinderEntityDto> finderEntityDtos) {
        return finderEntityDtos.size();
    }

    private static int getTotalDetailedItems(List<FinderEntityDto> finderEntityDtos) {
        int rc = 0;
        for (FinderEntityDto dto : finderEntityDtos) {
            rc+=dto.nbMatchedLines();
        }
        return rc;
    }

    private static void createSheetMainSheet(List<FinderEntityDto> dtos,
                                             Workbook workbook,
                                             AreaReference dataRange,
                                             int nbColumns,
                                             int areaReferenceStart,
                                             List<String> headers) {
        int currRow;
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(getString("CmfTemplatesHelper.excel.sheet.main.NAME"));
        sheet.setDefaultColumnWidth(Integer.parseInt(getString("CmfTemplatesHelper.excel.sheet.COLUMN_WIDTH")));
        sheet.setDisplayGridlines(Boolean.parseBoolean(getString("CmfTemplatesHelper.excel.sheet.DISPLAY_GRID_LINES")));

        XSSFTable xssfTable = sheet.createTable(null);
        CTTable ctTable = xssfTable.getCTTable();

        CTTableStyleInfo ctTableStyleInfo = ctTable.addNewTableStyleInfo();
        ctTableStyleInfo.setName(getString("Util.EXCEL_TABLE_STYLE"));

        ctTable.setRef(dataRange.formatAsString());
        ctTable.setDisplayName("DATA");
        ctTable.setName("DATA");
        ctTable.setId(1L);

        CTTableColumns columns = ctTable.addNewTableColumns();
        columns.setCount(nbColumns);

        /*Define header information for the table*/
        for (int i = 0; i < nbColumns; i++) {
            CTTableColumn column = columns.addNewTableColumn();
            column.setName("Column" + i);
            column.setId(i + 1);
        }

        XSSFRow row = sheet.createRow(areaReferenceStart);
        // Create headers
        for (int j = 0; j < nbColumns; j++) {
            XSSFCell localXSSFCell = row.createCell(j);
            localXSSFCell.setCellValue(headers.get(j));
        }

        currRow = areaReferenceStart + 1;

        for (FinderEntityDto object : dtos) {
            // Create a Row
            row = sheet.createRow(currRow++);

            // Add data
            row.createCell(0).setCellValue(object.businessObject());
            row.createCell(1).setCellValue(object.shortName());
            row.createCell(2).setCellValue(object.nbInstances());
            row.createCell(3).setCellValue(object.sourcePattern());
            row.createCell(4).setCellValue(object.targetPattern());
            row.createCell(5).setCellValue(object.action());
            row.createCell(6).setCellValue(object.absolutePath());
            row.createCell(7).setCellValue(object.item());
            row.createCell(8).setCellValue(object.status());
            row.createCell(9).setCellValue(object.comments());
        }
    }

//    private static void createSheetDetailedSheet(List<FinderEntityDto> dtos,
//                                                 Workbook workbook,
//                                                 AreaReference dataRange,
//                                                 int nbColumns,
//                                                 int areaReferenceStart,
//                                                 List<String> headers) {
//        int currRow;
//        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(getString("CmfTemplatesHelper.excel.sheet.detailed.NAME"));
//        sheet.setColumnWidth(1, 25000);
//
//        sheet.setDefaultColumnWidth(Integer.parseInt(getString("CmfTemplatesHelper.excel.sheet.COLUMN_WIDTH")));
//        sheet.setDisplayGridlines(Boolean.parseBoolean(getString("CmfTemplatesHelper.excel.sheet.DISPLAY_GRID_LINES")));
//
//        XSSFTable xssfTable = sheet.createTable(null);
//        CTTable ctTable = xssfTable.getCTTable();
//
//        CTTableStyleInfo ctTableStyleInfo = ctTable.addNewTableStyleInfo();
//        ctTableStyleInfo.setName(getString("Util.EXCEL_TABLE_STYLE"));
//
//        ctTable.setRef(dataRange.formatAsString());
//        ctTable.setDisplayName("DataDetailed");
//        ctTable.setName("DataDetailed");
//        ctTable.setId(1L);
//
//        CTTableColumns columns = ctTable.addNewTableColumns();
//        columns.setCount(nbColumns);
//
//        /*Define header information for the table*/
//        for (int i = 0; i < nbColumns; i++) {
//            CTTableColumn column = columns.addNewTableColumn();
//            column.setName("Column" + i);
//            column.setId(i + 1);
//        }
//
//        CellStyle style = workbook.createCellStyle();
//        style.setWrapText(true);
//
//        XSSFRow row = sheet.createRow(areaReferenceStart);
//        // Create headers
//        for (int j = 0; j < nbColumns; j++) {
//            XSSFCell localXSSFCell = row.createCell(j);
//            localXSSFCell.setCellValue(headers.get(j));
//            localXSSFCell.setCellStyle(style);
//        }
//
//        currRow = areaReferenceStart + 1;
//
//        for (FinderEntityDto dto : dtos) {
//            for (Map.Entry<String, List<String>> mapMatchedLines : dto.getMapMatchedLines().entrySet()) {
//                for (String matchedLine : mapMatchedLines.getValue()) {
//
//                    // Create a Row
//                    row = sheet.createRow(currRow++);
//                    // Add data
//                    XSSFCell xssfCell = row.createCell(0);
//                    xssfCell.setCellValue(tradingBankingBookReport.getSourcePattern());
//                    style.setAlignment(HorizontalAlignment.CENTER);
//                    xssfCell.setCellStyle(style);
//
//                    xssfCell = row.createCell(1);
//                    xssfCell.setCellValue(matchedLine);
//                    style.setAlignment(HorizontalAlignment.LEFT);
//                    style.setVerticalAlignment(VerticalAlignment.CENTER);
//                    xssfCell.setCellStyle(style);
//
//                }
//            }
//        }
//    }

}
