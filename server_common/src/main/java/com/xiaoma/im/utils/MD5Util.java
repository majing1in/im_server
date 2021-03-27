package com.xiaoma.im.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.stream.IntStream;


@Slf4j
public class MD5Util {

    /***
     * MD5加码 生成32位md5码
     */
    public static String getMD5String(String inStr){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        IntStream.range(0, md5Bytes.length).map(i -> (md5Bytes[i]) & 0xff).forEachOrdered(val -> {
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        });
        return hexValue.toString();

    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr){
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }

    /**
     * MD5解密
     */
    public static String decrypt(String inStr){
        return convertMD5(convertMD5(inStr));
    }

    public static void main(String[] args) {
        String s = new String("A20180101,B20180101,C20180101,D20180101,E20180101,F20180101,G20180101,F20180101;2-1");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + getMD5String(s));
        System.out.println("加密的：" + convertMD5(s));
        System.out.println("解密的：" + decrypt(s));
    }

}  