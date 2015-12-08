package com.madarasz.parsebank.database;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by imadaras on 12/6/15.
 */
@NodeEntity
public class Entry {
    @GraphId
    private Long id;
    private int serial;
    private String code;
    private String accountNum;
    private String recipientName;
    private String message;
    private int money;
    private Date date;
    private String formattedDate; // faking formating
    @RelatedTo(type = "BELONGS") private @Fetch Category category;
    @RelatedTo(type = "FORCED") private @Fetch Category forcedCategory;

    public Entry() {
    }

    public Entry(int serial, String code, String accountNum, String recipientName, String message, int money, Date date) {
        this.serial = serial;
        this.code = code;
        this.accountNum = accountNum;
        this.recipientName = recipientName;
        this.message = message;
        this.money = money;
        this.date = date;
    }

    public int getSerial() {
        return serial;
    }

    public String getCode() {
        return code;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getMessage() {
        return message;
    }

    public int getMoney() {
        return money;
    }

    public Date getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        if (forcedCategory == null) {
            return category;
        } else {
            return forcedCategory;
        }
    }

    public Category getForcedCategory() {
        return forcedCategory;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setForcedCategory(Category forcedCategory) {
        this.forcedCategory = forcedCategory;
    }

    // totally fake
    public String getFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id='" + id + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", money=" + money +
                ", date=" + date +
                ", code=" + code +
                ", message=" + message +
                '}';
    }
}
