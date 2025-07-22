package com.pfm.devops.ui.configuration.view.finder;

import com.pfm.devops.util.DashboardUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.prefs.Preferences;

import static com.pfm.devops.util.ValidationUtil.validationInformation;


public class FinderView extends VBox {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinderView.class);

    /**
     * ~ Autowired Variables
     * ----------------------------------------------------------------------------------------------------------------
     */
    
    /**
     * ~ UI components
     * ----------------------------------------------------------------------------------------------------------------
     */
    private FinderTableView tableView;
    private Tab tabDataHeader;
    private TabPane tabPane;
    // Preferences
    private Preferences pref = Preferences.userRoot();
    private String defaultInputPath = pref.get("DEFAULT_INPUT_PATH", "");

    /**
     * ~ Public methods
     * ----------------------------------------------------------------------------------------------------------------
     */


    public FinderView(){
        buildUI();
    }

    /**
     * ~ Private methods
     * ----------------------------------------------------------------------------------------------------------------
     */

    /**
     * Build UI with components
     *
     */
    private void buildUI() {
        BorderPane borderPane = new BorderPane();
        BorderPane embeddedBorderPane = new BorderPane();
        ToolBar toolBar = new ToolBar();
        tabPane = new TabPane();
        tabDataHeader = new Tab("Data header");

        tableView = new FinderTableView();
        tableView.setPrefHeight(800.0);
        tableView.setPrefWidth(1133.0);

        // Toolbar
        toolBar.getItems().add(createExport2ExcelButton());
        toolBar.getItems().add(createImportButton());

        // Main pane
        embeddedBorderPane.setCenter(tableView);
        tabDataHeader.setContent(embeddedBorderPane);
        tabPane.getTabs().add(tabDataHeader);

        // Add components to the borderPane
        borderPane.setTop(toolBar);
        borderPane.setCenter(tabPane);

        getChildren().add(borderPane);
    }

    /**
     * Create the export Excel button widget
     * @return
     */
    private Button createExport2ExcelButton() {
        Button buttonExport2Excel = new Button("Export to Excel",
                DashboardUtils.buildIconImage("uicontrols/csv_export_icon.png"));
        buttonExport2Excel.setOnAction(evt -> {
            //Creating a File chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");
            LOGGER.debug("Preference initial default input path is[{}]", defaultInputPath);

            File initialDirectory = new File(defaultInputPath).getParentFile();
            if (defaultInputPath != null && !defaultInputPath.isEmpty() && initialDirectory.exists()) {
                fileChooser.setInitialDirectory(initialDirectory);
            }
            //Set extension filter for text files
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"));

            //Show save file dialog
            File exportExcelFile = fileChooser.showSaveDialog(null);

            if (exportExcelFile != null) {
//                HelpersService.excelExport(tableView.getTable(), exportExcelFile.toPath());
                LOGGER.info("HelpersService.excelExport...");
                validationInformation("Table exported to [" + exportExcelFile.getAbsolutePath() + "]");
                //Save the selected defaultInputPath
                pref.put("DEFAULT_INPUT_PATH", exportExcelFile.getAbsolutePath());
            }

        });
        return buttonExport2Excel;
    }

    /**
     * Create the import button widget
     * @return
     */
    private Button createImportButton() {
        Button buttonImportConfiguration = new Button("Import from JSON", DashboardUtils.buildIconImage("uicontrols/csv_import_icon.png"));
        buttonImportConfiguration.setOnAction(evt -> {
            //Creating a File chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import");
            LOGGER.debug("Preference initial default input path is[{}]", defaultInputPath);

            File initialDirectory = new File(defaultInputPath).getParentFile();
            if (defaultInputPath != null && !defaultInputPath.isEmpty() && initialDirectory.exists()) {
                fileChooser.setInitialDirectory(initialDirectory);
            }
            //Set extension filter for text files
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));

            //Show save file dialog
            File importFile = fileChooser.showOpenDialog(null);
            long nbFileLines = 0;
            try {
                nbFileLines = Files.lines(importFile.toPath()).count();
            } catch (IOException e) {
                LOGGER.error("Cannot count file lines. IOException: {}", e.getMessage());
            }

            if (importFile != null) {
                LOGGER.info("Loading {}...", importFile.getName());
                validationInformation("Transactions[" + nbFileLines + "] imported");
                //Save the selected defaultInputPath
                pref.put("DEFAULT_INPUT_PATH", importFile.getAbsolutePath());
            }

        });

        return buttonImportConfiguration;
    }

}
