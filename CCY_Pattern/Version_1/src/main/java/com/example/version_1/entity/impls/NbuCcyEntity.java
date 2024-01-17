package com.example.version_1.entity.impls;

import com.example.version_1.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "nbu")
@ToString
public class NbuCcyEntity extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    @Column(nullable = false)
    private String date;

    @Builder(builderClassName = "child")
    public NbuCcyEntity(String currency, BigDecimal rate, Integer id, String title, BigDecimal buyPrice, BigDecimal sellPrice, String date) {
        super(currency, rate);
        this.id = id;
        this.title = title;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NbuCcyEntity that = (NbuCcyEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
