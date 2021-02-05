package com.example.myapplication.element;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    public String username;
    public String password;
    public int questionIndex;
    public String answer;

    public User(String username, String password, int questionIndex, String answer) {
        this.username = username;
        this.password = password;
        this.questionIndex = questionIndex;
        this.answer = answer;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.questionIndex = -1;
        this.answer = null;
    }

    public static String hashPassword(String password) {
        try {//Try moved from below comment to ensure
            //nullPointerExceptions from passing a null
            //password are caught and prevent program crash
            //If this interpretation of the behavior is unintended,
            byte[] passwordBytes = password.getBytes();
            MessageDigest md;
            //try{
            md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(passwordBytes);
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public boolean isEqual(User user) {
        return username.equals(user.username) && password.equals(user.password);
    }
}
