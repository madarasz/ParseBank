package com.madarasz.parsebank.database.stats;

import org.neo4j.unsafe.impl.batchimport.stats.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imadaras on 12/7/15.
 */
public class MonthlyStat {
    private List<String> categoryTitles;
    private List<StatRow> rows;

    public MonthlyStat() {
        this.categoryTitles = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public MonthlyStat(List<String> categoryTitles) {
        this.categoryTitles = categoryTitles;
        this.rows = new ArrayList<>();
    }

    public void addRow(StatRow row) {
        this.rows.add(row);
    }

    public List<String> getCategoryTitles() {
        return categoryTitles;
    }

    public List<StatRow> getRows() {
        return rows;
    }
}
