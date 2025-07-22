package com.pfm.devops.ui.configuration.view.finder;

import com.pfm.devops.util.Messages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import org.controlsfx.control.MasterDetailPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Optional;


public class FinderTableView extends VBox {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinderTableView.class);

    /**
     * ~ Autowired Variables
     * ----------------------------------------------------------------------------------------------------------------
     */


    /**
     * ~ UI components
     * ----------------------------------------------------------------------------------------------------------------
     */
    private TableView<FinderBean> tableView;
    private TableColumn<FinderBean, String> businessObjectColumn;
    private TableColumn<FinderBean, String> shortNameColumn;
    private TableColumn<FinderBean, Double> nbInstanceColumn;
    private TableColumn<FinderBean, String> sourcePatternColumn;
    private TableColumn<FinderBean, String> targetPatternColumn;
    private TableColumn<FinderBean, String> actionColumn;
    private TableColumn<FinderBean, String> fullPathColumn;
    private TableColumn<FinderBean, String> itemColumn;
    private TableColumn<FinderBean, String> statusColumn;
    private TableColumn<FinderBean, String> commentColumn;
    // Buttons
    private Button updateButton;
    private Button deleteButton;
    // TextFields
    private TextField businessObjectTextField;
    private TextField shortNameTextField;
    private TextField nbInstanceTextField;
    private TextField sourcePatternTextField;
    private TextField targetPatternTextField;
    private TextField actionTextField;
    private TextField fullPathTextField;
    private TextField itemTextField;
    private TextField statusTextField;
    private TextField commentTextField;

    private MasterDetailPane masterDetailPane;
    // ObservableList
    private ObservableList<FinderDto> observableList = FXCollections.observableArrayList();


    /**
     * ~ Public methods
     * ----------------------------------------------------------------------------------------------------------------
     */

    public FinderTableView() {

        buildUI();
    }


    /**
     * ~ Private methods
     * ----------------------------------------------------------------------------------------------------------------
     */

    /**
     * Build UI
     */
    private void buildUI() {
        BorderPane borderPane = new BorderPane();

        masterDetailPane = new MasterDetailPane(Side.RIGHT);
        masterDetailPane.setDividerSizeHint(100);

        tableView = new TableView<>();
        tableView.setEditable(false);
        tableView.setFixedCellSize(25);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                FinderBean bean = tableView.getSelectionModel().getSelectedItem();
                // Refresh UI
                loadData(bean);
            }
        });

        CheckBox chkShowDetails = new CheckBox("Show data details");
        chkShowDetails.setSelected(false);

        /* businessObject */
        businessObjectColumn = new TableColumn<>("Business Object");
        businessObjectColumn.setCellValueFactory(new PropertyValueFactory<>("businessObject"));
        businessObjectColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        businessObjectColumn.setPrefWidth(80);

        /* shortName */
        shortNameColumn = new TableColumn<>("Short Name");
        shortNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        shortNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        shortNameColumn.setPrefWidth(80);

        /* nbInstance */
        nbInstanceColumn = new TableColumn<>("Nb Instance(s)");
        nbInstanceColumn.setCellValueFactory(new PropertyValueFactory<>("nbInstance"));
