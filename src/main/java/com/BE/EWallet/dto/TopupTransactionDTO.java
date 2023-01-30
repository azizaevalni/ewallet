package com.BE.EWallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
    public class TopupTransactionDTO{
        private String username;
        private String password;
        private int amount;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

