package com.fdn.course.monitoring.core;


import com.fdn.course.monitoring.config.SMTPConfig;
import com.fdn.course.monitoring.util.LoggingFile;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Date;
import java.util.Properties;

public class SMTPCore {

    Properties prop;
    private Message message;
    private Session session;
    private String strDestination;
    private StringBuilder stringBuilder = new StringBuilder();
    private MimeBodyPart messageBodyPart;
    private Multipart multipart;


    private Properties getTLSProp(){
        prop = new Properties();
        prop.put("mail.smtp.host", SMTPConfig.getEmailHost());
        prop.put("mail.smtp.port", SMTPConfig.getEmailPortTLS());
        prop.put("mail.smtp.auth", SMTPConfig.getEmailAuth());
        prop.put("mail.smtp.starttls.enable", SMTPConfig.getEmailStartTLSEnable());
        prop.put("mail.smtp.timeout", (Long.parseLong(SMTPConfig.getEmailSMTPTimeout())*1000));
        prop.put("mail.smtp.connectiontimeout", (Long.parseLong(SMTPConfig.getEmailSMTPTimeout())*1000));
        prop.put("mail.smtp.writetimeout", (Long.parseLong(SMTPConfig.getEmailSMTPTimeout())*1000));

        return prop;
    }

    private Properties getSSLProp(){
        prop = new Properties();
        prop.put("mail.smtp.host", SMTPConfig.getEmailHost());
        prop.put("mail.smtp.port", SMTPConfig.getEmailPortSSL());
        prop.put("mail.smtp.auth", SMTPConfig.getEmailAuth());
        prop.put("mail.smtp.socketFactory.port", SMTPConfig.getEmailStartTLSEnable());
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.timeout", SMTPConfig.getEmailHost());
        prop.put("mail.smtp.connectiontimeout", SMTPConfig.getEmailHost());
        prop.put("mail.smtp.writetimeout", (Long.parseLong(SMTPConfig.getEmailSMTPTimeout())*1000));

        return prop;
    }

    public void sendMailWithAttachment(
            String[] strMailTo,
            String strSubject,
            String strContentMessage,
            String strLayer,
            String[] attachFiles
    ){
        Properties execProp;

        if (strLayer.equals("SSL")){
            execProp = getSSLProp();
        }else{
            execProp = getTLSProp();
        }

        stringBuilder.setLength(0);
        for (String s : strMailTo) {
            stringBuilder.append(s).append(",");
        }
        strDestination = stringBuilder.toString();
        strDestination = strDestination.substring(0,strDestination.length()-1);

        session = Session.getInstance(
                execProp,
                new Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(SMTPConfig.getEmailUserName(), SMTPConfig.getEmailPassword());
                    }
                }
            );

        try{
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTPConfig.getEmailUserName()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(strDestination)
            );
            message.setSentDate(new Date());
            message.setSubject(strSubject);

            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(strContentMessage,"text/html");

            multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (attachFiles != null){
                for (String filePath : attachFiles){
                    MimeBodyPart attachPart = new MimeBodyPart();
                    try {
                        attachPart.attachFile(filePath);
                    }catch (Exception e){
                        throw new Exception(e.getMessage());
                    }
                    multipart.addBodyPart(attachPart);
                }
            }
            message.setContent(multipart);

            Transport.send(message);
        }catch (Exception e){
            LoggingFile.logException("SMTPCore","sendMailWithAttachment",e, "y");
            System.out.println(e.getMessage());
        }
    }
}
