package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import result.DataDao;
import result.model.DataModel;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import org.tinylog.Logger;

public class AppController {

    @FXML
    public Label totalPos;
    @FXML
    public Label totalNeg;
    @FXML
    public Label totalVal;
    @FXML
    private TableColumn<DataModel, Long> id;
    @FXML
    private TableView<DataModel> dataTable;
    @FXML
    private TableColumn<DataModel, ZonedDateTime> created;
    @FXML
    private TableColumn<DataModel, Long> amount;
    @FXML
    private TableColumn<DataModel, String> description;

    private DataDao dataDao;

    public void initialize() {
        dataDao = DataDao.getInstance();
        setGraphic();
    }

    /**
     * Az összes bevétel beállítása.
     * @param value A value értéket kapja meg a függvény, és állítja be értékként.
     */
    @FXML
    public void setTotalPos(Long value) {
        if (value != null) {
            this.totalPos.setText(value.toString() + " Ft");
            Logger.info("New salary");
        } else this.totalPos.setText("0 Ft");
    }

    /**
     * Az összes kiadás beállítása.
     * @param value A value értéket kapja meg a függvény, és állítja be értékként.
     */
    @FXML
    public void setTotalNeg(Long value) {
        if (value != null) {
            this.totalNeg.setText(value.toString() + " Ft");
            Logger.info("New outlay");
        } else this.totalNeg.setText("0 Ft");
    }

    /**
     * Az összes érték összegzése.
     * @param value A value értéket kapja meg a függvény, és állítja be értékként.
     */
    @FXML
    public void setTotalVal(Long value) {
        if (value != null) {
            this.totalVal.setText(value.toString() + " Ft");
            Logger.info("New balance");
        } else this.totalVal.setText("0 Ft");
    }

    /**
     * Beállítja a grafikát, a táblázatot és a táblázat elemeit.
     */
    @FXML
    public void setGraphic() {
        List<DataModel> dataList = dataDao.findAll();
        created.setCellValueFactory(new PropertyValueFactory<>("created"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        created.setCellFactory(column -> {
            TableCell<DataModel, ZonedDateTime> cell = new TableCell<DataModel, ZonedDateTime>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);

                @Override
                protected void updateItem(ZonedDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.format(formatter));
                    }
                }
            };
            return cell;
        });

        ObservableList<DataModel> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(dataList);
        dataTable.setItems(observableResult);
        setTotalPos(getTotalPositiveValue(dataList));
        setTotalNeg(getTotalNegativeValue(dataList));
        setTotalVal(getTotalValue(dataList));
    }

    /**
     * Visszaadja a bevételek összegét.
     * @param dataModel Egy dataModel listát kap paraméterül
     * @return Visszatér a 0-tól nagyobb elemek összegével.
     */
    public Long getTotalPositiveValue(List<DataModel> dataModel){
        return dataModel.stream().filter(element -> element.getAmount()>0).mapToLong(DataModel::getAmount).sum();
    }

    /**
     * Visszaadja a kiadások összegét.
     * @param dataModel Egy dataModel listát kap paraméterül.
     * @return Visszatér a 0-tól kisebb elemek összegével.
     */
    public Long getTotalNegativeValue(List<DataModel> dataModel){
        return dataModel.stream().filter(element -> element.getAmount()<0).mapToLong(DataModel::getAmount).sum();
    }

    /**
     * Visszaadja a bevételek és kiadások összegzését.
     * @param dataModel Egy dataModel listát kap paraméterül.
     * @return Visszatér az összes elem összegével.
     */
    public Long getTotalValue(List<DataModel> dataModel){
        return dataModel.stream().mapToLong(DataModel::getAmount).sum();
    }

    /**
     * Megjeleníti a hozzáadáshoz tartozó ablakot
     * @param mouseEvent Kattintásra történő megnyitást biztosítja a paraméter.
     */
    public void goToAdd(MouseEvent mouseEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/add.fxml"));
            Parent root = fxmlLoader.load();
            AddController controller = fxmlLoader.getController();
            controller.setApp(this);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            Logger.info("Opened add screen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elem eltávolítása az adatbázisból és a grafikai felületről.
     * @param mouseEvent Kattintásra történő eltávolításhoz.
     */
    @FXML
    public void goToRemove(MouseEvent mouseEvent) {
        try {
            dataDao.remove(dataTable.getSelectionModel().getSelectedItem());
            setGraphic();
        } catch (Exception e) {
            throw new IllegalStateException();
        }

    }

    /**
     * A statisztika megjelenítése új grafikus ablakban.
     * @param mouseEvent Kattintásra történő átirányításhoz.
     * @throws IOException Amennyiben nem lehet megnyitni a statisztika oldalát.
     */
    @FXML
    public void goToStat(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/stat.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
        Logger.info("Opened the statistics site.");
    }
}
