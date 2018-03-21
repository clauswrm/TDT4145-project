package databaser.ui;

import databaser.persistence.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class ØvelserController implements Initializable {

    public Button goToMainMenu;
    public ChoiceBox<Øvelsesgruppe> gruppeChoiceBox;
    public ChoiceBox<Apparat> apparatChoiceBox;
    public Button addGruppeButton;
    public TextField gruppeNavnTextField;
    public Button addØvelseButton;
    public TextArea øvelseBeskrivelse;
    public TextField øvelseNavn;

    public CheckBox apparatØvelseCheckBox;
    public CheckBox friØvelseCheckBox;

    public ChoiceBox<Øvelse>  øvelseListView;
    public ChoiceBox<Øvelsesgruppe> gruppeViewChoiceBox;

    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public ListView<String> resultListView;


    public void initialize(URL location, ResourceBundle resources) {
        friØvelseCheckBox.setSelected(true);
        apparatChoiceBox.setVisible(false);
        updateGruppeChoiceBox();
        updateApparatChoiceBox();
    }

    public void updateØvelseListView(){
        ObservableList<Øvelse> items = øvelseListView.getItems();
        items.clear();
        Øvelsesgruppe gruppe = gruppeViewChoiceBox.getValue();
        List<Øvelse> øvelser = gruppe.getØvelser();

        for(Øvelse øvelse:øvelser){
            items.add(øvelse);
        }
    }
    public void updateResultListView(){
        Øvelse øvelse = øvelseListView.getValue();

        if(øvelse instanceof Apparatøvelse){
            updateApparatResultListView((Apparatøvelse) øvelse);
        }
        if(øvelse instanceof Friøvelse){
            updateFriResultListView((Friøvelse) øvelse);
        }

    }
    public void updateApparatResultListView(Apparatøvelse øvelse){
        Map<Treningsøkt, Map<String, Integer>> resultater = øvelse.getProgressForApparatøvelse();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        for(Treningsøkt økt: resultater.keySet()){
            //TODO:Konvertering mellom LocalDate og Date
        }

    }
    public void updateFriResultListView(Friøvelse øvelse){

    }

    private void updateGruppeChoiceBox(){
        ObservableList<Øvelsesgruppe> items = gruppeChoiceBox.getItems();
        items.clear();
        List<Øvelsesgruppe> grupper = Øvelsesgruppe.getAll();

        for(Øvelsesgruppe gruppe: grupper){
            items.add(gruppe);
        }
        gruppeViewChoiceBox.getItems().clear();
        gruppeViewChoiceBox.getItems().addAll(items);
    }

    public void handleAddØvelse(){
        String beskrivelse = øvelseBeskrivelse.getText();
        String navn = øvelseNavn.getText();

        øvelseBeskrivelse.setText("");
        øvelseNavn.setText("");
        Øvelse øvelse;
        if(apparatØvelseCheckBox.isSelected()){
            Apparat apparat = apparatChoiceBox.getValue();

            øvelse = new Apparatøvelse(navn,beskrivelse,apparat);
            øvelse.save();
        }
        else{
            øvelse = new Friøvelse(navn,beskrivelse);
            øvelse.save();
        }
        //TODO: Håndtere øvelsesgrupper

        Øvelsesgruppe gruppe = gruppeChoiceBox.getValue();
        øvelse.addToØvelsesgruppe(gruppe);

    }
    public void updateApparatChoiceBox(){
        ObservableList<Apparat> items = apparatChoiceBox.getItems();
        items.clear();
        List<Apparat> apparater = Apparat.getAll();
        for(Apparat apparat:apparater){
            items.add(apparat);
        }

    }
    public void handleApparatØvelseCheckBox(){

        if(apparatØvelseCheckBox.isSelected()){
            friØvelseCheckBox.setSelected(false);
            apparatChoiceBox.setVisible(true);
        }
        else{
            apparatØvelseCheckBox.setSelected(true);
        }
    }
    public void handleFriØvelseCheckBox(){
        if(friØvelseCheckBox.isSelected()){
            apparatChoiceBox.setVisible(false);
            apparatØvelseCheckBox.setSelected(false);
        }
        else{
            friØvelseCheckBox.setSelected(true);
        }
    }

    public void handleNyGruppe(){
        String gruppeNavn =  gruppeNavnTextField.getText();
        gruppeNavnTextField.setText("");

        Øvelsesgruppe gruppe = new Øvelsesgruppe(gruppeNavn);
        gruppe.save();
        updateGruppeChoiceBox();
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
