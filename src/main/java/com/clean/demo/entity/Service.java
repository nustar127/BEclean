package com.clean.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties
@Entity
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private @Nullable Image featuredImage;

    @ManyToMany
    private List<Image> imagesGallery = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "price")
    private Float price;

    @Column(name = "time_minuts")
    private Integer time_minuts;

    @Column(name = "depedens_on_area")
    private @Nullable Integer depedensOnArea;

    @JsonIgnoreProperties("service")
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ServiceRequirement> requirments = new ArrayList<>();

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

    public List<ServiceRequirement> getRequirments() {
        return requirments;
    }

    public void setRequirments(List<ServiceRequirement> requirments) {
        this.requirments = requirments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
