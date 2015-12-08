package com.madarasz.parsebank.database.stats;

/**
 * Created by imadaras on 12/8/15.
 */
public class SubCategory {
    private int money;
    private String categoryName;

    public SubCategory(int money, String categoryName) {
        this.money = money;
        this.categoryName = categoryName;
    }

    public int getMoney() {
        return money;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
