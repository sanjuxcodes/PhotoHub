package com.example.photoHub;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Photographer")
public class Photographer {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "experience")
    public String exp;

    @ColumnInfo(name = "img", typeAffinity = ColumnInfo.BLOB)
    public byte[] img;


    @Ignore
    private String uid;


    @Ignore
    private String service;

    @Ignore
    private List<byte[]> portfolioImages;

    @Ignore
    private String contact;

    @Ignore
    private String social;

    @Ignore
    private String driveLink;

    public Photographer(int id, String name, String exp, byte[] img) {
        this.id = id;
        this.name = name;
        this.exp = exp;
        this.img = img;
    }

    @Ignore
    public Photographer(String name, String exp, byte[] img) {
        this.name = name;
        this.exp = exp;
        this.img = img;
    }

    @Ignore
    public Photographer(String name, String exp, String service, String contact, String social, String driveLink) {
        this.name = name;
        this.exp = exp;
        this.service = service;
        this.contact = contact;
        this.social = social;
        this.driveLink = driveLink;
    }

    public Photographer() {}


    public int getId() { return id; }

    public String getName() { return name; }

    public String getExp() { return exp; }

    public byte[] getImg() { return img; }

    public String getService() { return service; }

    public void setService(String service) { this.service = service; }

    public List<byte[]> getPortfolioImages() { return portfolioImages; }

    public void setPortfolioImages(List<byte[]> portfolioImages) { this.portfolioImages = portfolioImages; }

    public String getPhone() { return contact; }

    public void setPhone(String contact) { this.contact = contact; }

    public String getSocial() { return social; }

    public void setSocialLink(String socialLink) { this.social = socialLink; }

    public String getDriveLink() { return driveLink; }

    public void setDriveLink(String driveLink) { this.driveLink = driveLink; }


    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }
}
