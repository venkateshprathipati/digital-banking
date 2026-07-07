package com.novalabs.digitalbanking.config.properties;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "bank")
@Validated
public class BankProperties {

//    private Account account = new Account();
//    private Transfer transfer = new Transfer();
//    private Notification notification = new Notification();
//    private Security security = new Security();
//    private Interest interest = new Interest();
//
//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }
//
//    public Transfer getTransfer() {
//        return transfer;
//    }
//
//    public void setTransfer(Transfer transfer) {
//        this.transfer = transfer;
//    }
//
//    public Notification getNotification() {
//        return notification;
//    }
//
//    public void setNotification(Notification notification) {
//        this.notification = notification;
//    }
//
//    public Security getSecurity() {
//        return security;
//    }
//
//    public void setSecurity(Security security) {
//        this.security = security;
//    }
//
//    public Interest getInterest() {
//        return interest;
//    }
//
//    public void setInterest(Interest interest) {
//        this.interest = interest;
//    }
//
//    public static class Account {
//
//        @DecimalMin("0.0")
//        private BigDecimal minimumBalance;
//
//        @DecimalMin("0.0")
//        private BigDecimal maximumBalance;
//
//        @NotBlank
//        private String defaultCurrency;
//
//        public BigDecimal getMinimumBalance() {
//            return minimumBalance;
//        }
//
//        public void setMinimumBalance(BigDecimal minimumBalance) {
//            this.minimumBalance = minimumBalance;
//        }
//
//        public BigDecimal getMaximumBalance() {
//            return maximumBalance;
//        }
//
//        public void setMaximumBalance(BigDecimal maximumBalance) {
//            this.maximumBalance = maximumBalance;
//        }
//
//        public String getDefaultCurrency() {
//            return defaultCurrency;
//        }
//
//        public void setDefaultCurrency(String defaultCurrency) {
//            this.defaultCurrency = defaultCurrency;
//        }
//    }
//
//    public static class Transfer {
//
//        @DecimalMin("0.0")
//        private BigDecimal dailyLimit;
//
//        @DecimalMin("0.0")
//        private BigDecimal maximumPerTransaction;
//
//        public BigDecimal getDailyLimit() {
//            return dailyLimit;
//        }
//
//        public void setDailyLimit(BigDecimal dailyLimit) {
//            this.dailyLimit = dailyLimit;
//        }
//
//        public BigDecimal getMaximumPerTransaction() {
//            return maximumPerTransaction;
//        }
//
//        public void setMaximumPerTransaction(BigDecimal maximumPerTransaction) {
//            this.maximumPerTransaction = maximumPerTransaction;
//        }
//    }
//
//    public static class Notification {
//
//        @Email
//        private String supportEmail;
//
//        private boolean smsEnabled;
//
//        private boolean emailEnabled;
//
//        public String getSupportEmail() {
//            return supportEmail;
//        }
//
//        public void setSupportEmail(String supportEmail) {
//            this.supportEmail = supportEmail;
//        }
//
//        public boolean isSmsEnabled() {
//            return smsEnabled;
//        }
//
//        public void setSmsEnabled(boolean smsEnabled) {
//            this.smsEnabled = smsEnabled;
//        }
//
//        public boolean isEmailEnabled() {
//            return emailEnabled;
//        }
//
//        public void setEmailEnabled(boolean emailEnabled) {
//            this.emailEnabled = emailEnabled;
//        }
//    }
//
//    public static class Security {
//
//        @Min(4)
//        private int otpLength;
//
//        @Min(1)
//        private int otpExpiryMinutes;
//
//        public int getOtpLength() {
//            return otpLength;
//        }
//
//        public void setOtpLength(int otpLength) {
//            this.otpLength = otpLength;
//        }
//
//        public int getOtpExpiryMinutes() {
//            return otpExpiryMinutes;
//        }
//
//        public void setOtpExpiryMinutes(int otpExpiryMinutes) {
//            this.otpExpiryMinutes = otpExpiryMinutes;
//        }
//    }
//
//    public static class Interest {
//
//        @DecimalMin("0.0")
//        private BigDecimal savingsRate;
//
//        @DecimalMin("0.0")
//        private BigDecimal fixedDepositRate;
//
//        public BigDecimal getSavingsRate() {
//            return savingsRate;
//        }
//
//        public void setSavingsRate(BigDecimal savingsRate) {
//            this.savingsRate = savingsRate;
//        }
//
//        public BigDecimal getFixedDepositRate() {
//            return fixedDepositRate;
//        }
//
//        public void setFixedDepositRate(BigDecimal fixedDepositRate) {
//            this.fixedDepositRate = fixedDepositRate;
//        }
//    }
}
