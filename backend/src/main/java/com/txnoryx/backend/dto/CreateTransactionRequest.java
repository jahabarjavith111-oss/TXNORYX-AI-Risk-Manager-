package com.txnoryx.backend.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public class CreateTransactionRequest {
    @NotBlank @Pattern(regexp="^TXN-[A-Z0-9\\-]{4,32}$", message="transactionId must match TXN-XXXX") private String transactionId;
    @NotNull @DecimalMin(value="1.00", message="amount >= 1") @DecimalMax(value="10000000", message="amount too large") private BigDecimal amount;
    @Size(max=10) private String currency;
    @NotBlank @Pattern(regexp="^(UPI|CARD|NET_BANKING|WALLET)$", message="invalid paymentMethod") private String paymentMethod;
    @NotBlank @Size(max=80) private String merchant;
    @Size(max=200) private String failureReason;
    @Size(max=40) private String deviceId;
    @Size(max=80) private String location;
    private Long userId;
    @Pattern(regexp="^(SUCCESS|FAILED|TIMEOUT|DECLINED|SUSPICIOUS|PENDING|RECOVERED)?$", message="invalid status") private String status;
    public String getTransactionId(){return transactionId;} public void setTransactionId(String v){this.transactionId=v==null?null:v.trim().toUpperCase();}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){this.amount=v;}
    public String getCurrency(){return currency;} public void setCurrency(String v){this.currency=v;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){this.paymentMethod=v;}
    public String getMerchant(){return merchant;} public void setMerchant(String v){this.merchant=v==null?null:v.trim();}
    public String getFailureReason(){return failureReason;} public void setFailureReason(String v){this.failureReason=v==null?null:v.trim();}
    public String getDeviceId(){return deviceId;} public void setDeviceId(String v){this.deviceId=v;}
    public String getLocation(){return location;} public void setLocation(String v){this.location=v;}
    public Long getUserId(){return userId;} public void setUserId(Long v){this.userId=v;}
    public String getStatus(){return status;} public void setStatus(String v){this.status=v==null?null:v.trim().toUpperCase();}
}
