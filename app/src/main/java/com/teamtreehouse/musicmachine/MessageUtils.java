package com.teamtreehouse.musicmachine;


import android.os.Message;

/** Stores convenience methods */
public class MessageUtils {

    public static String getMsgInfo(Message msg){
        String data;
        data=String.format("Message{" +
                "\narg1=%d" +
                "\narg2=%d" , msg.arg1, msg.arg2);

        return data;
    }

}
