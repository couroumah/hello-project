package com.westpac.murex.devops.lookupTable.service;

import com.westpac.murex.devops.lookupTable.dto.LookupTableDto;
import com.westpac.murex.devops.util.Messages;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.westpac.murex.devops.util.DevopsUtil.STATUS_NOK;

@Service
@Data
public class LookupTableSpreadsheetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LookupTableSpreadsheetService.class);

    // ----------------------------------------------------------------------------------------------------------------
    // ~ Static fields/initializers
    // ----------------------------------------------------------------------------------------------------------------
    private int currRow = 0;
    private int areaReferenceStart = 2;
    private int nbColumns = 0;
    private CellStyle dataCellStyle;
    private Workbook workbook;
    private CTTable cttable;

    /**
     * Write CsvBean into a spreadsheet
     *
     * @param items list of MxML entries
     * @return a status OK | NOTOK
     */
    public String writeExcel(List<? extends LookupTableDto> items, Path pathExcelOutputFile, String environment) {

        // timestamp start
        Instant startApplication = Instant.now();
        LOGGER.info("--start [writeExcel] #{} item(s) at {}", items.size(), LocalDateTime.ofInstant(startApplication, ZoneId.systemDefault()));

        List<String> headers = Arrays.asList(
                Messages.getString("lut.excel.header.TITLE").split(","));
        nbColumns = headers.size();

        /* Define the data range including headers */
        AreaReference dataRange = new AreaReference(
                new CellReference(areaReferenceStart, 0),
                new CellReference(Integer.valueOf(Messages.getString("Util.EXCEL_MAX_ROWS")), nbColumns - 1),
                SpreadsheetVersion.EXCEL2007);

        workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(
                Messages.getString("lut.excel.sheet.TITLE", Messages.getString("lut.excel.sheet.TITLE_TYPE")));
        sheet.setDefaultColumnWidth(15);
        sheet.setDisplayGridlines(false);

        XSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(16);
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setFontName(Messages.getString("Util.EXCEL_FONT_NAME"));
        style.setFont(font);

        String currDate = DateFormatUtils.format(Calendar.getInstance(),
                DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
        XSSFCell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(Messages.getString("lut.excel.sheet.TITLE_TO_SHEET", Messages.getString("lut.excel.sheet.TITLE_TYPE"), currDate, environment));
        cell.setCellStyle(style);

        /* Create an object of type XSSFTable */
        XSSFTable xssfTable = sheet.createTable(null);

        /* get CTTable object*/
        cttable = xssfTable.getCTTable();

        /* Let us define the required Style for the table */
        CTTableStyleInfo ctTableStyleInfo = cttable.addNewTableStyleInfo();
        ctTableStyleInfo.setName(Messages.getString("Util.EXCEL_TABLE_STYLE"));

        /* Set Table Style Options */
        ctTableStyleInfo.setShowColumnStripes(false); //showColumnStripes=0
        ctTableStyleInfo.setShowRowStripes(true); //showRowStripes=1

        /* Set Range to the Table */
        cttable.setRef(dataRange.formatAsString());
        cttable.setDisplayName("Data");      /* this is the display name of the table */
        cttable.setName("Data");    /* This maps to "displayName" attribute in <table>, OOXML */
        cttable.setId(1L); //id attribute against table as long value

        CTTableColumns columns = cttable.addNewTableColumns();
        columns.setCount(nbColumns); //define number of columns

        /* Define Header Information for the Table */
        for (int i = 0; i < nbColumns; i++) {
            CTTableColumn column = columns.addNewTableColumn();
            column.setName("Column" + i);
            column.setId(i + 1);
        }

        row = sheet.createRow(areaReferenceStart);
        for (int j = 0; j < nbColumns; j++) {
            XSSFCell localXSSFCell = row.createCell(j);
            localXSSFCell.setCellValue(headers.get(j));
        }
        currRow = areaReferenceStart + 1;

        //Write rows
        write(items);

        //Close
        close(pathExcelOutputFile);

        if (Files.exists(pathExcelOutputFile)) {
            LOGGER.info("--Success [writeExcel] File #{} ", pathExcelOutputFile);
            LOGGER.info("--end [writeExcel] #{} item(s) at {}", items.size(), LocalDateTime.ofInstant(startApplication, ZoneId.systemDefault()));
            return STATUS_NOK;
        } else {
            LOGGER.info("--end [Unable to writeExcel] file [{}] not generated at {}", pathExcelOutputFile, LocalDateTime.ofInstant(startApplication, ZoneId.systemDefault()));
            return STATUS_NOK;
        }
    }

    private void close(Path pathExcelOutputFile) {
        /* Write output as File */
        try (FileOutputStream fileOut = new FileOutputStream(pathExcelOutputFile.toFile())) {
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file[%s]. FileNotFoundException:%s".formatted(pathExcelOutputFile, e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException("Cannot find file[%s]. IOException:%s".formatted(pathExcelOutputFile, e.getMessage()));
        }
        currRow = 0;
    }

    private void write(List<? extends LookupTableDto> items) {
        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);

        /* Add remaining Table Data */
        for (LookupTableDto data : items) {

            //we have to populate rows
            /* Create a Row */
            XSSFRow row = sheet.createRow(currRow);

            //businessObject
            XSSFCell localXSSFCell = row.createCell(0);
            localXSSFCell.setCellValue(data.businessObject());

            //lookupTableName
            localXSSFCell = row.createCell(1);
            localXSSFCell.setCellValue(data.lookupTableName());

            //nbOccurrence
            localXSSFCell = row.createCell(2);
            localXSSFCell.setCellValue(data.nbOccurrence());

            //sourcePattern
            localXSSFCell = row.createCell(3);
            localXSSFCell.setCellValue(data.sourcePattern());

            //targetPattern
            localXSSFCell = row.createCell(4);
            localXSSFCell.setCellValue(data.targetPattern());

            //action
            localXSSFCell = row.createCell(5);
            localXSSFCell.setCellValue(data.action());

            //fullPath
            localXSSFCell = row.createCell(6);
            localXSSFCell.setCellValue(data.fullPath());

            //item
            localXSSFCell = row.createCell(7);
            localXSSFCell.setCellValue(data.item());

            //status
            localXSSFCell = row.createCell(8);
            localXSSFCell.setCellValue(data.status());

            //comment
            localXSSFCell = row.createCell(9);
            localXSSFCell.setCellValue(data.comment());

            currRow++;
        }

        AreaReference dataRange = new AreaReference(
                new CellReference(areaReferenceStart, 0),
                new CellReference(currRow, nbColumns - 1),
                SpreadsheetVersion.EXCEL2007);
        cttable.setRef(dataRange.formatAsString());
    }
}
