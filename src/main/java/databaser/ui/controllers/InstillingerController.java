package databaser.ui.controllers;

import databaser.persistence.ActiveDomainObject;
import databaser.ui.Page;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InstillingerController extends BaseController {

    @FXML
    public Button oppdater;
    @FXML
    public TextField urlField;
    @FXML
    public TextField brukernavnField;
    @FXML
    public TextField passordField;
    @FXML
    public Button goToMainMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        urlField.setText(ActiveDomainObject.dbURL);
        brukernavnField.setText(ActiveDomainObject.dbUsername);
        passordField.setText(ActiveDomainObject.dbPassword);
    }

    @FXML
    public void goToMainMenu(ActionEvent event) throws IOException {
        goTo(event, Page.MAIN_MENU);
    }

    @FXML
    public void handleOppdater() {
        ActiveDomainObject.dbURL = urlField.getText();
        ActiveDomainObject.dbUsername = brukernavnField.getText();
        ActiveDomainObject.dbPassword = passordField.getText();
    }
}
