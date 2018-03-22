package databaser.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class BaseController {

    @FXML
    public void goTo(ActionEvent event, Page page) throws IOException {
        URL resource = getClass().getResource(page.fxmlPath());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = UserInterface.primaryStage;
        primaryStage.setTitle(page.title());
        primaryStage.setScene(new Scene(root, UserInterface.WIDTH, UserInterface.HEIGHT));
        primaryStage.show();
    }
}
