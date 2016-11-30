package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import helpers.Helper;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import models.MainModel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@FXMLController(value = "/resources/fxml/MainView.fxml", title = "mcpai")
public final class MainController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    private DefaultFlowContainer defaultFlowContainer;
    private Flow flow;

    private MainModel mainModel;

    private TripInfoController tripInfoController;

    @FXML JFXTabPane mainTabPane;
    @FXML Tab tripInfoTab;
    @FXML Tab drivingInfoTab;
    @FXML Tab flyingInfoTab;
    @FXML Tab resultsTab;

    @FXML JFXButton testButton;
    @FXML Label driveTimeLabel;
    @FXML Label driveDistanceLabel;
    @FXML Label drivePriceLabel;
    @FXML Label driveDurationLabel;
    
    @FXML HBox infosBox;

    public DoubleProperty totalDriveTimeDoubleProperty;
    public DoubleProperty totalDriveDistanceDoubleProperty;

    private String testString;

    /**
     * Constructor then initialize then init
     */
    public MainController() {

    }

    public MainController(MainModel mainModel) {
        this();
        this.setMainModel(mainModel);
    }

    public MainModel getMainModel() { return mainModel; }
    public MainController setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
        return this;
    }

    public ViewFlowContext getContext() { return context; }
    public MainController setContext(ViewFlowContext context) {
        this.context = context;
        return this;
    }

    public Flow getFlow() { return flow; }
    public MainController setFlow(Flow flow) {
        this.flow = flow;
        return this;
    }

    public DefaultFlowContainer getDefaultFlowContainer() { return defaultFlowContainer; }
    public void setDefaultFlowContainer(DefaultFlowContainer defaultFlowContainer) {
        this.defaultFlowContainer = defaultFlowContainer;
    }

    public TripInfoController getTripInfoController() { return tripInfoController; }
    public MainController setTripInfoController(TripInfoController tripInfoController) {
        this.tripInfoController = tripInfoController;
        return this;
    }

    public String getTestString() { return this.testString; }
    public MainController setTestString(String testString) {
        this.testString = testString;
        return this;
    }

    private void printTest() {
        System.out.print(this + "...");
        Helper.printObject(mainModel, "mainModel");
    }

    private void testButtonAction() {
        printTest();
    }

    @FXML
    /**
     * Constructor then initialize then init
     */
    private void initialize() {

    }

    @PostConstruct
    /**
     * Constructor then initialize then init
     */
    public void init() throws Exception {
        setMainModel((MainModel) context.getRegisteredObject("mainModel"));
        setTestString(mainModel.getTestString());
        setDefaultFlowContainer(
              (DefaultFlowContainer) context.getRegisteredObject("defaultFlowContainer"));

        testButton.setOnAction(e -> {testButtonAction();});

        setupTabs();
        setUpObservables();
    }
    
    private void setUpObservables() {
//        setupObservable(mainModel.totalDriveTimeProperty,
//              driveTimeLabel, "(%.2f hrs)");
//        setupObservable(mainModel.totalDriveDistanceProperty,
//              driveDistanceLabel, "(%.0f miles)");
        setupObservable(mainModel.drivePriceProperty,
              drivePriceLabel,
              "($%.2f)");
        setupObservable(mainModel.driveDurationProperty,
              driveDurationLabel,
              "(%.1f hrs)");
    }
    
    private void setupObservable(Property<Number> prop, Label lbl) {
        setupObservable(prop, lbl, "%.2f");
    }
    
    private void setupObservable(Property<Number> prop, Label lbl, String fmt) {
        prop.addListener((obs, oldVal, newVal) -> {
            setLbl(lbl, newVal, fmt);
        });
        lbl.textProperty().setValue(String.format(
              fmt, prop.getValue()
        ));
    }
    
    private void setLbl(Label lbl, Number val, String fmt) {
        lbl.setText(String.format(fmt, val));
    }

    public MainController setTripInfoTabContents(Node node) {
        tripInfoTab.setContent(node);
        return this;
    }

    private void setupTabs() throws Exception {
        // set up tripInfoTab
        tripInfoController = new TripInfoController();
        Flow tripInfoFlow = new Flow(tripInfoController.getClass());
        DefaultFlowContainer tripInfoFlowContainer = new DefaultFlowContainer();
        tripInfoFlow.createHandler(context).start(tripInfoFlowContainer);
        tripInfoTab.setContent(tripInfoFlowContainer.getView());
    }

    @PreDestroy
    public void cleanup() {

    }


}
