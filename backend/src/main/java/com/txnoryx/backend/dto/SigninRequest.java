package com.txnoryx.backend.dto;
import jakarta.validation.constraints.*;
public class SigninRequest {
    @NotBlank @Email private String email;
    @NotBlank private String password;
    public String getEmail(){return email;} public void setEmail(String v){this.email=v==null?null:v.trim().toLowerCase();}
    public String getPassword(){return password;} public void setPassword(String v){this.password=v;}
}
