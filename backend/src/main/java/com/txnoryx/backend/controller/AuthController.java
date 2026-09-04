package com.txnoryx.backend.controller;
import com.txnoryx.backend.auth.AuthTokenStore;
import com.txnoryx.backend.auth.OtpMailService;
import com.txnoryx.backend.auth.OtpService;
import com.txnoryx.backend.dto.SigninRequest;
import com.txnoryx.backend.dto.SignupRequest;
import com.txnoryx.backend.dto.VerifyOtpRequest;
import com.txnoryx.backend.model.User;
import com.txnoryx.backend.repository.UserRepository;
import com.txnoryx.backend.security.PasswordHasher;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import java.time.LocalDateTime;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final OtpService otpService;
    private final OtpMailService mail;
    private final AuthTokenStore tokens;
    public AuthController(UserRepository users, OtpService otpService, OtpMailService mail, AuthTokenStore tokens){
        this.users=users; this.otpService=otpService; this.mail=mail; this.tokens=tokens;
    }
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> signup(@Valid @RequestBody SignupRequest req){
        if(!req.getPassword().equals(req.getConfirmPassword())) throw new IllegalArgumentException("Passwords do not match");
        if(users.findByEmailIgnoreCase(req.getEmail()).isPresent()) throw new IllegalArgumentException("Email already registered - please sign in");
        User u=new User();
        u.setName(HtmlUtils.htmlEscape(req.getName()));
        u.setEmail(req.getEmail());
        u.setOrganization(HtmlUtils.htmlEscape(req.getOrganization()));
        u.setPhone(null);
        u.setRole("MERCHANT");
        u.setPasswordHash(PasswordHasher.hash(req.getPassword()));
        u.setStatus("pending_verification");
        u.setCreatedAt(LocalDateTime.now());
        String otp=otpService.issue(u);
        users.save(u);
        mail.send(u.getEmail(), u.getName(), otp);
        return Map.of("success",true,"status","pending_verification","email",u.getEmail(),"otpSent",true,"message","Account created - verify the OTP sent to your email");
    }
    @PostMapping("/verify-otp")
    public Map<String,Object> verify(@Valid @RequestBody VerifyOtpRequest req){
        User u=users.findByEmailIgnoreCase(req.getEmail()).orElseThrow(()->new IllegalArgumentException("Account not found"));
        try{ otpService.check(u, req.getOtp()); }
        catch(IllegalArgumentException e){ users.save(u); throw e; }
        otpService.consume(u);
        u.setStatus("active");
        u.setLastLogin(LocalDateTime.now());
        users.save(u);
        String token=tokens.issue(u.getId());
        return Map.of("success",true,"token",token,"user",publicUser(u),"message","Account verified - welcome to TXNORYX");
    }
    @PostMapping("/signin")
    public Map<String,Object> signin(@Valid @RequestBody SigninRequest req){
        User u=users.findByEmailIgnoreCase(req.getEmail()).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));
        if(u.getPasswordHash()==null||!PasswordHasher.verify(req.getPassword(), u.getPasswordHash())) throw new IllegalArgumentException("Invalid email or password");
        if(!"active".equals(u.getStatus())){
            String otp=otpService.issue(u); users.save(u); mail.send(u.getEmail(), u.getName(), otp);
            throw new IllegalArgumentException("Email not verified - a fresh OTP was sent");
        }
        u.setLastLogin(LocalDateTime.now()); users.save(u);
        String token=tokens.issue(u.getId());
        return Map.of("success",true,"token",token,"user",publicUser(u),"message","Welcome back");
    }
    @PostMapping("/resend-otp")
    public Map<String,Object> resend(@RequestBody Map<String,String> body){
        String email=body.get("email")==null?"":body.get("email").trim().toLowerCase();
        User u=users.findByEmailIgnoreCase(email).orElseThrow(()->new IllegalArgumentException("Account not found"));
        if("active".equals(u.getStatus())) throw new IllegalArgumentException("Account already verified - please sign in");
        String otp=otpService.issue(u); users.save(u); mail.send(u.getEmail(), u.getName(), otp);
        return Map.of("success",true,"otpSent",true,"message","New OTP sent to your email");
    }
    @GetMapping("/me")
    public Map<String,Object> me(@RequestHeader(value="Authorization", required=false) String auth){
        Long id=tokens.resolve(auth);
        if(id==null) throw new IllegalArgumentException("Unauthorized - please sign in");
        User u=users.findById(id).orElseThrow(()->new IllegalArgumentException("Unauthorized - please sign in"));
        return Map.of("user",publicUser(u));
    }
    @PostMapping("/logout")
    public Map<String,Object> logout(@RequestHeader(value="Authorization", required=false) String auth){
        tokens.revoke(auth);
        return Map.of("success",true,"message","Signed out");
    }
    private Map<String,Object> publicUser(User u){
        return Map.of("id",u.getId(),"name",u.getName(),"email",u.getEmail(),"organization",u.getOrganization()==null?"":u.getOrganization(),"role",u.getRole()==null?"MERCHANT":u.getRole(),"status",u.getStatus());
    }
}
