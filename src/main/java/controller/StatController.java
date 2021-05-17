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

import org.tinylog.Logger;

public class StatController {

    public Label avg;
    public Label maxBuy;
    public Label minBuy;
    public Label withoutAfa;
    public Label Afa;
    public Label allSum;

    ObservableList<String> choices= FXCollections.observableArrayList("napi","havi");

    private DataDao dataDao;

    /**
     * Visszatér a kezdő oldalra.
     * @param mouseEvent Kattintásra történő visszatéréshez.
     */
    @FXML
    public void returnToApp(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Logger.info("Returned to the main site");
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

    /**
     * Visszaadja a vásárlások átlagos összegét.
     * @param dataModels Egy dataModel listát kap paraméterül.
     * @return Visszatér az összes elem átlagával, ha nincs elem az adatbázisban, akkor 0-val.
     */
    public Double getAvgOfAll(List<DataModel> dataModels){
        return dataModels.stream().filter(dataModel->dataModel.getAmount()<0)
                .mapToLong(DataModel::getAmount).average().orElse(0);
    }

    /**
     * Visszatér a legnagyobb elköltött összeggel.
     * @param dataModels Egy dataModel listát kap paraméterül.
     * @return Visszatér a legkisebb 0-tól kisebb számmal, ha nincs elem az adatbázisban, akkor 0-val.
     */
    public Long getMaxBuy(List<DataModel> dataModels){
        return dataModels.stream().filter(dataModel->dataModel.getAmount()<0)
                .mapToLong(DataModel::getAmount).min().orElse(0);
    }

    /**
     * Visszatér a legkisebb elköltött összeggel.
     * @param dataModels Egy dataModel listát kap paraméterül.
     * @return Visszatér a legnagyobb 0-tól kisebb számmal, ha nincs elem az adatbázisban, akkor 0-val.
     */
    public Long getMinBuy(List<DataModel> dataModels){
        return dataModels.stream()
                .filter(dataModel->dataModel.getAmount()<0)
                .mapToLong(DataModel::getAmount).max().orElse(0);
    }

    /**
     * Visszaadja a költekezések Áfa nélküli átlagát.
     * @param dataModels Egy dataModel listát kap paraméterül.
     * @return Visszatér a 0-tól kisebb számok átlagával, amely számok a költekezések áfa nélküli értékeit jelentik, és 0-val, ha nincs elem az adatbázisban.
     */
    public double getAverageAmountWithoutAFA(List<DataModel> dataModels){
        return dataModels.stream()
                .filter(dataModel->dataModel.getAmount()<0)
                .mapToDouble(amount ->amount.getAmount()- getAFA(amount.getAmount()))
                .average().orElse(0);
    }

    /**
     * Visszaadja az eddigi összes kiadás összegét.
     * @param dataModels Egy dataModel listát kap paraméterül.
     * @return Visszaadja az összes 0-tól kisebb érték összegét.
     */
    public Long getSumOfPrices(List<DataModel> dataModels){
        return dataModels.stream()
                .filter(dataModel->dataModel.getAmount()<0)
                .mapToLong(DataModel::getAmount).sum();
    }

    /**
     * Visszaadja az eddigi összes költekezés áfájanak az összegét.
     * @param dataModels Egy dataModel listát kap paraméterül.
     * @return Visszatér a 0-tól kisebb számokból vett áfáknak az összegével.
     */
    public Double getAllAfa(List<DataModel> dataModels){
        return dataModels.stream()
                .filter(dataModel->dataModel.getAmount()<0)
                .mapToDouble(amount -> getAFA(amount.getAmount())).sum();
    }

    /**
     * Áfa számítása.
     * @param amount A kiadás értéke.
     * @return Visszatér az áfa értékével.
     */
    public double getAFA(Long amount){
        return 0.27*amount;
    }
}
