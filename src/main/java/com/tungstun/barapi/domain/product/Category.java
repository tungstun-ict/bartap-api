package com.tungstun.barapi.domain.product;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIdentityReference(alwaysAsId = true)
@Entity
@Table(name = "category")
@SQLDelete(sql = "UPDATE category SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Category {
    @Column(name = "deleted")
    private final boolean deleted = Boolean.FALSE;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "bar_id")
    private Long barId;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    public Category() { }
    public Category(Long barId, String name, ProductType productType) {
        this.barId = barId;
        this.name = name;
        this.productType = productType;
    }

    public Long getId() {
        return id;
    }

    public Long getBarId() {
        return barId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public ProductType getProductType() { return productType; }

    public void setProductType(ProductType productType) { this.productType = productType; }
}