//        nbInstanceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nbInstanceColumn.setPrefWidth(80);

        /* sourcePattern */
        sourcePatternColumn = new TableColumn<>("Source Pattern");
        sourcePatternColumn.setCellValueFactory(new PropertyValueFactory<>("sourcePattern"));
        sourcePatternColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sourcePatternColumn.setPrefWidth(80);

        /* targetPattern */
        targetPatternColumn = new TableColumn<>("Target Pattern");
        targetPatternColumn.setCellValueFactory(new PropertyValueFactory<>("targetPattern"));
        targetPatternColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        targetPatternColumn.setPrefWidth(80);

        /* action */
        actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        actionColumn.setPrefWidth(80);

        /* fullPath */
        fullPathColumn = new TableColumn<>("Full Path");
        fullPathColumn.setCellValueFactory(new PropertyValueFactory<>("fullPath"));
        fullPathColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fullPathColumn.setPrefWidth(80);

        /* item */
        itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemColumn.setPrefWidth(80);

        /* status */
        statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusColumn.setPrefWidth(80);

        /* comment */
        commentColumn = new TableColumn<>("Comment");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        commentColumn.setPrefWidth(80);

        tableView.getColumns().add(businessObjectColumn);
        tableView.getColumns().add(shortNameColumn);
        tableView.getColumns().add(nbInstanceColumn);
        tableView.getColumns().add(sourcePatternColumn);
        tableView.getColumns().add(targetPatternColumn);
        tableView.getColumns().add(actionColumn);
        tableView.getColumns().add(fullPathColumn);
        tableView.getColumns().add(itemColumn);
        tableView.getColumns().add(statusColumn);
        tableView.getColumns().add(commentColumn);
        tableView.setPrefHeight(800.0);
        tableView.setPrefWidth(400.0);

        borderPane.setCenter(tableView);

        masterDetailPane.setMasterNode(borderPane);
        masterDetailPane.setDetailNode(createControlDataTrackingDetail());

        getChildren().addAll(masterDetailPane);
    }

    /**
     * Create a panel with details and data input purposes
     *
     * @return
     */
    private Node createControlDataTrackingDetail() {
        LOGGER.debug("-- starting [createControlDataTrackingDetail]");

        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        // Update button
        updateButton = new Button(Messages.getString("BudgetTrackingTableView.UPDATE_BUTTON"));
        updateButton.setOnAction(evt -> {
            try {
                updateData(tableView.getSelectionModel().getSelectedItem());
            } catch (ParseException e) {
                LOGGER.error("Cannot save data. ParseException: {}", e.getMessage());
            }
        });

        // Delete button
        deleteButton = new Button(Messages.getString("BudgetTrackingTableView.DELETE_BUTTON"));
        deleteButton.setOnAction(evt -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure to delete?");
                Optional<ButtonType> action = alert.showAndWait();

                if (action.get() == ButtonType.OK) {
                    deleteData(tableView.getSelectionModel().getSelectedItem());
                }

            } catch (ParseException e) {
                LOGGER.error("Cannot save data. ParseException: {}", e.getMessage());
            }
        });

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(deleteButton, updateButton);

        // businessObject
        Label businessObjectLabel = new Label("Business Object");
        GridPane.setHalignment(businessObjectLabel, HPos.LEFT);
        businessObjectTextField = new TextField();
        businessObjectTextField.setDisable(true);

        // shortName
        Label shortNameLabel = new Label("Short name");
        GridPane.setHalignment(businessObjectLabel, HPos.LEFT);
        shortNameTextField = new TextField();
        shortNameTextField.setDisable(true);

        // nbInstance
        Label nbInstanceLabel = new Label("Nb instance");
        GridPane.setHalignment(businessObjectLabel, HPos.LEFT);
        nbInstanceTextField = new TextField();
        nbInstanceTextField.setDisable(true);

        // sourcePattern
        Label sourcePatternLabel = new Label("Source pattern");
        GridPane.setHalignment(businessObjectLabel, HPos.LEFT);
        sourcePatternTextField = new TextField();
        sourcePatternTextField.setDisable(true);

        // targetPattern
        Label targetPatternLabel = new Label("Target pattern");
        GridPane.setHalignment(businessObjectLabel, HPos.LEFT);
        targetPatternTextField = new TextField();
        targetPatternTextField.setDisable(true);

        // action
        Label actionLabel = new Label("Action");
        GridPane.setHalignment(businessObjectLabel, HPos.LEFT);
        actionTextField = new TextField();
        actionTextField.setDisable(true);

        // fullPath
        Label fullPathLabel = new Label("Full path");
        GridPane.setHalignment(businessObjectLabel, HPos.LEFT);
        fullPathTextField = new TextField();
        fullPathTextField.setDisable(true);

        // item
        Label itemLabel = new Label("Item");
        GridPane.setHalignment(businessObjectLabel, HPos.LEFT);
        itemTextField = new TextField();
        itemTextField.setDisable(true);

        // Status
        Label statusLabel = new Label("Status");
        statusTextField = new TextField();
        statusTextField.setDisable(true);

        // comment
        Label commentLabel = new Label("Comment");
        commentTextField = new TextField();
        commentTextField.setDisable(true);

        // Add content
        gridPane.getStyleClass().add("content");

        gridPane.add(businessObjectLabel, 0, 1);
        gridPane.add(businessObjectTextField, 1, 1);

        gridPane.add(shortNameLabel, 0, 2);
        gridPane.add(shortNameTextField, 1, 2);

        gridPane.add(nbInstanceLabel, 0, 3);
        gridPane.add(nbInstanceTextField, 1, 3);

        gridPane.add(sourcePatternLabel, 0, 4);
        gridPane.add(sourcePatternTextField, 1, 4);

        gridPane.add(targetPatternLabel, 0, 5);
        gridPane.add(targetPatternTextField, 1, 5);

        gridPane.add(actionLabel, 0, 6);
        gridPane.add(actionTextField, 1, 6);

        gridPane.add(fullPathLabel, 0, 7);
        gridPane.add(fullPathTextField, 1, 7);

        gridPane.add(itemLabel, 0, 8);
        gridPane.add(itemTextField, 1, 8);

        gridPane.add(statusLabel, 0, 9);
        gridPane.add(statusTextField, 1, 9);

        gridPane.add(commentLabel, 0, 10);
        gridPane.add(commentTextField, 1, 10);


        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setHalignment(HPos.RIGHT);
        col2.setHalignment(HPos.LEFT);

        gridPane.getColumnConstraints().addAll(col1, col2);

        vBox.getChildren().add(gridPane);

        LOGGER.debug("-- ending [createControlTransactionInput]");

        return vBox;
    }

    /**
     * Persist data
     */
    private void updateData(FinderBean selectedDataTracking) throws ParseException {
        LOGGER.info("Updating selected data [{}]", selectedDataTracking);

        selectedDataTracking.setBusinessObject(businessObjectTextField.getText());
        selectedDataTracking.setShortName(shortNameTextField.getText());
        selectedDataTracking.setNbInstance(Double.valueOf(nbInstanceTextField.getText()));
        selectedDataTracking.setSourcePattern(sourcePatternTextField.getText());
        selectedDataTracking.setTargetPattern(targetPatternTextField.getText());
        selectedDataTracking.setAction(actionTextField.getText());
        selectedDataTracking.setFullPath(fullPathTextField.getText());
        selectedDataTracking.setItem(itemTextField.getText());
        selectedDataTracking.setStatus(statusTextField.getText());
        selectedDataTracking.setComment(commentTextField.getText());

//        controller.updateBudgetTracking(selectedBudgetTracking);

        LOGGER.info("Updated selected data [{}]", selectedDataTracking);
    }

    /**
     * Delete data
     */
    private void deleteData(FinderBean dataToDelete) throws ParseException {
        LOGGER.info("Deleting selected data [{}]", dataToDelete);
//        controller.deleteBudgetTracking(budgetTracking);
        LOGGER.debug("Deleted selected transactions [{}]", dataToDelete);
    }

    /**
     *
     */
    private void showDetailPane() {
        masterDetailPane.setShowDetailNode(false);
//        masterDetailPane.showDetailNodeProperty(true);
    }


    /**
     * Load data
     *
     * @param bean
     */
    private void loadData(FinderBean bean) {
        LOGGER.debug("--starting  [loadData]");

        // Load data on UI
        businessObjectColumn.setText(bean.getBusinessObject());
        shortNameColumn.setText(bean.getShortName());
        nbInstanceColumn.setText(String.valueOf(bean.getNbInstance()));
        sourcePatternColumn.setText(bean.getSourcePattern());
        targetPatternColumn.setText(bean.getTargetPattern());
        actionColumn.setText(bean.getAction());
        fullPathColumn.setText(bean.getFullPath());
        itemColumn.setText(bean.getItem());
        statusColumn.setText(bean.getStatus());
        commentColumn.setText(bean.getComment());

        LOGGER.debug("--end  [loadData]");
    }

    /**
     * Returns data

     * @return
     */
    private ObservableList<FinderBean> getFinder(){

        ObservableList<FinderBean> beans = FXCollections.observableArrayList();
        return beans;
    }


}
