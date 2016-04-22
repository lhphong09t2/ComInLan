package com.onballgroup.cominlan.model.Base;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class BaseModel implements IBaseModel {

    public BaseModel() {
        super();
    }

    public BaseModel(JSONObject jsonObject)
    {
        try {
            _dataJsonObject = new JSONObject(jsonObject.getString("Data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        create(jsonObject);
    }

    private JSONObject _dataJsonObject;

    @Override
    public JSONObject getDataJsonObject()
    {
        return  _dataJsonObject;
    }

    public static String calculateChecksum(String content)
    {
        if (content == null)
        {
            return "";
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = messageDigest.digest(content.getBytes("UTF-8"));
            return convertByteArrayToHexString(hashedBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        return stringBuffer.toString();
    }
}
