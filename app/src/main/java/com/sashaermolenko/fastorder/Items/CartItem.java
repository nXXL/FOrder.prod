package com.sashaermolenko.fastorder.Items;

public class CartItem extends Item {
    CartItem(String title, String imgURL, int id) {
        super(title, imgURL, id);
    }
    CartItem(DishItem di) {
        super(di.getName(), di.getURL(), di.getId());
    }
}
