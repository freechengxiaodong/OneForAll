package com.tools.vo;

import java.io.Serializable;

public class BalanceVo implements Serializable {

    private static final long serialVersionUID = 187253175793377700L;
    private int id;
    private int diamond;
    private int ticket;

    public BalanceVo() {
    }

    public BalanceVo(int id, int diamond, int ticket) {
        this(id, diamond, ticket, "OK");
    }

    public BalanceVo(int id, int diamond, int ticket, String message) {
        this.id = id;
        this.diamond = diamond;
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "BalanceVo{" +
                "id=" + id +
                ", diamond=" + diamond +
                ", ticket=" + ticket +
                '}';
    }
}