package com.mellau.spring.jpa.h2.persistence;

import com.mellau.spring.jpa.h2.common.enums.Sex;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
//@Accessors(chain = true)
public class UserEntity  {

    /*
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;*/

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull()
    private String email;

    @Max(100)
    @Min(0)
    @Column(columnDefinition = "integer default 0")
    private Integer level;

    @Max(99999)
    @Min(0)
    @Column(columnDefinition = "integer default 0")
    private Integer gold;


    @NotNull()
    private Sex sex;
}
