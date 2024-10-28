package com.example.colabjdbcmysqlthaycan.Class;

public class Images {
    private int idImage;
    private String link;

    public Images() {
    }

    public Images(int idImage, String link) {
        this.idImage = idImage;
        this.link = link;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Images{" +
                "idImage=" + idImage +
                ", link='" + link + '\'' +
                '}';
    }
}


