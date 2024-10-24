package com.example.afinal.Constructor;

import java.util.ArrayList;
import java.io.Serializable;

public class Package implements Serializable {
    private int packageId;
    private String title;
    private double price;
    private String description;
    private String mainImage;
    private String image1;
    private String image2;
    private String image3;
    private ArrayList<String> inclusions; // To store inclusions
    private ArrayList<String> itineraries; // To store itineraries

    // Constructor for Package with inclusions and itineraries
    public Package(int packageId, String title, double price, String description,
                   String mainImage, String image1,String image2,String image3,
                   ArrayList<String> inclusions, ArrayList<String> itineraries) {
        this.packageId = packageId;
        this.title = title;
        this.price = price;
        this.description = description;
        this.mainImage = mainImage;
        // Initialize lists, or set to empty lists if null
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.inclusions = inclusions != null ? inclusions : new ArrayList<>();
        this.itineraries = itineraries != null ? itineraries : new ArrayList<>();
    }

    // Getters


    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

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


    public ArrayList<String> getInclusions() {
        return inclusions;
    }

    public ArrayList<String> getItineraries() {
        return itineraries;
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



    public void setInclusions(ArrayList<String> inclusions) {
        this.inclusions = inclusions != null ? inclusions : new ArrayList<>();
    }

    public void setItineraries(ArrayList<String> itineraries) {
        this.itineraries = itineraries != null ? itineraries : new ArrayList<>();
    }
}
