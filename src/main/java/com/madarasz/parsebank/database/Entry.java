package com.madarasz.parsebank.database;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern="yyyy.MM.dd")
    private Date date;

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

    @Override
    public String toString() {
        return "Entry{" +
                "recipientName='" + recipientName + '\'' +
                ", money=" + money +
                ", date=" + date +
                '}';
    }
}
