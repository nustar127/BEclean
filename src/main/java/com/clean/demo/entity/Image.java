package com.clean.demo.entity;

import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "alt")
    private @Nullable String alt;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Nullable
    public String getAlt() {
        return alt;
    }

    public void setAlt(@Nullable String alt) {
        this.alt = alt;
    }

    @JsonGetter("url")
    public String getFullUrl() {
        return "http://localhost:8080/uploads/" + this.filename;
    }
}
