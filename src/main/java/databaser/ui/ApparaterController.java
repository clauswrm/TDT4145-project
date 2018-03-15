package databaser.ui;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class ApparaterController implements Initializable {

    public Button nyttApparat;
    public TextField navnField;
    public TextField beskrivelseField;
    public ListView<String> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> items = listView.getItems();
        //TODO: Get all the apparats <3
        items.add("Navn: Benchpress\nBeskrivelse: Here be benchies");
        items.add("Two");
        for(int i = 0;i<20;i++){
            items.add("fuck");
        }
    }


    public void handleNyttApparat(){

        String navn = navnField.getText();
        String beskrivelse = beskrivelseField.getText();

        System.out.println("Nytt apparat ved navn "+navn+":\n"+beskrivelse);

        //TODO: Legg til nytt apparat i databasen
    }
}
