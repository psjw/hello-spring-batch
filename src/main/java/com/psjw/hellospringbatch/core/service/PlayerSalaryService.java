package com.psjw.hellospringbatch.core.service;

import com.psjw.hellospringbatch.dto.PlayerDto;
import com.psjw.hellospringbatch.dto.PlayerSalaryDto;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class PlayerSalaryService {
    public PlayerSalaryDto calcSalary(PlayerDto player) {
        int salary = (Year.now().getValue() - player.getBirthYear()) * 100;
        return PlayerSalaryDto.of(player, salary);
    }
}
