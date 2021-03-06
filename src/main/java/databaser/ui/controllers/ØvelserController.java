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
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class ØvelserController extends BaseController {

    public ComboBox<Øvelsesgruppe> gComboBox;
    public ComboBox<Øvelse> øComboBox;

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
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
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
        Map<Treningsøkt, Map<String, Integer>> resultater = øvelse.getProgressForApparatøvelse();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();


        ObservableList<String> items = resultListView.getItems();
        items.clear();
        for (Treningsøkt økt : resultater.keySet()) {
            if(økt.isInInterval(startDate,endDate)){
                Map<String, Integer> stats = økt.getStatsForApparatøvelse(øvelse);
                String vekt = stats.get("kilo").toString();
                String reps = stats.get("reps").toString();
                String sets = stats.get("set").toString();

                items.add(økt.getDato().toString()+"\nVekt: "+vekt+"\nRepetisjoner: "+reps+"\nSett: "+sets);

            }
        }

    }

    public void updateFriResultListView(Friøvelse øvelse) {
        Map<Treningsøkt,String> resultater = øvelse.getProgressForFriøvelse();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        ObservableList<String> items = resultListView.getItems();
        items.clear();
        for(Treningsøkt økt: resultater.keySet()){
            if(økt.isInInterval(startDate,endDate)){
                String res = økt.getStatsForFriøvelse(øvelse);
                items.add("Resultat: "+res);
            }
        }

    }
    public void addØvelseToGruppe(){
        Øvelse øvelse = øComboBox.getValue();
        Øvelsesgruppe gruppe = gComboBox.getValue();

        if(!gruppe.getØvelser().contains(øvelse)){
            øvelse.addToØvelsesgruppe(gruppe);
        }

    }
    public void updateØvelseToGroupComboBox(){
        ObservableList<Øvelse> items = øComboBox.getItems();
        items.clear();

        items.addAll(Apparatøvelse.getAll());
        items.addAll(Friøvelse.getAll());
    }

    @FXML
    private void updateGruppeChoiceBox() {
        ObservableList<Øvelsesgruppe> items = gruppeChoiceBox.getItems();
        items.clear();
        List<Øvelsesgruppe> grupper = Øvelsesgruppe.getAll();

        items.addAll(grupper);

        gruppeViewChoiceBox.getItems().clear();
        gruppeViewChoiceBox.getItems().addAll(items);
        gComboBox.getItems().clear();
        gComboBox.getItems().addAll(items);
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
