package com.hackmech;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Properties;

public class SMSSender {

    public static void sendSms(String toPhoneNumber, String messageText) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));

            String sid = props.getProperty("twilio.sid");
            String token = props.getProperty("twilio.token");
            String from = props.getProperty("twilio.from");

            Twilio.init(sid, token);

            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),         // To number
                    new PhoneNumber(from),        // From number (Twilio)
                    messageText                             // Message
            ).create();

            System.out.println("✅ SMS sent! SID: " + message.getSid());

        } catch (InaccessibleObjectException iae) {
            //to avoid unwanted exception from dependency using blank sop
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send SMS: " + e.getMessage());
        }
    }
}
