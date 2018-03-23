package databaser.ui.controllers;

import databaser.persistence.Apparatøvelse;
import databaser.persistence.Friøvelse;
import databaser.persistence.Notat;
import databaser.persistence.Treningsøkt;
import databaser.persistence.Øvelse;
import databaser.persistence.Øvelsesgruppe;
import databaser.ui.Page;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ØktController extends BaseController {

    public static Treningsøkt økt;
    public static Øvelse currentØvelse;

    @FXML
    public Label øktLabel;

    //Ny øvelse
    @FXML
    public ChoiceBox<Øvelsesgruppe> gruppeChoiceBox;
    @FXML
    public ComboBox<String> øvelseChoiceBox;
    @FXML
    public TextArea nyØvelseKommentar;
    @FXML
    public TextField setsTextField;
    @FXML
    public TextField repsTextField;
    @FXML
    public TextField weightTextField;
    @FXML
    public Button nyØvelseButton;

    @FXML
    public ListView<String> øvelseListView;

    public ArrayList<Øvelse> øvelseArrayList = new ArrayList<>();

    //Notater
    @FXML
    public TextArea notatTextArea;
    @FXML
    public Button nyNotatButton;
    @FXML
    public ListView<Notat> notatListView;

    public void setØkt(Treningsøkt økt) {
        this.økt = økt;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setØktLabel();
        updateGruppeChoiceBox();
        resetØvelseInputFields();
        updateØvelseListView();
        updateNotatListView();
    }

    @FXML
    public void handleNyNotat() {
        Notat notat = new Notat(notatTextArea.getText(), økt);
        notatTextArea.setText("");
        notat.save();


        updateNotatListView();
    }

    @FXML
    public void updateNotatListView() {
        ObservableList<Notat> items = notatListView.getItems();
        items.clear();
        List<Notat> notater = økt.getNotater();
        items.addAll(notater);
    }

    private void setØktLabel() {
        øktLabel.setText(økt.toString());
    }

    @FXML
    public void updateØvelseListView() {
        ObservableList<String> items = øvelseListView.getItems();
        items.clear();

        List<Øvelse> øvelser = økt.getØvelser();

        for (Øvelse øvelse : øvelser) {
            String displayString = "";
            if (øvelse instanceof Friøvelse) {
                String beskrivelse = økt.getStatsForFriøvelse((Friøvelse) øvelse);
                displayString += øvelse.toString() + "\n" + beskrivelse;
                items.add(displayString);

            }
            if (øvelse instanceof Apparatøvelse) {
                Map<String, Integer> stats = økt.getStatsForApparatøvelse((Apparatøvelse) øvelse);
                String vekt = stats.get("kilo").toString();
                String reps = stats.get("reps").toString();
                String sets = stats.get("set").toString();

                displayString = øvelse.toString() + "\nVekt: " + vekt + "\nRepetisjoner: " + reps + "\nAntall sett: " + sets;
                items.add(displayString);

            }

        }
    }

    public void goToDagbok(ActionEvent event) throws IOException {
        goTo(event, Page.DAGBOK);
    }

    private void updateGruppeChoiceBox() {
        ObservableList<Øvelsesgruppe> items = gruppeChoiceBox.getItems();
        items.clear();
        List<Øvelsesgruppe> grupper = Øvelsesgruppe.getAll();
        items.addAll(grupper);
    }

    @FXML
    public void resetØvelseInputFields() {
        øvelseChoiceBox.setValue("");
        setsTextField.setVisible(false);
        repsTextField.setVisible(false);
        weightTextField.setVisible(false);
        nyØvelseKommentar.setVisible(false);
        weightTextField.setText("");
        repsTextField.setText("");
        setsTextField.setText("");
        nyØvelseKommentar.setText("");
    }

    @FXML
    public void setInputFieldVisibility() {
        Øvelse øvelse = findØvelseFromString();
        if (øvelse instanceof Apparatøvelse) {
            setsTextField.setVisible(true);
            repsTextField.setVisible(true);
            weightTextField.setVisible(true);
            nyØvelseKommentar.setVisible(false);
        } else if (øvelse instanceof Friøvelse) {
            setsTextField.setVisible(false);
            repsTextField.setVisible(false);
            weightTextField.setVisible(false);
            nyØvelseKommentar.setVisible(true);
        }
    }

    @FXML
    public void updateØvelseChoiceBox() {
        øvelseChoiceBox.getItems().clear();
        Øvelsesgruppe gruppe = gruppeChoiceBox.getValue();
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
    public void addØvelse() {
        Øvelse øvelse = findØvelseFromString();
        if (øvelse instanceof Apparatøvelse) {
            addApparatØvelse((Apparatøvelse) øvelse);
        } else if (øvelse instanceof Friøvelse) {
            addFriØvelse((Friøvelse) øvelse);
        }
    }

    @FXML
    public void addApparatØvelse(Apparatøvelse øvelse) {
        int kilo = Integer.parseInt(weightTextField.getText());
        int reps = Integer.parseInt(repsTextField.getText());
        int sets = Integer.parseInt(setsTextField.getText());

        resetØvelseInputFields();
        økt.addApparatøvelse(øvelse, kilo, reps, sets);
    }

    @FXML
    public void addFriØvelse(Friøvelse øvelse) {
        String desc = nyØvelseKommentar.getText();
        økt.addFriøvelse(øvelse, desc);
    }

    @FXML
    public void handleNyØvelse() {
        addØvelse();
        updateØvelseListView();
        resetØvelseInputFields();
        updateNotatListView();
    }
}
