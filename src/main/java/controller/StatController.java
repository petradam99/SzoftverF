package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import result.DataDao;
import result.model.DataModel;

import java.util.List;

public class StatController {

    public Label avg;
    public Label maxBuy;
    public Label minBuy;
    public Label withoutAfa;
    public Label Afa;
    public Label allSum;

    ObservableList<String> choices= FXCollections.observableArrayList("napi","havi");

    private DataDao dataDao;

    @FXML
    public void returnToApp(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        dataDao = DataDao.getInstance();
        List<DataModel> dataModels = dataDao.findAll();
        avg.setText(String.valueOf(getAvgOfAll(dataModels)));
        maxBuy.setText(String.valueOf(getMaxBuy(dataModels)));
        minBuy.setText(String.valueOf(getMinBuy(dataModels)));
        withoutAfa.setText(String.valueOf(getAverageAmountWithoutAFA(dataModels)));
        Afa.setText(String.valueOf(getAllAfa(dataModels)));
        allSum.setText(String.valueOf(getSumOfPrices(dataModels)));
    }

    public Double getAvgOfAll(List<DataModel> dataModels){
        return dataModels.stream().mapToLong(DataModel::getAmount).average().orElse(0);
    }

    public Long getMaxBuy(List<DataModel> dataModels){
        return dataModels.stream().filter(dataModel->dataModel.getAmount()<0)
                .mapToLong(DataModel::getAmount).min().orElse(0);
    }

    public Long getMinBuy(List<DataModel> dataModels){
        return dataModels.stream()
                .filter(dataModel->dataModel.getAmount()<0)
                .mapToLong(DataModel::getAmount).max().orElse(0);
    }

    public double getAverageAmountWithoutAFA(List<DataModel> dataModels){
        return dataModels.stream()
                .filter(dataModel->dataModel.getAmount()<0)
                .mapToDouble(amount ->amount.getAmount()- getAFA(amount.getAmount()))
                .average().orElse(0);
    }

    public Long getSumOfPrices(List<DataModel> dataModels){
        return dataModels.stream()
                .filter(dataModel->dataModel.getAmount()<0)
                .mapToLong(DataModel::getAmount).sum();
    }

    public Double getAllAfa(List<DataModel> dataModels){
        return dataModels.stream()
                .filter(dataModel->dataModel.getAmount()<0)
                .mapToDouble(amount -> getAFA(amount.getAmount())).sum();
    }

    public double getAFA(Long amount){
        return 0.27*amount;
    }
}
