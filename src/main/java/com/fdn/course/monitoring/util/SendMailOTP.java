package com.fdn.course.monitoring.util;

import com.fdn.course.monitoring.config.SMTPConfig;
import com.fdn.course.monitoring.core.SMTPCore;

public class SendMailOTP {
    public static void  verifyRegisOTP(
            String subject,
            String nama,
            String email,
            String token
    ){
        if (true){
            try {
                String[] strVerify = new String[3];
                strVerify[0] = nama;
                strVerify[1] = token;
                String  strContent = new ReadTextFileSB("registration-token-email.html").getContentFile();
                strContent = strContent.replace("yyttccvj",strVerify[0]);//Kepentingan
                strContent = strContent.replace("tkeno",strVerify[1]);//Nama Lengkap
                final String content = strContent;
                System.out.println(SMTPConfig.getEmailHost());
                String [] strEmail = {email};
                String [] strImage = null;
                SMTPCore sc = new SMTPCore();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sc.sendMailWithAttachment(strEmail,
                                subject,
                                content,
                                "SSL",strImage);
                    }
                });
                t.start();
                System.out.println("mengirim otp");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}