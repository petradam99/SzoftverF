package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import result.DataDao;
import result.model.DataModel;

public class AddController {

    @FXML
    public TextField amount;
    @FXML
    public TextField description;

    private DataDao dataDao;

    private AppController app;

    public void setApp(AppController app) {
        this.app = app;
    }


    public void initialize() {
        dataDao = DataDao.getInstance();
    }

    public void returnToApp(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void AddRow(MouseEvent mouseEvent) {
        try {
            long osszeg=Long.parseLong(amount.getText());
            DataModel mod= DataModel.builder()
                    .id(null)
                    .amount(osszeg)
                    .description(description.getText())
                    .build();
            dataDao.persist(mod);
            app.setGraphic();
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("A bemenő adat csak szám lehet");
            alert.showAndWait();
            throw new IllegalArgumentException();
        }
    }
}