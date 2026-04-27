package com.clean.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_line")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Service service;

    private Integer quantity = 1;

    private Double area;

    @ManyToOne
    @JsonIgnoreProperties("items")
    private Order order;

    public Double getLineTotal() {
        if (service == null || service.getPrice() == null || quantity == null) {
            return 0.0;
        }

        double linePrice = service.getPrice();

        if (service.getDepedensOnArea() != null && area != null) {
            double additionalArea = Math.max(0.0, area - service.getDepedensOnArea());
            if (additionalArea > 0 && service.getPriceForAdditionalMeter() != null) {
                linePrice += additionalArea * service.getPriceForAdditionalMeter();
            }
        }

        return linePrice * quantity;
    }
}
