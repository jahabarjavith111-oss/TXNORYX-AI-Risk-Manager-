package com.txnoryx.backend.security;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component @Order(2)
public class SecurityHeadersFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse r=(HttpServletResponse)res;
        r.setHeader("Content-Security-Policy","default-src 'self'; script-src 'self'; object-src 'none'");
        r.setHeader("X-Content-Type-Options","nosniff");
        r.setHeader("X-Frame-Options","DENY");
        r.setHeader("X-XSS-Protection","0");
        chain.doFilter(req,res);
    }
}
