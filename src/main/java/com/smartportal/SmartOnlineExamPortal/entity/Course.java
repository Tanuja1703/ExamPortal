package com.smartportal.SmartOnlineExamPortal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;       // Course title

    private String category;    // e.g. Programming, Design
    private String duration;    // e.g. 6 Weeks
    private String level;       // Beginner, Intermediate, Advanced

    private double price;       // INR

    private String thumb;       // Thumbnail image URL

    @Column(length = 2000)
    private String description; // Detailed course description

    // --------------------------
    // Constructors
    // --------------------------
    public Course() {}

    public Course(String title, String category, String duration, String level, double price, String thumb, String description) {
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.level = level;
        this.price = price;
        this.thumb = thumb;
        this.description = description;
    }

    // --------------------------
    // Getters & Setters
    // --------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getThumb() { return thumb; }
    public void setThumb(String thumb) { this.thumb = thumb; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // --------------------------
    // toString (Optional but Helpful)
    // --------------------------
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", duration='" + duration + '\'' +
                ", level='" + level + '\'' +
                ", price=" + price +
                ", thumb='" + thumb + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
