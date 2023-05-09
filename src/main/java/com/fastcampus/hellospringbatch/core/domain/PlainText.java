package com.fastcampus.hellospringbatch.core.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@DynamicUpdate //entity 일부 컬럼 변경된 값들중에 일부만 변경이 되었을때 변경된 컬럼에 대해서만 업데이트
@Table(name = "plain_text")
public class PlainText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String text;

}
