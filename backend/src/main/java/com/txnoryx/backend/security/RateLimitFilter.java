package com.txnoryx.backend.security;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Component @Order(1)
public class RateLimitFilter implements Filter {
    private final ConcurrentHashMap<String,int[]> buckets=new ConcurrentHashMap<>();
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r=(HttpServletRequest)req; String path=r.getRequestURI();
        boolean sensitive=path.contains("/simulate")||path.contains("/analyze")||path.contains("/agent/execute");
        if(!sensitive){ chain.doFilter(req,res); return; }
        String key=r.getRemoteAddr()+":"+path;
        int[] b=buckets.computeIfAbsent(key,k->new int[]{0, (int)(System.currentTimeMillis()/60000)});
        int cur=(int)(System.currentTimeMillis()/60000);
        synchronized(b){ if(b[1]!=cur){ b[0]=0; b[1]=cur; } b[0]++; if(b[0]>10){ ((HttpServletResponse)res).setStatus(429); res.getWriter().write("{\"error\":\"Rate limit exceeded - 10/min\"}"); res.setContentType("application/json"); return; } }
        chain.doFilter(req,res);
    }
}
