package databaser.ui;

import databaser.persistence.Treningsøkt;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DagbokController {

    public Button goToMainMenuButton;

    //Ny økt
    public TextField datoTextField;
    public TextField tidspunktTextField;
    public TextField varighetTextField;
    public ChoiceBox<Integer> innsatsChoiceBox;
    public ChoiceBox<Integer> formChoiceBox;
    public Button nyØktButton;

    //Vis økter
    public TextField antallØkterTextField;


    public void goToMainMenu(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/MainMenu.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = (Stage) goToMainMenuButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root,1280,720));
        primaryStage.show();
    }
    public void handleGetØkter(){
        int antall = Integer.parseInt(antallØkterTextField.getText());
        List<Treningsøkt> økter = Treningsøkt.getAll();
    }


    public void handleNewØkt(){
        //TODO:Knytte datofeltet til datovariabelen i denne metoden

        int varighet = Integer.parseInt(varighetTextField.getText());
        varighetTextField.setText("");
        int form = formChoiceBox.getValue();
        int innsats = innsatsChoiceBox.getValue();
        Date dato = Calendar.getInstance().getTime();
        Treningsøkt økt = new Treningsøkt(dato,varighet,form,innsats);
        økt.save();
    }

    private void updateIntegerChoiceBox(ChoiceBox<Integer> box){
        ObservableList<Integer> items = box.getItems();
        for(int i = 1;i<11;i++){
            items.add(i);
        }

    }
    public void initialize() {
        updateIntegerChoiceBox(innsatsChoiceBox);
        updateIntegerChoiceBox(formChoiceBox);
    }
}
