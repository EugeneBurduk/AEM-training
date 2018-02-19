package com.epam.aem.training.beans;


public class CustomPage {
    private String title;
    private String description;



    private String image;
    private String url;

    public CustomPage() {
    }

    public CustomPage(String title) {
        this.title = title;
    }

    public CustomPage(String title, String description, String image, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.image = image;
    }
    public CustomPage(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return title;
    }
}
