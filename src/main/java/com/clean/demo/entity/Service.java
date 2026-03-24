package com.clean.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties
@Entity
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Image featuredImage;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Image> imagesGallery = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Float price;

    @Column(name = "time_minuts")
    private Integer time_minuts;

    @Column(name = "depedens_on_area")
    private @Nullable Integer depedensOnArea;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Image getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(Image featuredImage) {
        this.featuredImage = featuredImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getTime() {
        return time_minuts;
    }

    public void setTime(Integer time_minuts) {
        this.time_minuts = time_minuts;
    }

    public List<Image> getImages() {
        return this.imagesGallery;
    }

    public void setImages(List<Image> imagesGallery) {
        this.imagesGallery = imagesGallery;
    }

    @Nullable
    public Integer getDepedensOnArea() {
        return depedensOnArea;
    }

    public void setDepedensOnArea(@Nullable Integer depedensOnArea) {
        this.depedensOnArea = depedensOnArea;
    }
}
