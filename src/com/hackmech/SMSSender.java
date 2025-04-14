package com.hackmech;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.lang.reflect.InaccessibleObjectException;

public class SMSSender {

    public static final String ACCOUNT_SID = "AC7f32425a48ec26e592e3c78cc8f3cc71";
    public static final String AUTH_TOKEN = "9cc67a4ba5f83d612c2f7d2f26cca25d";

    public static void sendSms(String toPhoneNumber, String messageText) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),         // To number
                    new PhoneNumber("+13344012185"),        // From number (Twilio)
                    messageText                             // Message
            ).create();

            System.out.println("✅ SMS sent! SID: " + message.getSid());

        }
        catch (InaccessibleObjectException iae){
            //to avoid unwanted exception from dependency using blank sop
            System.out.println("");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send SMS: " + e.getMessage());
        }
    }
}
