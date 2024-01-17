package com.example.cbu_caching.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@ToString
public class Currency{
    @Id
    private Integer id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false, unique = true)
    private String currency;
    @Column(nullable = false)
    private String nameEnglish;
    @Column(nullable = false)
    private String nameUzbek;
    @Column(nullable = false)
    private String nameUzbekKrill;
    @Column(nullable = false)
    private String nameRussian;
    @Column(nullable = false)
    private String nominal;
    @Column(nullable = false)
    private BigDecimal rate;
    @Column(nullable = false)
    private BigDecimal difference;
    @Column(nullable = false)
    private String date;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
