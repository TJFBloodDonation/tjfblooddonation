package ro.ubb.tjfblooddonation.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
    public static String hash(String passwordToHash){
        String generatedPassword;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
            System.out.println(generatedPassword);
            System.out.println(generatedPassword.length());
            return generatedPassword;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
