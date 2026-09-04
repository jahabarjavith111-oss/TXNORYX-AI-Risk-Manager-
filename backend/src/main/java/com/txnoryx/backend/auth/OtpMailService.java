package com.txnoryx.backend.auth;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Service
public class OtpMailService {
    private static final Logger log = LoggerFactory.getLogger(OtpMailService.class);
    private final JavaMailSender sender;
    private final boolean enabled;
    private final String from;
    public OtpMailService(JavaMailSender sender,
                          @Value("${txnoryx.mail.enabled:false}") boolean enabled,
                          @Value("${txnoryx.mail.from:}") String from){
        this.sender=sender; this.enabled=enabled; this.from=from;
    }
    public String buildHtml(String name, String otp){
        String safe = name==null?"there":name.replace("<","").replace(">","");
        return "<!DOCTYPE html><html><body style=\"margin:0;padding:0;background:#f4f6fb;font-family:Arial,Helvetica,sans-serif;\">"
        + "<div style=\"max-width:560px;margin:32px auto;background:#ffffff;border-radius:12px;overflow:hidden;border:1px solid #e5e9f2;\">"
        + "<div style=\"padding:28px 32px 0;text-align:center;\"><div style=\"font-size:22px;font-weight:800;letter-spacing:3px;color:#0f172a;\">TXNORYX</div>"
        + "<div style=\"margin-top:14px;font-size:18px;font-weight:700;color:#0f172a;\">Verify your email address</div></div>"
        + "<div style=\"padding:16px 32px;color:#334155;font-size:14px;line-height:1.6;\">"
        + "<p>Hi "+safe+",</p><p>You are almost there. Use the verification code below to complete your TXNORYX account setup.</p>"
        + "<div style=\"text-align:center;margin:20px 0;\"><span style=\"display:inline-block;font-size:28px;font-weight:800;letter-spacing:8px;color:#0f172a;background:#f1f5f9;border:1px dashed #94a3b8;border-radius:10px;padding:12px 22px 12px 30px;\">"+otp+"</span></div>"
        + "<p style=\"text-align:center;color:#64748b;\">This verification code is valid for 10 minutes.</p>"
        + "<p>If you did not request this code, you can safely ignore this email.</p>"
        + "<p>For your security, never share this code with anyone.</p></div>"
        + "<div style=\"border-top:1px solid #e5e9f2;padding:16px 32px;color:#64748b;font-size:12px;\"><strong>TXNORYX</strong><br/>Autonomous Payment Risk &amp; Recovery Intelligence<br/>&copy; 2026 TXNORYX. All rights reserved.</div>"
        + "</div></body></html>";
    }
    public void send(String to, String name, String otp){
        if(!enabled || from==null || from.isBlank()) throw new RuntimeException("Email service not configured - set GMAIL_USER, GMAIL_APP_PASSWORD and MAIL_ENABLED=true");
        String html = buildHtml(name, otp);
        try{
            MimeMessage msg = sender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
            h.setFrom(from, "TXNORYX");
            h.setTo(to);
            h.setSubject("Verify your TXNORYX account");
            h.setText(html, true);
            sender.send(msg);
            log.info("OTP email sent to {}", to);
        }catch(Exception e){
            log.error("SMTP send failed to {} err={}", to, e.getMessage());
            throw new RuntimeException("Could not send OTP email - please try again");
        }
    }
    public String preview(String name, String otp){ return buildHtml(name, otp); }
}
