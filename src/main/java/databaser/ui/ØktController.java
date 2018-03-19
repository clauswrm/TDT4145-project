package databaser.ui;

import databaser.persistence.Treningsøkt;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ØktController {

    public static Treningsøkt økt;

    public Label øktLabel;

    public void setØkt(Treningsøkt økt){
        this.økt = økt;
    }

    public void initialize(){
        setØktLabel();
    }
    private void setØktLabel(){
        øktLabel.setText(økt.toString());
    }

    public void goToDagbok(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/Dagbok.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        Stage primaryStage = (Stage) øktLabel.getScene().getWindow();
        primaryStage.setScene(new Scene(root,1280,720));
        primaryStage.show();
    }


}
