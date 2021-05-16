package controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import result.DataDao;
import result.model.DataModel;

import java.util.List;

public class StatControllerTest {
    private DataDao dataDao;
    private StatController underTest;

    private final DataModel productSalary = DataModel.builder()
            .amount(124000L)
            .description("fizetés")
            .build();
    private final DataModel productExpenditure = DataModel.builder()
            .amount(-20000L)
            .description("kiadás")
            .build();
    private final DataModel productShopping = DataModel.builder()
            .amount(-10000L)
            .description("bevásárlás")
            .build();
    private final List<DataModel> dataModel = List.of(productSalary, productExpenditure, productShopping);

    @BeforeEach
    public void init() {
        dataDao = Mockito.mock(DataDao.class);
        underTest = new StatController();
    }

    @Test
    public void getAvgOfAllShouldReturnTheAvgOfAllValueWhenHaveValue(){
        //Given
        double expected = 94000/3D;
        //When
        double actual = underTest.getAvgOfAll(dataModel);
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getAvgOfAllShouldReturnZeroWhenThereAreNoValues(){
        //When
        double actual = underTest.getAvgOfAll(List.of());
        //Then
        Assertions.assertEquals(0,actual);
    }

    @Test
    public void getMaxBuyShouldReturnTheMaximumSpentMoney(){
        //Given
        Long expected = -20000L;
        //When
        Long actual = underTest.getMaxBuy(dataModel);
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getMaxBuyShouldReturnZeroWhenThereAreNoSpentMoney(){
        //Given
        Long expected = 0L;
        //When
        Long actual = underTest.getMaxBuy(List.of(productSalary));
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getMinBuyShouldReturnTheMinimumSpentMoney(){
        //Given
        Long expected = -10000L;
        //When
        Long actual = underTest.getMinBuy(dataModel);
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getMinBuyShouldReturnZeroWhenThereAreNoSpentMoney(){
        //Given
        Long expected = 0L;
        //When
        Long actual = underTest.getMinBuy(List.of(productSalary));
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getAFAShouldReturnTheAfaForAmount() {
        //Given
        double expected = 2700L;
        //When
        double actual = underTest.getAFA(10000L);
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getAverageAmountWithoutAFAShouldReturnTheAverageOfAmountWithoutAfaIfHaveSpentMoney(){
        //Given
        double expected = -10950D;
        //When
        double actual = underTest.getAverageAmountWithoutAFA(dataModel);
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getAverageAmountWithoutAFAShouldReturnZeroWhenNoSpentMoneyHave(){
        //Given
        double expected = 0D;
        //When
        double actual = underTest.getAverageAmountWithoutAFA(List.of(productSalary));
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getSumOfPricesShouldReturnTheSumOfAllSpentMoney(){
        //Given
        double expected = -30000D;
        //When
        double actual = underTest.getSumOfPrices(dataModel);
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getSumOfPricesShouldReturnZeroWhenNoSpentMoneyHave(){
        //Given
        double expected = 0D;
        //When
        double actual = underTest.getSumOfPrices(List.of(productSalary));
        //Then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getAllAfaShouldReturnTheSumOfAfasWhenSpentMoney(){
        //Given
        double expected = -8100D;
        //When
        double actual = underTest.getAllAfa(dataModel);
        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllAfaShouldReturnZeroWhenNoSpentMoneyHave(){
        //Given
        double expected = 0D;
        //When
        double actual = underTest.getAllAfa(List.of(productSalary));
        //Then
        Assertions.assertEquals(expected, actual);
    }
}
