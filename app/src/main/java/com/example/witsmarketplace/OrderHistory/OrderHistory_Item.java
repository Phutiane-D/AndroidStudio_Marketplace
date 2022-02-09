package com.example.witsmarketplace.OrderHistory;

import java.util.Dictionary;

public class OrderHistory_Item implements java.io.Serializable {
    private final String total, date, street, surburb, city, country, items, order_no;
    private String[] price;
    private String[] name;
    //  constructor fetching all data required on an itemBox
    public OrderHistory_Item(String total, String date, String street,String surburb,String city,String country, String items, String[] name, String[] price, String order_no) {
        this.total = total;
        this.date = date;
        this.street = street;
        this.surburb = surburb;
        this.city = city;
        this.country = country;
        this.items = items;
        this.name = name;
        this.price = price;
        this.order_no = order_no;
    }

    //  getters for all required data
    public String getTotal(){
        return total;
    }
    public String getDate(){
        return date;
    }
    public String getStreet(){
        return street;
    }
    public String getSurburb(){
        return surburb;
    }
    public String getCity(){
        return city;
    }
    public String getCountry(){
        return country;
    }
    public String getItems(){ return items; }
    public String getOrder_no(){ return order_no; }
    public String[] getName(){ return name; }
    public String[] getPrice(){ return price; }
}
