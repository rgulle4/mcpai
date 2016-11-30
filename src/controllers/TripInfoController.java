package controllers;

import com.jfoenix.controls.*;
import helpers.Helper;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.MainModel;

import javax.annotation.PostConstruct;

@FXMLController(value = "/resources/fxml/TripInfo.fxml", title = "mcpai")
public final class TripInfoController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    private Flow flow;

    private MainModel mainModel;

    private MainController mainController;

    @FXML VBox tripInfoInputsContainer;
    @FXML JFXButton tripInfoTestButton;
    @FXML JFXTextField startLocationInput;
    @FXML JFXTextField destinationLocationInput;
    @FXML JFXDatePicker startDateInput;
    @FXML JFXDatePicker endDateInput;
    @FXML JFXTextField maxBudgetInput;
    @FXML JFXCheckBox isRoundTripInput;
    @FXML JFXSlider timeWeightInput;

    /**
     * Constructor then initialize then init
     */
    public TripInfoController() {
    }


    @FXML
    /**
     * Constructor then initialize then init
     */
    private void initialize() { }

    @PostConstruct
    /**
     * Constructor then initialize then init
     */
    public void init() throws FlowException, VetoException {
        setMainModel((MainModel) context.getRegisteredObject("mainModel"));
        loadDefaultValues();
        setUpAutoSave();
    }

    private void loadDefaultValues() {
        startLocationInput.setText(mainModel.getStartLocation().toString());
        destinationLocationInput.setText(mainModel.getDestinationLocation().toString());
        startDateInput.setValue(mainModel.getStartDate());
        endDateInput.setValue(mainModel.getEndDate());
        isRoundTripInput.setSelected(mainModel.getIsRoundTrip());
        maxBudgetInput.setText(String.valueOf(mainModel.getMaxBudget()));
        timeWeightInput.setValue(mainModel.getTimeWeight());
    }

    private void setUpAutoSave() {
        startLocationInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            mainModel.setStartLocation(startLocationInput.getText().toString());
//            mainModel.calculateDrive();
//            mainModel.calculateFlight();
        });

        destinationLocationInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            mainModel.setDestinationLocation(destinationLocationInput.getText().toString());
//            mainModel.calculateDrive();
//            mainModel.calculateFlight();
        });

        startDateInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            mainModel.setStartDate(newValue);
//            mainModel.calculateFlight();
        });

        endDateInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            mainModel.setEndDate(newValue);
//            mainModel.calculateFlight();
        });

        isRoundTripInput.selectedProperty().addListener((observable, oldValue, newValue) -> { {
                mainModel.setIsRoundTrip(newValue);
            }
        });

        maxBudgetInput.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                mainModel.setMaxBudget(getNumberOfTravelersValue(maxBudgetInput));
            }
        });

        timeWeightInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            mainModel.setTimeWeight(timeWeightInput.getValue());
        });
    }

    public String getStringValue(TextField c) {
        return c.getText();
    }

    public int getNumberOfTravelersValue(TextField c) {
        String stringValue = getStringValue(c);
        if (stringValue.isEmpty()) {
            maxBudgetInput.setText(String.valueOf(mainModel.getMaxBudget()));
            return -1;
        }
        return Integer.parseInt(getStringValue(c));
    }

    private void printTest() {
        System.out.print(this + "...");
        Helper.printObject(mainModel, "mainModel");
    }

    private void testButtonAction() {
        this.printTest();
    }

    public ViewFlowContext getContext() { return context; }
    public TripInfoController setContext(ViewFlowContext context) {
        this.context = context;
        return this;
    }

    public Flow getFlow() { return flow; }
    public TripInfoController setFlow(Flow flow) {
        this.flow = flow;
        return this;
    }

    public MainModel getMainModel() { return mainModel; }
    public TripInfoController setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
        return this;
    }

    public MainController getMainController() { return mainController; }
    public TripInfoController setMainController(MainController mainController) {
        this.mainController = mainController;
        return this;
    }
}
