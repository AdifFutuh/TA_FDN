package com.fdn.course.monitoring.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class AESGeneratedKey {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());

        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES", "BC");

            keygen.init(256);

            SecretKey aesKey = keygen.generateKey();

            System.out.println("AES Key: " + bytesToHex(aesKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String bytesToHex(byte[] bytes){
        StringBuilder result = new StringBuilder();
        for (byte b : bytes){
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}
