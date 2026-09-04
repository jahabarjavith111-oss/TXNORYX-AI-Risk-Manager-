package com.txnoryx.backend.security;
public final class PromptSanitizer {
    private PromptSanitizer(){}
    public static String sanitize(String s){
        if(s==null) return "—";
        String t=s.replaceAll("[\\{\\}<>`$]", "").replaceAll("(?i)ignore previous instructions|system prompt|do not obey", "[filtered]");
        t=t.trim(); if(t.length()>200) t=t.substring(0,200)+"…";
        return t;
    }
    public static String sanitizeForPrompt(String s){ return "UNTRUSTED DATA: "+sanitize(s); }
}
