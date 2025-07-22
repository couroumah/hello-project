package com.pfm.devops.ui.configuration.view.finder;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents the UI object of a Finder
 */
public class FinderBean {

    /***************************************************************************
     *                                                                         *
     * Instance Variables                                                      *
     *                                                                         *
     **************************************************************************/
    private final StringProperty businessObject = new SimpleStringProperty();
    private final StringProperty shortName = new SimpleStringProperty();
    private final ObjectProperty<Double> nbInstance = new SimpleObjectProperty();
    private final StringProperty sourcePattern = new SimpleStringProperty();
    private final StringProperty targetPattern = new SimpleStringProperty();
    private final StringProperty action = new SimpleStringProperty();
    private final StringProperty fullPath = new SimpleStringProperty();
    private final StringProperty item = new SimpleStringProperty();
    private final StringProperty status= new SimpleStringProperty();
    private final StringProperty comment = new SimpleStringProperty();

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public FinderBean (String businessObject, String shortName, Double nbInstance, String sourcePattern, String targetPattern,
                       String action, String fullPath, String item, String status, String comment){
        setBusinessObject(businessObject);
        setShortName(shortName);
        setNbInstance(nbInstance);
        setSourcePattern(sourcePattern);
        setTargetPattern(targetPattern);
        setAction(action);
        setFullPath(fullPath);
        setItem(item);
        setStatus(status);
        setComment(comment);

    }


    /***************************************************************************
     *                                                                         *
     * Public methods                                                          *
     *                                                                         *
     **************************************************************************/

    /* businessObject */
    public String getBusinessObject() {
        return businessObject.get();
    }

    public StringProperty businessObjectProperty() {
        return businessObject;
    }

    public void setBusinessObject(String businessObject) {
        this.businessObject.set(businessObject);
    }

    /* shortName */
    public String getShortName() {
        return shortName.get();
    }

    public StringProperty shortNameProperty() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName.set(shortName);
    }

    /* nbInstance */
    public Double getNbInstance() {
        return nbInstance.get();
    }

    public ObjectProperty<Double> nbInstanceProperty() {
        return nbInstance;
    }

    public void setNbInstance(Double nbInstance) {
        this.nbInstance.set(nbInstance);
    }

    /* sourcePattern */
    public String getSourcePattern() {
        return sourcePattern.get();
    }

    public StringProperty sourcePatternProperty() {
        return sourcePattern;
    }

    public void setSourcePattern(String sourcePattern) {
        this.sourcePattern.set(sourcePattern);
    }

    /* targetPattern */
    public String getTargetPattern() {
        return targetPattern.get();
    }

    public StringProperty targetPatternProperty() {
        return targetPattern;
    }

    public void setTargetPattern(String targetPattern) {
        this.targetPattern.set(targetPattern);
    }

    /* action */
    public String getAction() {
        return action.get();
    }

    public StringProperty actionProperty() {
        return action;
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    /* fullPath */
    public String getFullPath() {
        return fullPath.get();
    }

    public StringProperty fullPathProperty() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath.set(fullPath);
    }

    /* item */
    public String getItem() {
        return item.get();
    }

    public StringProperty itemProperty() {
        return item;
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    /* status */
    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }


    /* comment */
    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }
}
