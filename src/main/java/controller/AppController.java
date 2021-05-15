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

    @FXML
    public void setTotalPos(Long value) {
        if (value != null) {
            this.totalPos.setText(value.toString() + " Ft");
        } else this.totalPos.setText("0 Ft");
    }
    @FXML
    public void setTotalNeg(Long value) {
        if (value != null) {
            this.totalNeg.setText(value.toString() + " Ft");
        } else this.totalNeg.setText("0 Ft");
    }

    @FXML
    public void setTotalVal(Long value) {
        if (value != null) {
            this.totalVal.setText(value.toString() + " Ft");
        } else this.totalVal.setText("0 Ft");
    }

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

    public Long getTotalPositiveValue(List<DataModel> dataModel){
        return dataModel.stream().filter(element -> element.getAmount()>0).mapToLong(DataModel::getAmount).sum();
    }

    public Long getTotalNegativeValue(List<DataModel> dataModel){
        return dataModel.stream().filter(element -> element.getAmount()<0).mapToLong(DataModel::getAmount).sum();
    }

    public Long getTotalValue(List<DataModel> dataModel){
        return dataModel.stream().mapToLong(DataModel::getAmount).sum();
    }

    public void goToAdd(MouseEvent mouseEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/add.fxml"));
            Parent root = fxmlLoader.load();
            AddController controller = fxmlLoader.getController();
            controller.setApp(this);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToRemove(MouseEvent mouseEvent) {
        try {
            dataDao.remove(dataTable.getSelectionModel().getSelectedItem());
            setGraphic();
        } catch (Exception e) {
            throw new IllegalStateException();
        }

    }

    @FXML
    public void goToStat(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/stat.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }
}
