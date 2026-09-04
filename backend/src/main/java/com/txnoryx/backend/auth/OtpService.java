package com.txnoryx.backend.auth;
import com.txnoryx.backend.model.User;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
@Service
public class OtpService {
    public static final int TTL_MINUTES = 10;
    public static final int MAX_ATTEMPTS = 3;
    private final SecureRandom random = new SecureRandom();
    public String issue(User user){
        String otp = String.format("%06d", random.nextInt(1000000));
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(TTL_MINUTES));
        user.setOtpAttempts(0);
        return otp;
    }
    public void check(User user, String otp){
        if(user.getOtpCode()==null) throw new IllegalArgumentException("No OTP pending - request a new code");
        if(user.getOtpExpiry()!=null && LocalDateTime.now().isAfter(user.getOtpExpiry())) throw new IllegalArgumentException("OTP expired - request a new code");
        if(user.getOtpAttempts()>=MAX_ATTEMPTS) throw new IllegalArgumentException("Too many attempts - request a new code");
        if(!user.getOtpCode().equals(otp.trim())){
            user.setOtpAttempts(user.getOtpAttempts()+1);
            throw new IllegalArgumentException("Invalid OTP");
        }
    }
    public void consume(User user){ user.setOtpCode(null); user.setOtpExpiry(null); user.setOtpAttempts(0); }
}
