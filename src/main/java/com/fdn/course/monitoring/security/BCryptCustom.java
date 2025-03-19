package com.fdn.course.monitoring.security;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptCustom {
    private final int logRound;

    public BCryptCustom(int logRound){
        this.logRound = logRound;
    }
    public String hash( String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(logRound));
    }

    public boolean verifyHash(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }
}
