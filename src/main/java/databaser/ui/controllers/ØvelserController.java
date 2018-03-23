package databaser.ui.controllers;

import databaser.persistence.Apparat;
import databaser.persistence.Apparatøvelse;
import databaser.persistence.Friøvelse;
import databaser.persistence.Treningsøkt;
import databaser.persistence.Øvelse;
import databaser.persistence.Øvelsesgruppe;
import databaser.ui.Page;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class ØvelserController extends BaseController {

    @FXML
    public Button goToMainMenu;
    @FXML
    public ChoiceBox<Øvelsesgruppe> gruppeChoiceBox;
    @FXML
    public ChoiceBox<Apparat> apparatChoiceBox;
    @FXML
    public Button addGruppeButton;
    @FXML
    public TextField gruppeNavnTextField;
    @FXML
    public Button addØvelseButton;
    @FXML
    public TextArea øvelseBeskrivelse;
    @FXML
    public TextField øvelseNavn;

    @FXML
    public CheckBox apparatØvelseCheckBox;
    @FXML
    public CheckBox friØvelseCheckBox;

    @FXML
    public ChoiceBox<String> øvelseChoiceBox;

    public ArrayList<Øvelse> øvelseArrayList = new ArrayList<>();

    @FXML
    public ChoiceBox<Øvelsesgruppe> gruppeViewChoiceBox;

    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    public ListView<String> resultListView;


    public void initialize(URL location, ResourceBundle resources) {
        friØvelseCheckBox.setSelected(true);
        apparatChoiceBox.setVisible(false);
        updateGruppeChoiceBox();
        updateApparatChoiceBox();
    }


    @FXML
    public void updateResultListView() {
        Øvelse øvelse = findØvelseFromString();

        if (øvelse instanceof Apparatøvelse) {
            updateApparatResultListView((Apparatøvelse) øvelse);
        }
        if (øvelse instanceof Friøvelse) {
            updateFriResultListView((Friøvelse) øvelse);
        }
    }

    @FXML
    public void updateØvelseChoiceBox() {
        øvelseChoiceBox.getItems().clear();
        Øvelsesgruppe gruppe = gruppeViewChoiceBox.getValue();
        øvelseArrayList.clear();
        øvelseArrayList.addAll(gruppe.getØvelser());

        for (Øvelse øvelse : øvelseArrayList) {
            øvelseChoiceBox.getItems().add(øvelse.toString());
        }
    }

    @FXML
    public Øvelse findØvelseFromString() {
        String ovString = øvelseChoiceBox.getValue();
        for (Øvelse øvelse : øvelseArrayList) {
            if (øvelse.toString().equals(ovString)) {
                return øvelse;
            }
        }
        return null;
    }


    @FXML
    public void updateApparatResultListView(Apparatøvelse øvelse) {
        System.out.println("yolo");
        Map<Treningsøkt, Map<String, Integer>> resultater = øvelse.getProgressForApparatøvelse();
        System.out.println(resultater.size());
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        ObservableList<String> items = resultListView.getItems();

        for (Treningsøkt økt : resultater.keySet()) {
            //TODO:Konvertering mellom LocalDa te og Date
            if(økt.isInInterval(startDate,endDate)){
                Map<String, Integer> stats = økt.getStatsForApparatøvelse(øvelse);
                String vekt = stats.get("kilo").toString();
                String reps = stats.get("reps").toString();
                String sets = stats.get("set").toString();

                items.add(vekt+" "+reps+" "+sets);

            }
        }

    }

    public void updateFriResultListView(Friøvelse øvelse) {

    }

    @FXML
    private void updateGruppeChoiceBox() {
        ObservableList<Øvelsesgruppe> items = gruppeChoiceBox.getItems();
        items.clear();
        List<Øvelsesgruppe> grupper = Øvelsesgruppe.getAll();

        items.addAll(grupper);

        gruppeViewChoiceBox.getItems().clear();
        gruppeViewChoiceBox.getItems().addAll(items);
    }

    @FXML
    public void handleAddØvelse() {
        String beskrivelse = øvelseBeskrivelse.getText();
        String navn = øvelseNavn.getText();

        øvelseBeskrivelse.setText("");
        øvelseNavn.setText("");
        Øvelse øvelse;
        if (apparatØvelseCheckBox.isSelected()) {
            Apparat apparat = apparatChoiceBox.getValue();

            øvelse = new Apparatøvelse(navn, beskrivelse, apparat);
            øvelse.save();
        } else {
            øvelse = new Friøvelse(navn, beskrivelse);
            øvelse.save();
        }
        //TODO: Håndtere øvelsesgrupper

        Øvelsesgruppe gruppe = gruppeChoiceBox.getValue();
        øvelse.addToØvelsesgruppe(gruppe);

    }

    @FXML
    public void updateApparatChoiceBox() {
        ObservableList<Apparat> items = apparatChoiceBox.getItems();
        items.clear();
        List<Apparat> apparater = Apparat.getAll();
        items.addAll(apparater);

    }

    @FXML
    public void handleApparatØvelseCheckBox() {

        if (apparatØvelseCheckBox.isSelected()) {
            friØvelseCheckBox.setSelected(false);
            apparatChoiceBox.setVisible(true);
        } else {
            apparatØvelseCheckBox.setSelected(true);
        }
    }

    @FXML
    public void handleFriØvelseCheckBox() {
        if (friØvelseCheckBox.isSelected()) {
            apparatChoiceBox.setVisible(false);
            apparatØvelseCheckBox.setSelected(false);
        } else {
            friØvelseCheckBox.setSelected(true);
        }
    }

    @FXML
    public void handleNyGruppe() {
        String gruppeNavn = gruppeNavnTextField.getText();
        gruppeNavnTextField.setText("");

        Øvelsesgruppe gruppe = new Øvelsesgruppe(gruppeNavn);
        gruppe.save();
        updateGruppeChoiceBox();
    }

    @FXML
    public void goToMainMenu(ActionEvent event) throws IOException {
        goTo(event, Page.MAIN_MENU);
    }
}
