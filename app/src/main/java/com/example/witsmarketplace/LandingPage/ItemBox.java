package com.example.witsmarketplace.LandingPage;
import java.util.ArrayList;

public class ItemBox implements java.io.Serializable{
    private final String name , price, image, description, Stock;
    private final int productID;
    private final ArrayList<String> imageUrls;

//  constructor fetching all data required on an itemBox
    public ItemBox(int productID,String name, String price, String image, String description,ArrayList<String> imageUrls, String stock) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.imageUrls = imageUrls;
        this.Stock = stock;
    }

//  getters for all required data
    public String getStock(){return Stock;}
    public int getProductID(){return productID;}
    public String getName(){
        return name;
    }
    public String getPrice(){
        return price;
    }
    public String getImage(){
        return image;
    }
    public String getDescription(){
        return description;
    }
    public ArrayList<String> getImageUrls(){
        return imageUrls;
    }
}
