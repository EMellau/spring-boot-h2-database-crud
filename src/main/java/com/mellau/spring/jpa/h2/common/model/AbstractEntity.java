package com.mellau.spring.jpa.h2.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public abstract class AbstractEntity<T extends Serializable> implements Serializable, Entity<T> {
/* import javax.persistence.*; @MappedSuperclass
    @Serial
    private static final long serialVersionUID = -739283600922042164L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    protected T id;

    @Column(name = "created_at")
    protected Instant createdAt;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }*/
}
