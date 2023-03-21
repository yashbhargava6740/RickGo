package com.example.homepage;

public class TokenCl {
    private String tok;
    private boolean flag;
    public TokenCl() {
        tok = "null";
        flag = true;
    }
    public String getTok() { return this.tok; }
    public boolean getFlag() { return this.flag; }
    //public String getId() { return this.id; }
    public void setFlag() { this.flag = true; }
    public void unsetFlag() { this.flag = false; }
}
