package com.txnoryx.backend.security;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class IdempotencyStore {
    private final ConcurrentHashMap<String,Long> seen=new ConcurrentHashMap<>();
    public boolean isDuplicate(String key){
        if(key==null||key.isBlank()) return false;
        long now=System.currentTimeMillis();
        seen.entrySet().removeIf(e-> now - e.getValue() > 3600_000);
        return seen.putIfAbsent(key, now)!=null;
    }
}
