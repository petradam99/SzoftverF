package controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import result.DataDao;
import result.model.DataModel;

import java.util.List;

class AppControllerTest {
    private DataDao dataDao;
    private AppController underTest;
    private final DataModel productOnePositive = DataModel.builder()
            .amount(124000L)
            .description("fizetés")
            .build();
    private final DataModel productOneNegative = DataModel.builder()
            .amount(-10000L)
            .description("bevásárlás")
            .build();
    private final List<DataModel> dataModel = List.of(productOnePositive,productOneNegative);


    @BeforeEach
    public void init() {
        dataDao = Mockito.mock(DataDao.class);
        underTest = new AppController();
    }

    @Test
    public void getTotalPositiveValueShouldReturnOnlyThePositiveValue(){
        //when
        Long actual = underTest.getTotalPositiveValue(dataModel);
        //then
        Assertions.assertEquals(productOnePositive.getAmount(),actual);
    }

    @Test
    public void getTotalNegativeValueShouldReturnOnlyTheNegativeValue(){
        //when
        Long actual = underTest.getTotalNegativeValue(dataModel);
        //then
        Assertions.assertEquals(productOneNegative.getAmount(),actual);
    }

    @Test
    public void getTotalValueShouldReturnTheSumOfAllElement(){
        //Given
        Long expected = productOnePositive.getAmount()+productOneNegative.getAmount();
        //When
        Long actual = underTest.getTotalValue(dataModel);
        //Then
        Assertions.assertEquals(expected, actual);
    }

}