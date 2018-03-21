package databaser.ui;

import databaser.persistence.Treningsøkt;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class DagbokController {

    public Button goToMainMenuButton;

    //Ny økt
    @FXML
    public DatePicker datoPicker;
    @FXML
    public TextField varighetTextField;
    @FXML
    public ChoiceBox<Integer> innsatsChoiceBox;
    @FXML
    public ChoiceBox<Integer> formChoiceBox;
    @FXML
    public Button nyØktButton;

    //Vis økter
    @FXML
    public TextField antallØkterTextField;
    @FXML
    public Button hentØkterButton;
    @FXML
    public ScrollPane økterPane;
    @FXML
    public ListView<Button> øktView;

    @FXML
    public void goToMainMenu(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/MainMenu.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = (Stage) goToMainMenuButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    @FXML
    public void handleGetØkter() {
        int antall = Integer.parseInt(antallØkterTextField.getText());
        List<Treningsøkt> økter = Treningsøkt.getAll();
        antall = (økter.size() < antall) ? økter.size() : antall;
        økter = økter.subList(økter.size() - antall, økter.size());

        ObservableList<Button> items = øktView.getItems();
        items.clear();
        updateØkter(økter, items);
    }

    private void updateØkter(List<Treningsøkt> økter, ObservableList<Button> items) {
        for (Treningsøkt økt : økter) {

            Button button = new Button();
            button.setText(økt.toString());

            button.setOnAction(event -> {
                try {
                    ØktController.økt = økt;
                    goToØkt(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            items.add(button);
        }
    }

    @FXML
    public void goToØkt(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/Økt.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = (Stage) økterPane.getScene().getWindow();
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    @FXML
    public void handleNewØkt() {
        int varighet = Integer.parseInt(varighetTextField.getText());
        varighetTextField.setText(""); // Reset text field
        int form = formChoiceBox.getValue();
        int innsats = innsatsChoiceBox.getValue();

        // Convert from java.time.LocalDate to java.util.Date
        LocalDate localDate = datoPicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        Treningsøkt økt = new Treningsøkt(date, varighet, form, innsats);
        økt.save();

        // Update list of Økter
        updateDagbokView();
    }

    @FXML
    private void updateIntegerChoiceBox(ChoiceBox<Integer> box) {
        ObservableList<Integer> items = box.getItems();
        for (int i = 1; i < 11; i++) {
            items.add(i);
        }
    }

    @FXML
    private void updateDagbokView() {
        ObservableList<Button> items = øktView.getItems();
        items.clear();
        List<Treningsøkt> økter = Treningsøkt.getAll();
        updateØkter(økter, items);
    }

    public void initialize() {
        updateIntegerChoiceBox(innsatsChoiceBox);
        updateIntegerChoiceBox(formChoiceBox);
        updateDagbokView();
    }
}
