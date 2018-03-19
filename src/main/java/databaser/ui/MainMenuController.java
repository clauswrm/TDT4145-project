package databaser.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainMenuController {

    public Button apparaterButton;
    public Button øvelserButton;
    public Button dagbokButton;

    public void goToDagbok(ActionEvent event) throws IOException{
        URL resource = getClass().getResource("/Dagbok.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = (Stage) dagbokButton.getScene().getWindow();
        primaryStage.setScene(new Scene(root,1280,720));
        primaryStage.show();
    }

    public void goToApparater(ActionEvent event) throws IOException{
        URL resource = getClass().getResource("/Apparater.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = (Stage) apparaterButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root,1280,720));
        primaryStage.show();

    }
    public void goToØvelser(ActionEvent event) throws IOException{
        URL resource = getClass().getResource("/Øvelser.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = (Stage) øvelserButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root,1280,720));
        primaryStage.show();

    }
}
