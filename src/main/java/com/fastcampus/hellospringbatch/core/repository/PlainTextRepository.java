package com.fastcampus.hellospringbatch.core.repository;

import com.fastcampus.hellospringbatch.core.domain.PlainText;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlainTextRepository extends JpaRepository<PlainText, Integer> {
    Page<PlainText> findBy(Pageable pageable); //Pageable을 통해 페이지 사이즈 만큼 읽음
}
