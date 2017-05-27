package com.haitr.doge.Object;

import com.haitr.doge.Object.Dish;

/**
 * Created by haitr on 5/25/2017.
 */

public class Vendor extends Dish {
    private int id;
    private String name, address, open_time, close_time;
    private double quality, service, pricing, space;
    private String image;

    public Vendor() {
    }

    public Vendor(String name, String address, double quality, String image) {
        this.name = name;
        this.address = address;
        this.quality = quality;
        this.image = image;
    }

    public Vendor(int id, String name, String address, String open_time, String close_time, double quality, double service, double pricing, double space, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.open_time = open_time;
        this.close_time = close_time;
        this.quality = quality;
        this.service = service;
        this.pricing = pricing;
        this.space = space;
        this.image = image;
    }

    public Vendor(String vendor_name, String price, String image) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public double getService() {
        return service;
    }

    public void setService(double service) {
        this.service = service;
    }

    public double getPricing() {
        return pricing;
    }

    public void setPricing(double pricing) {
        this.pricing = pricing;
    }

    public double getSpace() {
        return space;
    }

    public void setSpace(double space) {
        this.space = space;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
