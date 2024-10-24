package com.example.afinal.Constructor;

import java.util.ArrayList;

public class Package {
    private int packageId;
    private String title;
    private double price;
    private String description;
    private String mainImage;
    private ArrayList<String> additionalImages; // To store the 3 other images

    public Package(int packageId, String title, double price, String description, String mainImage, ArrayList<String> additionalImages) {
        this.packageId = packageId;
        this.title = title;
        this.price = price;
        this.description = description;
        this.mainImage = mainImage;
        this.additionalImages = additionalImages;
    }

    // Getters
    public int getPackageId() {
        return packageId;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getMainImage() {
        return mainImage;
    }

    public ArrayList<String> getAdditionalImages() {
        return additionalImages;
    }

    // Setters
    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public void setAdditionalImages(ArrayList<String> additionalImages) {
        this.additionalImages = additionalImages;
    }
}
