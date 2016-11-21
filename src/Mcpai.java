import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyphLoader;
import controllers.MainController;
import controllers.TripInfoController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.MainModel;
import resources.ResourceFinder;

import java.io.IOException;
import java.util.ArrayList;

public final class Mcpai extends Application {

    /* -- settings -------------------------------------------- */

    private static final double MAIN_WIDTH = 1066;
    private static final double MAIN_HEIGHT = 800;
    private static final double MAIN_MIN_WIDTH = 800;
    private static final double MAIN_MIN_HEIGHT = 600;
    private static final String WINDOW_TITLE = "mcpai";
    private static final String
          PARENT_FXML_FILE_PATH = "resources/fxml/MainView.fxml";
    private static final ArrayList<String>
          DEFAULT_CSS_FILES_PATHS = new ArrayList<String>() {{
              add("resources/css/jfoenix-fonts.css");
              add("resources/css/jfoenix-design.css");
              add("resources/css/jfoenix-main-demo.css");
              add("resources/css/custom.css"); }};

    public static final String TRIP_INFO_FXML_PATH
          = "resources/fxml/TripInfo.fxml";

    /* -- start ----------------------------------------------- */

    private static final MainModel MAIN_MODEL
          = new MainModel("mainModelTestString, set in Mcpai");
    private static final MainController MAIN_CONTROLLER
          = new MainController();
    public static final ResourceFinder RESOURCE_FINDER
          = new ResourceFinder();

    @FXMLViewFlowContext
    private static final ViewFlowContext CONTEXT
          = new ViewFlowContext();
    private static final DefaultFlowContainer DEFAULT_FLOW_CONTAINER
          = new DefaultFlowContainer();
    private static final Flow FLOW
          = new Flow(MAIN_CONTROLLER.getClass());


    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Thread(()->{
            try {
                SVGGlyphLoader.loadGlyphsFont(getClass().getResourceAsStream(
                      "resources/fonts/icomoon.svg"),
                      "icomoon.svg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Stage stage = primaryStage;
        CONTEXT.register("Stage", stage);
        CONTEXT.register("mainObject", this);
        CONTEXT.register("defaultFlowContainer", DEFAULT_FLOW_CONTAINER);
        CONTEXT.register("mainModel", MAIN_MODEL);
        FLOW.createHandler(CONTEXT).start(DEFAULT_FLOW_CONTAINER);

        JFXDecorator decorator = new JFXDecorator(
              stage,
              DEFAULT_FLOW_CONTAINER.getView());

        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, MAIN_WIDTH, MAIN_HEIGHT);
        addDefaultCssToScene(scene);
        stage.setMinWidth(MAIN_MIN_WIDTH);
        stage.setMinHeight(MAIN_MIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }


    private Parent getDefaultRoot() {
        return getRoot(PARENT_FXML_FILE_PATH);
    }

    private Parent getRoot(String fxmlFilePath) {
        try {
            return FXMLLoader.load(getClass().getResource(fxmlFilePath));
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    private void addDefaultCssToScene(Scene scene) {
        for (String cssFilePath : DEFAULT_CSS_FILES_PATHS)
            scene.getStylesheets().add(this.getClass()
                  .getResource(cssFilePath).toExternalForm());
    }
}
