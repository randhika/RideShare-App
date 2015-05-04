package com.rideshare.rideshare.utils;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Security {

    private static String KEY = "ZX5jGmF_v64njboi3jfZn";

    public static String encrypt(String text){
        try{
            DESKeySpec keySpec = new DESKeySpec(KEY.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] cleartext = text.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(Base64.encode(cipher.doFinal(cleartext), Base64.DEFAULT));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String encryptedText){
        try{
            DESKeySpec keySpec = new DESKeySpec(KEY.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encrypedPwdBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(encrypedPwdBytes));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
