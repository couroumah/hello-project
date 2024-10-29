package com.devops.toolbox.util;

import com.devops.toolbox.cmftemplates.ConfigurationTemplateDto;
import com.devops.toolbox.cmftemplates.Messages;
import com.devops.toolbox.finder.TradingBankingBookReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.devops.toolbox.cmftemplates.Messages.getString;

@Slf4j
public class CmfTemplatesHelper {

    public static String writeToExcel(List<TradingBankingBookReport> tradingBankingBookReports,
                                      Path pathOutputFile) {
        log.debug("--start [{}] #{} item(s) at {}...",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                tradingBankingBookReports.size(),
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
        int maxRows = getTotalItems(tradingBankingBookReports) + areaReferenceStartMainSheet;
        int maxRowsDetailedSheet = getTotalDetailedItems(tradingBankingBookReports) + areaReferenceStartMainSheet;

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
        font.setFontName(Messages.getString("Util.EXCEL_FONT_NAME"));
        style.setFont(font);

        createSheetMainSheet(tradingBankingBookReports,
                workbook,
                dataRangeMainSheet,
                nbColumnsMainSheet,
                areaReferenceStartMainSheet,
                mainSheetHeaders);

        createSheetDetailedSheet(tradingBankingBookReports,
                workbook,
                dataRangeDetailedSheet,
                nbColumnsDetailedSheet,
                areaReferenceStartDetailedSheet,
                detailedSheetHeaders);

        //Close file
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathOutputFile.toFile())) {
            workbook.write(fileOutputStream);

            log.info("\tFile [{}] generated at time [{}]", pathOutputFile, DateUtils.getCurrentDateTime());

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file [%s]. FileNotFoundException: %s".formatted(pathOutputFile, e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException("Cannot write file [%s]. IOException: %s".formatted(pathOutputFile, e.getMessage()));
        }

        log.debug("--end [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        return StandardConstants.STATUS_OK;
    }

