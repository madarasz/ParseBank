package com.madarasz.parsebank.database.stats;

import java.util.List;

/**
 * Created by imadaras on 12/7/15.
 */
public class StatRow {
    private String startDate;
    private int moneyflow;
    private List<Integer> subcategory;
    private int cashflow;
    private int uncategorized;

    public StatRow(String startDate, int moneyflow, List<Integer> subcategory, int cash, int uncategorized) {
        this.startDate = startDate;
        this.moneyflow = moneyflow;
        this.subcategory = subcategory;
        this.cashflow = cash;
        this.uncategorized = uncategorized;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getMoneyflow() {
        return moneyflow;
    }

    public List<Integer> getSubcategory() {
        return subcategory;
    }

    public int getCashflow() {
        return cashflow;
    }

    public int getUncategorized() {
        return uncategorized;
    }
}
