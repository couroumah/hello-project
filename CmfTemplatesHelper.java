package com.devops.toolbox.util;

import com.devops.toolbox.cmftemplates.ConfigurationTemplateDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CmfTemplatesHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmfTemplatesHelper.class);

    public static String writeConfigurationTemplateToExcel(List<ConfigurationTemplateDto> configurationTemplateDtos,
                                                           List<String> mainSheetHeaders,
                                                           Path pathOutputFile) {
        LOGGER.debug("--start [{}] #{} item(s) at {}...",
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

        //Create body
        createConfigurationTemplateMainSheet(configurationTemplateDtos,
                workbook,
                dataRangeMainSheet,
                nbColumnsMainSheet,
                areaReferenceStartMainSheet,
                mainSheetHeaders);

        //Close file
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathOutputFile.toFile())) {
            workbook.write(fileOutputStream);
            LOGGER.info("\tSuccess - File [{}] generated at time [{}]", pathOutputFile, DateUtils.getCurrentDateTime());

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file [%s]. FileNotFoundException: %s".formatted(pathOutputFile, e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException("Cannot write file [%s]. IOException: %s".formatted(pathOutputFile, e.getMessage()));
        }

        LOGGER.debug("--end [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        return StandardConstants.STATUS_OK;
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
//            row.createCell(5).setCellValue(dto.instanceKeyValue());
            row.createCell(5).setCellValue(dto.description());
        }
    }

    public static final Path getCmfTemplatePath(String inputZIPFilename, Path pathUnZippedFolder) {
        String pathAsString = "cmf_templates/" + FilenameUtils.removeExtension(inputZIPFilename) + "." + StandardConstants.XML_FILE_EXTENSION;
        return pathUnZippedFolder.resolve(pathAsString);

    }

}
