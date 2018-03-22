package databaser.ui;

import databaser.ui.controllers.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class TrainingApp extends Application {

    public static final int WIDTH = 660;
    public static final int HEIGHT = 900;
    public static final String appName = "Train-n-Gain";
    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TrainingApp.primaryStage = primaryStage;

        URL resource = getClass().getResource(Page.MAIN_MENU.fxmlPath());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        MainMenuController controller = loader.getController();

        primaryStage.setTitle(Page.MAIN_MENU.title());
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
