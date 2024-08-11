package com.psjw.hellospringbatch.core.service;

import com.psjw.hellospringbatch.dto.PlayerDto;
import com.psjw.hellospringbatch.dto.PlayerSalaryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class PlayerSalaryServiceTest {

    private PlayerSalaryService playerSalaryService;

    @BeforeEach
    public void setup(){
        playerSalaryService = new PlayerSalaryService();
    }

    @Test
    public void calcSalary(){
        //실패하지 않는 테스트 작성 ->Year.now()를 Mocking
        MockedStatic<Year> mockYearClass = Mockito.mockStatic(Year.class);
        Year mockYear = Mockito.mock(Year.class);
        Mockito.when(mockYear.getValue()).thenReturn(2024);
        mockYearClass.when(Year::now).thenReturn(mockYear);

        //given
        PlayerDto mockPlayer = Mockito.mock(PlayerDto.class);
        Mockito.when(mockPlayer.getBirthYear()).thenReturn(1985);


        //when
        PlayerSalaryDto result = playerSalaryService.calcSalary(mockPlayer);

        //then
        Assertions.assertEquals(result.getSalary(), 39000000);

    }
}