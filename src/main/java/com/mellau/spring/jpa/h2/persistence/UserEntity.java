package com.mellau.spring.jpa.h2.persistence;

import com.mellau.spring.jpa.h2.common.enums.Sex;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
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
