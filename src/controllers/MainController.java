package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import helpers.Helper;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import mapUtils.Flight;
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
    @FXML JFXButton calcButton;
    
    @FXML HBox drivingBox;
    @FXML Label drivePriceLabel;
    @FXML Label driveDurationLabel;

    @FXML HBox flyingBox;
    @FXML Label flyPriceLabel;
    @FXML Label flyDurationLabel;
    
    
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

    private void printlnButtonAction() {
        new Thread(() -> {printTest();}).start();
        Flight bestFlight = mainModel.bestFlight;
        if (bestFlight == null)
            setDebugTextArea("bestFlight = " + null);
        setDebugTextArea("bestFlight = " + mainModel.bestFlight + "\n"
              + Helper.toJsonPretty(bestFlight));
    }
    
    public void setDebugTextArea(String msg) {
        mainModel.tripInfoController.setDebugTextArea(msg);
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

        testButton.setOnAction(e -> {
            printlnButtonAction();});
        calcButton.setOnAction(e -> {
            mainModel.search();
        });

        setupTabs();
        setUpObservables();
    }
    
    private void setUpObservables() {
        setupObservable(mainModel.flightPriceProperty,
              flyPriceLabel,
              "($%.2f)");
        setupObservable(mainModel.flightDurationProperty,
              flyDurationLabel,
              "(%.2f hrs)");
        setupObservable(mainModel.drivePriceProperty,
              drivePriceLabel,
              "($%.2f)");
        setupObservable(mainModel.driveDurationProperty,
              driveDurationLabel,
              "(%.2f hrs)");
    }
    
    private void setupObservable(Property<Number> prop, Label lbl) {
        setupObservable(prop, lbl, "%.2f");
    }
    
    private void setupObservable(Property<Number> prop, Label lbl, String fmt) {
        prop.addListener((obs, oldVal, newVal) -> {
            setLbl(lbl, newVal, fmt);
            flyingBox.getStyleClass().remove(flyingBox.getStyleClass().size() - 1);
            drivingBox.getStyleClass().remove(drivingBox.getStyleClass().size() - 1);
            if (mainModel.flightPrice < mainModel.drivePrice) {
                flyingBox.getStyleClass().add("better-of-the-two");
                drivingBox.getStyleClass().add("worse-of-the-two");
            } else if (mainModel.flightPrice > mainModel.drivePrice) {
                flyingBox.getStyleClass().add("worse-of-the-two");
                drivingBox.getStyleClass().add("better-of-the-two");
            } else {
                flyingBox.getStyleClass().add("worse-of-the-two");
                drivingBox.getStyleClass().add("worse-of-the-two");
            }
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

    /* -- graveyard -------------------------------------------- */
    private final void oldCalculateHander() {
        mainModel.calculateDrive();
        mainModel.calculateFlight();
        double flyPrice = mainModel.flightPrice;
        double flyDuration = mainModel.flightDuration;
        Helper.printDebug("flyPrice = " + flyPrice);
        Helper.printDebug("flyDuration = " + flyDuration);
    }

}
