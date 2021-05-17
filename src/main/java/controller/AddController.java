package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import result.DataDao;
import result.model.DataModel;

import org.tinylog.Logger;

public class AddController {

    @FXML
    public TextField amount;
    @FXML
    public TextField description;

    private DataDao dataDao;

    private AppController app;

    /**
     * Az AppController osztály beállítása.
     */
    public void setApp(AppController app) {
        this.app = app;
    }

    public void initialize() {
        dataDao = DataDao.getInstance();
    }

    /**
     * Visszatérés a kezdő oldalra, megnyitott ablak bezárása.
     */
    public void returnToApp(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Logger.info("Returned to the main side");
    }

    /**
     * @param mouseEvent Kattintás szükséges hozzá
     * Új elem hozzáadása az adatbázishoz, majd frissíti a táblázatot.
     * Amennyiben az összegnél nem szám szerepel, hiba.
     */
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
            Logger.info("Added a new row.");
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