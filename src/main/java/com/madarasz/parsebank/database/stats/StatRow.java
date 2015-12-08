package com.madarasz.parsebank.database.stats;

import java.util.List;

/**
 * Created by imadaras on 12/7/15.
 */
public class StatRow {
    private String startDate;
    private int moneyflow;
    private List<SubCategory> subCategories;
    private int cashflow;
    private int uncategorized;

    public StatRow(String startDate, int moneyflow, List<SubCategory> subCategories, int cash, int uncategorized) {
        this.startDate = startDate;
        this.moneyflow = moneyflow;
        this.subCategories = subCategories;
        this.cashflow = cash;
        this.uncategorized = uncategorized;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getMoneyflow() {
        return moneyflow;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public int getCashflow() {
        return cashflow;
    }

    public int getUncategorized() {
        return uncategorized;
    }
}
