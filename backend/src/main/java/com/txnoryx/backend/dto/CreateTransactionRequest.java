package com.txnoryx.backend.dto;

import java.math.BigDecimal;

public class CreateTransactionRequest {


    private String transactionId;


    private BigDecimal amount;


    private String currency;


    private String paymentMethod;


    private String merchant;


    private String failureReason;


    private String deviceId;


    private String location;


    private Long userId;

    private String status;


    public String getTransactionId() {
        return transactionId;
    }


    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getCurrency() {
        return currency;
    }


    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }


    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public String getMerchant() {
        return merchant;
    }


    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }


    public String getFailureReason() {
        return failureReason;
    }


    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }


    public String getDeviceId() {
        return deviceId;
    }


    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}