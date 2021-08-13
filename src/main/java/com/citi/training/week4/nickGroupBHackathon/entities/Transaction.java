package com.citi.training.week4.nickGroupBHackathon.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="transactions")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="investor_id")
    private Integer investorId;
    @Column(name="type")
    private String type;
    @Column(name="price")
    private Double price;
    @Column(name="amount")
    private Integer amount;
    @Column(name="time_stamp")
    private LocalDateTime timeStamp;
    @Column(name="symbol")
    private String symbol;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Integer investorId) {
        this.investorId = investorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
