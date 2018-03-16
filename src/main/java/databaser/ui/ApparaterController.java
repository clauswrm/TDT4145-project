package databaser.ui;

import databaser.persistence.Apparat;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class ApparaterController implements Initializable {

    public Button nyttApparat;
    public TextField navnField;
    public TextField beskrivelseField;
    public ListView<String> listView;
    public Button goToMainMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateListView();
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

    public void updateListView(){
        ObservableList<String> items = listView.getItems();
        items.clear();
        List<Apparat> apparater = Apparat.getAll();
        for(int i = 0;i<apparater.size();i++){
            items.add(apparater.get(i).toString());
        }
        //items.notifyAll();

    }

    public void handleNyttApparat(){

        String navn = navnField.getText();
        String beskrivelse = beskrivelseField.getText();
        Apparat nyttApparat = new Apparat(navn,beskrivelse);
        nyttApparat.save();

        navnField.setText("");
        beskrivelseField.setText("");
        updateListView();

    }
}
