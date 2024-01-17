package com.example.version_1.entity.impls;

import com.example.version_1.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cbu")
@ToString
public class CbuCcyEntity extends AbstractEntity {
    @Id
    private Integer id;
    @Column(nullable = false, unique = true)
    private String code;
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
    private String difference;
    @Column(nullable = false)
    private String date;

    @Builder(builderClassName = "child")
    public CbuCcyEntity(String currency, BigDecimal rate, Integer id, String code, String nameEnglish, String nameUzbek, String nameUzbekKrill, String nameRussian, String nominal, String difference, String date) {
        super(currency, rate);
        this.id = id;
        this.code = code;
        this.nameEnglish = nameEnglish;
        this.nameUzbek = nameUzbek;
        this.nameUzbekKrill = nameUzbekKrill;
        this.nameRussian = nameRussian;
        this.nominal = nominal;
        this.difference = difference;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CbuCcyEntity currency = (CbuCcyEntity) o;
        return Objects.equals(id, currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
