package com.txnoryx.backend.auth;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class AuthTokenStore {
    private static final long TTL_MS = 12*3600_000L;
    private record Entry(Long userId, long expires){ }
    private final ConcurrentHashMap<String,Entry> tokens=new ConcurrentHashMap<>();
    public String issue(Long userId){
        String t=UUID.randomUUID().toString();
        tokens.put(t,new Entry(userId,System.currentTimeMillis()+TTL_MS));
        return t;
    }
    public Long resolve(String header){
        if(header==null||!header.startsWith("Bearer ")) return null;
        Entry e=tokens.get(header.substring(7).trim());
        if(e==null||System.currentTimeMillis()>e.expires()){ if(e!=null) tokens.remove(header.substring(7).trim()); return null; }
        return e.userId();
    }
    public void revoke(String header){
        if(header!=null&&header.startsWith("Bearer ")) tokens.remove(header.substring(7).trim());
    }
}
