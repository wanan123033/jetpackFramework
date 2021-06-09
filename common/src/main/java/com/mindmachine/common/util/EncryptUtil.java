package com.mindmachine.common.util;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {
    private static EncryptUtil util;
    /**
     * 解密是否成功的校验码
     */
    private String CHECK_CODE = "fp2018";

    // 以下为生成方法
    private final String BASESTRING = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 解密是否成功的校验码
     */
    private String AES_KEY = CHECK_CODE;
    private final String randomS;
    private final SecretKeySpec secretKey;

    private EncryptUtil(){
        // 加密
        randomS = randomString(BASESTRING, 10);
        AES_KEY = CHECK_CODE + randomS;
        secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
    }

    public static synchronized EncryptUtil getInstance(){
        if (util == null){
            util = new EncryptUtil();
        }
        return util;
    }

    private String randomString(String baseString, int length) {
        final StringBuilder sb = new StringBuilder();

        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 加密数据
     *
     * @param paramsData
     * @return
     */
    public String setEncryptData(String paramsData) {
        try {
            LogUtils.e("加密前:"+paramsData);
            LogUtils.e("加密key:"+AES_KEY);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encoderStr = cipher.doFinal(paramsData.getBytes("UTF-8"));
            String dd = new String(Hex.encodeHex(encoderStr));
            LogUtils.e("加密后:"+dd);
            return dd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 签名
     * @param signData
     * @return
     */
    public String getSignData(String signData) {
        LogUtils.e("sign前:"+signData);
        String sign = new String(Hex.encodeHex(DigestUtils.sha(signData)));
        String startString = sign.substring(0, 8);
        String endString = sign.substring(8, sign.length());
        String dd = startString + randomS + endString;
        LogUtils.e("sign后:"+dd);
        return dd;
    }
    public String decodeHttpData(String data, String sign) {
        String b = "";
        if (!TextUtils.isEmpty(sign) && sign.length() > 17) {
            // 分离签名
//            String a = sign.substring(0, 8);
            b = sign.substring(8, 18);
//            String c = sign.substring(18);
        }
        // 加密key
        String key = CHECK_CODE + b;
        System.out.println("key: " + key);
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        return setDecodeData(data, secretKey);
    }
    private static String setDecodeData(String encryptData, SecretKey secretKey) {

        try {
            Cipher cipher = Cipher.getInstance("AES");
//            Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decode = Hex.decodeHex(encryptData.toCharArray());
            byte[] decodeStr = cipher.doFinal(decode);
//            int index = paddingIndex(decodeStr);
//            byte[] noPaddingBytes = new byte[index];
//            System.arraycopy(decodeStr, 0, noPaddingBytes, 0, index);
            return new String(decodeStr, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
