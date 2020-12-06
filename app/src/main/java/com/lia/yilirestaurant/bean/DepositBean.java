package com.lia.yilirestaurant.bean;

public class DepositBean {
    InfoBean info;
    Deposit user;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public Deposit getUser() {
        return user;
    }

    public void setUser(Deposit user) {
        this.user = user;
    }

    public static class Deposit{
        String username;
        String balance;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }
}
