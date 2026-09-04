package com.txnoryx.backend.dto;
import jakarta.validation.constraints.*;
public class VerifyOtpRequest {
    @NotBlank @Email private String email;
    @NotBlank @Pattern(regexp="^[0-9]{6}$", message="otp must be 6 digits") private String otp;
    public String getEmail(){return email;} public void setEmail(String v){this.email=v==null?null:v.trim().toLowerCase();}
    public String getOtp(){return otp;} public void setOtp(String v){this.otp=v==null?null:v.trim();}
}
