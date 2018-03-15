package databaser.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ØvelserController implements Initializable {

    public Button goToMainMenu;
    public ChoiceBox<String> øvelsesGrupper;

    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> items = øvelsesGrupper.getItems();
        //TODO: Get all the apparats <3
        items.add("Navn: Benchpress\nBeskrivelse: Here be benchies");
        items.add("Two");
        for(int i = 0;i<20;i++) {
            items.add("fuck");
        }
    }


    public void goToMainMenu(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/MainMenu.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = (Stage) goToMainMenu.getScene().getWindow();

        primaryStage.setScene(new Scene(root,1280,720));
        primaryStage.show();

    }
}
