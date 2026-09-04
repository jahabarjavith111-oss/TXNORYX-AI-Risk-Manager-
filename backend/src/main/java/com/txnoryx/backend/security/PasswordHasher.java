package com.txnoryx.backend.security;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;
public final class PasswordHasher {
    private PasswordHasher(){}
    public static String hash(String password){
        try{
            byte[] salt=new byte[16]; new SecureRandom().nextBytes(salt);
            byte[] dig=MessageDigest.getInstance("SHA-256").digest(concat(salt, password.getBytes(StandardCharsets.UTF_8)));
            for(int i=0;i<9999;i++) dig=MessageDigest.getInstance("SHA-256").digest(dig);
            return "sha256$"+HexFormat.of().formatHex(salt)+"$"+HexFormat.of().formatHex(dig);
        }catch(Exception e){ throw new IllegalStateException("hash failed"); }
    }
    public static boolean verify(String password, String stored){
        try{
            String[] p=stored.split("\\$"); if(p.length!=3) return false;
            byte[] salt=HexFormat.of().parseHex(p[1]);
            byte[] dig=MessageDigest.getInstance("SHA-256").digest(concat(salt, password.getBytes(StandardCharsets.UTF_8)));
            for(int i=0;i<9999;i++) dig=MessageDigest.getInstance("SHA-256").digest(dig);
            return MessageDigest.isEqual(dig, HexFormat.of().parseHex(p[2]));
        }catch(Exception e){ return false; }
    }
    private static byte[] concat(byte[] a, byte[] b){ byte[] o=new byte[a.length+b.length]; System.arraycopy(a,0,o,0,a.length); System.arraycopy(b,0,o,a.length,b.length); return o; }
}
