package com.example.witsmarketplace.fave_cart;

public class FavItem {

    private final String name, price, image, count, desc,email,productID;

    //  constructor fetching all data required on an itemBox
    public FavItem(String name, String price, String image, String count,String desc,String email,String productID) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.count = count;
        this.desc  = desc;
        this.email = email;
        this.productID = productID;
    }

    //  getters for all required data
    public String getName(){
        return name;
    }
    public String getPrice(){
        return price;
    }
    public String getImage(){
        return image;
    }
    public String getCount(){
        return count;
    }
    public String getDesc() {return desc;}
    public String getEmail(){return email;}
    public String getProductID(){return productID;}
}


