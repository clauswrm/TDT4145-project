package databaser.ui;

import databaser.persistence.Apparat;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ApparaterController extends BaseController implements Initializable {

    @FXML
    public Button nyttApparat;
    @FXML
    public TextField navnField;
    @FXML
    public TextField beskrivelseField;
    @FXML
    public ListView<Apparat> listView;
    @FXML
    public Button goToMainMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateListView();
    }

    @FXML
    public void goToMainMenu(ActionEvent event) throws IOException {
        goTo(event, Page.MAIN_MENU);
    }

    @FXML
    public void updateListView() {
        ObservableList<Apparat> items = listView.getItems();
        items.clear();
        List<Apparat> apparater = Apparat.getAll();
        items.addAll(apparater);
        //items.notifyAll();
    }

    @FXML
    public void handleNyttApparat() {
        String navn = navnField.getText();
        String beskrivelse = beskrivelseField.getText();
        Apparat nyttApparat = new Apparat(navn, beskrivelse);
        nyttApparat.save();

        navnField.setText("");
        beskrivelseField.setText("");
        updateListView();
    }
}
