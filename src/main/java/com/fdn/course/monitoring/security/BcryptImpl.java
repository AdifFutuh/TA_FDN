package com.fdn.course.monitoring.security;

public class BcryptImpl {
    private static final BCryptCustom bcrypt = new BCryptCustom(11);

    public static String hash(String password){
        return bcrypt.hash(password);
    }

    public static boolean verifyHash(String password, String hash){
        return bcrypt.verifyHash(password, hash);
    }
}
