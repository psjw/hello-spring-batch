package com.fastcampus.hellospringbatch.core.repository;

import com.fastcampus.hellospringbatch.core.domain.ResultText;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;

public interface ResultTextRepository extends JpaRepository<ResultText, Integer> {
}
