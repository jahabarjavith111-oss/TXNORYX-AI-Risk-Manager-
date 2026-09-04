package com.txnoryx.backend.dto;
import jakarta.validation.constraints.*;
public class SignupRequest {
    @NotBlank @Size(max=50) private String name;
    @NotBlank @Email @Size(max=191) private String email;
    @NotBlank @Size(max=100) private String organization;
    @NotBlank @Size(min=8, max=72, message="password must be 8-72 chars") @Pattern(regexp="^(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,72}$", message="password needs a number and a special char") private String password;
    @NotBlank private String confirmPassword;
    public String getName(){return name;} public void setName(String v){this.name=v==null?null:v.trim();}
    public String getEmail(){return email;} public void setEmail(String v){this.email=v==null?null:v.trim().toLowerCase();}
    public String getOrganization(){return organization;} public void setOrganization(String v){this.organization=v==null?null:v.trim();}
    public String getPassword(){return password;} public void setPassword(String v){this.password=v;}
    public String getConfirmPassword(){return confirmPassword;} public void setConfirmPassword(String v){this.confirmPassword=v;}
}