    public static String writeConfigurationTemplateToExcel(List<ConfigurationTemplateDto> configurationTemplateDtos,
                                                           List<String> mainSheetHeaders,
                                                           Path pathOutputFile) {
        log.info("--start [{}] #{} item(s) at {}...",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                configurationTemplateDtos.size(),
                DateUtils.getCurrentDateTime());

        if (!Files.exists(pathOutputFile.getParent())) {
            try {
                FileUtils.forceMkdir(pathOutputFile.getParent().toFile());
            } catch (IOException e) {
                throw new RuntimeException("Cannot create directory [%s]. IOException: %s".formatted(pathOutputFile.getParent(), e.getMessage()));
            }
        }

        int nbColumnsMainSheet = mainSheetHeaders.size();
        int areaReferenceStartMainSheet = 0;
        int maxRows = configurationTemplateDtos.size() + areaReferenceStartMainSheet;

        AreaReference dataRangeMainSheet = new AreaReference(
                new CellReference(areaReferenceStartMainSheet, 0),
                new CellReference(maxRows, nbColumnsMainSheet - 1),
                SpreadsheetVersion.EXCEL2007
        );

        Workbook workbook = new XSSFWorkbook();
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setFontName(Messages.getString("Util.EXCEL_FONT_NAME"));
        style.setFont(font);

        createConfigurationTemplateMainSheet(configurationTemplateDtos,
                workbook,
                dataRangeMainSheet,
                nbColumnsMainSheet,
                areaReferenceStartMainSheet,
                mainSheetHeaders);

        //Close file
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathOutputFile.toFile())) {
            workbook.write(fileOutputStream);
            log.info("\tFile [{}] generated at time [{}]", pathOutputFile, DateUtils.getCurrentDateTime());

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file [%s]. FileNotFoundException: %s".formatted(pathOutputFile, e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException("Cannot write file [%s]. IOException: %s".formatted(pathOutputFile, e.getMessage()));
        }

        log.info("--end [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        return StandardConstants.STATUS_OK;
    }

    private static void createSheetMainSheet(List<TradingBankingBookReport> tradingBankingBookReports,
                                             Workbook workbook,
                                             AreaReference dataRange,
                                             int nbColumns,
                                             int areaReferenceStart,
                                             List<String> headers) {
        int currRow;
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(Messages.getString("CmfTemplatesHelper.excel.sheet.main.NAME"));
        sheet.setDefaultColumnWidth(Integer.parseInt(Messages.getString("CmfTemplatesHelper.excel.sheet.COLUMN_WIDTH")));
        sheet.setDisplayGridlines(Boolean.parseBoolean(Messages.getString("CmfTemplatesHelper.excel.sheet.DISPLAY_GRID_LINES")));

        XSSFTable xssfTable = sheet.createTable(null);
        CTTable ctTable = xssfTable.getCTTable();

        CTTableStyleInfo ctTableStyleInfo = ctTable.addNewTableStyleInfo();
        ctTableStyleInfo.setName(Messages.getString("Util.EXCEL_TABLE_STYLE"));

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

        for (TradingBankingBookReport object :
                tradingBankingBookReports) {
            // Create a Row
            row = sheet.createRow(currRow++);



            // Add data
            row.createCell(0).setCellValue(object.getBusinessObject());
            row.createCell(1).setCellValue(object.getShortName());
            row.createCell(2).setCellValue(object.getNbInstances());
            row.createCell(3).setCellValue(object.getSourcePortfolio());
            row.createCell(4).setCellValue(object.getTargetPortfolio());
            row.createCell(5).setCellValue(object.getAction());
            row.createCell(6).setCellValue(object.getAbsolutePath());
            row.createCell(7).setCellValue(object.getItem());
            row.createCell(8).setCellValue(object.getStatus());
            row.createCell(9).setCellValue(object.getComments());
        }
    }


    private static void createConfigurationTemplateMainSheet(List<ConfigurationTemplateDto> configurationTemplateDtos,
                                                             Workbook workbook,
                                                             AreaReference dataRange,
                                                             int nbColumns,
                                                             int areaReferenceStart,
                                                             List<String> headers) {
        int currRow;
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(Messages.getString("CmfTemplatesHelper.excel.sheet.configurationTemplate.NAME"));
        sheet.setDefaultColumnWidth(Integer.parseInt(Messages.getString("CmfTemplatesHelper.excel.sheet.COLUMN_WIDTH")));
        sheet.setDisplayGridlines(Boolean.parseBoolean(Messages.getString("CmfTemplatesHelper.excel.sheet.DISPLAY_GRID_LINES")));

        XSSFTable xssfTable = sheet.createTable(null);
        CTTable ctTable = xssfTable.getCTTable();

        CTTableStyleInfo ctTableStyleInfo = ctTable.addNewTableStyleInfo();
        ctTableStyleInfo.setName(Messages.getString("Util.EXCEL_TABLE_STYLE"));

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

        for (ConfigurationTemplateDto dto : configurationTemplateDtos) {
            // Create a Row
            row = sheet.createRow(currRow++);

            // Add data
            row.createCell(0).setCellValue(dto.id());
            row.createCell(1).setCellValue(dto.templateName());
            row.createCell(2).setCellValue(dto.itemName());
            row.createCell(3).setCellValue(dto.itemObjectId());
            row.createCell(4).setCellValue(dto.instanceLabel());
            row.createCell(5).setCellValue(dto.instanceKeyValue());
            row.createCell(6).setCellValue(dto.description());
        }
    }

    private static void createSheetDetailedSheet(List<TradingBankingBookReport> tradingBankingBookReports,
                                                 Workbook workbook,
                                                 AreaReference dataRange,
                                                 int nbColumns,
                                                 int areaReferenceStart,
                                                 List<String> headers) {
        int currRow;
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(Messages.getString("CmfTemplatesHelper.excel.sheet.detailed.NAME"));
        sheet.setColumnWidth(1, 25000);

        sheet.setDefaultColumnWidth(Integer.parseInt(Messages.getString("CmfTemplatesHelper.excel.sheet.COLUMN_WIDTH")));
        sheet.setDisplayGridlines(Boolean.parseBoolean(Messages.getString("CmfTemplatesHelper.excel.sheet.DISPLAY_GRID_LINES")));

        XSSFTable xssfTable = sheet.createTable(null);
        CTTable ctTable = xssfTable.getCTTable();

        CTTableStyleInfo ctTableStyleInfo = ctTable.addNewTableStyleInfo();
        ctTableStyleInfo.setName(Messages.getString("Util.EXCEL_TABLE_STYLE"));

        ctTable.setRef(dataRange.formatAsString());
        ctTable.setDisplayName("DataDetailed");
        ctTable.setName("DataDetailed");
        ctTable.setId(1L);

        CTTableColumns columns = ctTable.addNewTableColumns();
        columns.setCount(nbColumns);

        /*Define header information for the table*/
        for (int i = 0; i < nbColumns; i++) {
            CTTableColumn column = columns.addNewTableColumn();
            column.setName("Column" + i);
            column.setId(i + 1);
        }

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        XSSFRow row = sheet.createRow(areaReferenceStart);
        // Create headers
        for (int j = 0; j < nbColumns; j++) {
            XSSFCell localXSSFCell = row.createCell(j);
            localXSSFCell.setCellValue(headers.get(j));
            localXSSFCell.setCellStyle(style);
        }

        currRow = areaReferenceStart + 1;

        for (TradingBankingBookReport tradingBankingBookReport : tradingBankingBookReports) {
            for (Map.Entry<String, List<String>> mapMatchedLines : tradingBankingBookReport.getMapMatchedLines().entrySet()) {
                for (String matchedLine : mapMatchedLines.getValue()) {

                    // Create a Row
                    row = sheet.createRow(currRow++);
                    // Add data
                    XSSFCell xssfCell = row.createCell(0);
                    xssfCell.setCellValue(tradingBankingBookReport.getSourcePortfolio());
                    style.setAlignment(HorizontalAlignment.CENTER);
                    xssfCell.setCellStyle(style);

                    xssfCell = row.createCell(1);
                    xssfCell.setCellValue(matchedLine);
                    style.setAlignment(HorizontalAlignment.LEFT);
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                    xssfCell.setCellStyle(style);

                }
            }
        }
    }

    private static int getTotalItems(List<TradingBankingBookReport> tradingBankingBookReports) {
        return tradingBankingBookReports.size();
    }

    private static int getTotalDetailedItems(List<TradingBankingBookReport> tradingBankingBookReports) {
        int rc = 0;
        for (TradingBankingBookReport tradingBankingBookReport : tradingBankingBookReports) {
            for (Map.Entry<String, List<String>> mapMatchedLines : tradingBankingBookReport.getMapMatchedLines().entrySet()) {
                for (String matchedLine : mapMatchedLines.getValue()) {
                    rc++;
                }
            }
        }
        return rc;
    }

    public static final Map<String, String> getObjectClassDescriptionMap() {
        Map<String, String> rc = new HashMap<>();

        rc.put("CM.563", "Configurator.Data extraction.Datamart.Datamart maintenance.Purge rules");
        rc.put("CM.783", "End-user.Middle office.Datamart reporting.Execute single datamart extraction.View item formulas");
        rc.put("CM.420", "Configurator.Data extraction.Datamart.Datamart management.Dynamic table.Murex");
        rc.put("CM.354", "Configurator.Data extraction.Datamart.Datamart management.Dynamic table.Client");
        rc.put("CM.353", "Configurator.Data extraction.Datamart.Datamart management.Sql table.Client");
        rc.put("CM.352", "End-user.Middle office.Datamart reporting.Execute single datamart extraction.Formatting view");
        rc.put("CM.472", "End-user.Middle office.Datamart reporting.Execute single datamart extraction.Formatting layout");
        rc.put("CM.1258", "Configurator.Data extraction.Datamart.Batches of datamart extractions.Parameters.Email notification.Email template");
        rc.put("CM.406", "Configurator.Data extraction.Datamart.Batches of stored procedures");
        rc.put("CM.407", "Configurator.Data extraction.Datamart.Rights templates");
        rc.put("CM.418", "Configurator.Data extraction.Datamart.Batches of table feeders");
        rc.put("CM.329", "Configurator.Data extraction.Datamart.Views.Dynamic table filters");
        rc.put("CM.417", "Configurator.Data extraction.Datamart.Batches of datamart extractions");
        rc.put("CM.405", "Configurator.Data extraction.Datamart.Stored procedures");
        rc.put("CM.878", "Supervisor.Reports and Screens.Datamart Reporting Template");
        rc.put("CM.414", "Configurator.Data extraction.Datamart.Table feeders");
        rc.put("CM.403", "Configurator.Data extraction.Datamart.Views.User dynamic tables");
        rc.put("CM.400", "Configurator.Data extraction.Datamart.Global filter");
        rc.put("CM.147", "End-user.Extensions.Datamodel query.Queries");
        rc.put("CM.408", "Configurator.Data extraction.Datamart.Datamart extractions");
        rc.put("CM.419", "Configurator.Data extraction.Datamart.Datamart management.Sql table.Murex");
        rc.put("CM.97", "End-user.Pricing.e-Tradepad.Public strategy");


        return rc;

    }

    public static final String getObjectClassDescription(String objectClass) {
        if ((getObjectClassDescriptionMap().get(objectClass) == null) | Strings.isEmpty(getObjectClassDescriptionMap().get(objectClass))) {
            return Strings.EMPTY;
        }
        return getObjectClassDescriptionMap().get(objectClass);
    }


    public static final Path getCmfTemplatePath(String inputZIPFilename, Path pathUnZippedFolder) {

        String pathAsString = "cmf_templates/" + FilenameUtils.removeExtension(inputZIPFilename) + "." + StandardConstants.XML_FILE_EXTENSION;
        return pathUnZippedFolder.resolve(pathAsString);

    }

    /**
     * @param text
     * @param delimiters
     * @param capitalizeFirstLetter
     * @return
     */
    public static final String convertToCamelCase(String text,
                                                  char delimiters,
                                                  boolean capitalizeFirstLetter) {
        return CaseUtils.toCamelCase(text, capitalizeFirstLetter, delimiters);
    }

    public static final String getCmfTemplateRepositoryPath(String text) {
        String text2 = text
                .replaceAll("\\.", File.separator + File.separator)
                .replaceAll(",", "");
        return convertToCamelCase(text2, ' ', true);
    }
}
