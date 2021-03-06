package databaser.ui.controllers;

import databaser.ui.Page;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends BaseController {

    @FXML
    public Button apparaterButton;
    @FXML
    public Button øvelserButton;
    @FXML
    public Button dagbokButton;
    @FXML
    public Button instillingerButton;

    @FXML
    public void goToDagbok(ActionEvent event) throws IOException {
        goTo(event, Page.DAGBOK);
    }

    @FXML
    public void goToApparater(ActionEvent event) throws IOException {
        goTo(event, Page.APPARATER);
    }

    @FXML
    public void goToØvelser(ActionEvent event) throws IOException {
        goTo(event, Page.ØVELSER);
    }

    @FXML
    public void goToInstillinger(ActionEvent event) throws IOException {
        goTo(event, Page.INSTILLINGER);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
