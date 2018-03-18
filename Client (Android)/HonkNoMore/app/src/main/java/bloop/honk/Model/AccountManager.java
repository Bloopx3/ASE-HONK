package bloop.honk.Model;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64.Encoder;


import bloop.honk.View.AdminActivity;

public class AccountManager {
    public AccountManager() {
    }

    //login function
    public void login(final String username, final String password, final Activity activity, final SharedPreferences sharedPreferences) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if (response.equalsIgnoreCase("failure")) {
                            Toast.makeText(activity, "Invalid username/ password", Toast.LENGTH_LONG).show();
                        } else {
                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.USERNAME_SHARED_PREF, username);
                            editor.putString(Config.ROLE_SHARED_PREF, response);
                            //Saving values to editor
                            editor.apply();

                            if (response.equals("user")) {
                                Toast.makeText(activity, "Successfully login", Toast.LENGTH_SHORT).show();
                                activity.finish();
                            } else {
                                activity.finish();
                                Intent intent = new Intent(activity, AdminActivity.class);
                                activity.startActivity(intent);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                //Adding parameters to request
//                params.put(Config.TAG_USERNAME, username);
//                params.put(Config.TAG_PASSWORD, password);
//                return params; //return params to string request
//            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> params = getUserInfo();
                if (params != null && params.size() > 0) {
                    return encodeParameters(params, getParamsEncoding());
                }
                return null;
            }

            public Map<String, String> getUserInfo() {
                Map<String, String> params = new HashMap<>();
                params.put(Config.TAG_USERNAME, username);
                params.put(Config.TAG_PASSWORD, password);
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);//this is the login request.
        requestQueue.add(stringRequest);
    }

    public byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    public static String Encrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        //BASE64Encoder encoder = new BASE64Encoder();
        return Base64.encodeToString(results,Base64.NO_WRAP); // it returns the result as a String
    }


    public String hashPassword(final char[] password, final byte[] salt) {
        final int interation = 1;
        final int keyLength = 256;
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(password, salt, interation, keyLength);
            SecretKey key = skf.generateSecret(spec);

            return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(SharedPreferences sharedPreferences) {
        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //clear values of editor
        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        editor.putString(Config.USERNAME_SHARED_PREF, "");
        editor.putString(Config.ROLE_SHARED_PREF, "");
        //Saving values to editor
        editor.apply();
    }

    public void register(final User user, String confirmPassword, final Activity activity, final SharedPreferences sharedPreferences) throws Exception {
        if (user.getPassword().equals(confirmPassword)) {
            //user.setPassword(hashPassword(user.getPassword().toCharArray(), user.getUsername().getBytes()));
            user.setPassword(Encrypt(user.getPassword(),"putanginamo"));
            //perform register [POST]
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Adding values to editor
                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                                editor.putString(Config.USERNAME_SHARED_PREF, user.getUsername());
                                editor.putString(Config.ROLE_SHARED_PREF, "user");
                                //Saving values to editor
                                editor.apply();

                                Toast.makeText(activity, "Registered & logged in", Toast.LENGTH_SHORT).show();
                                activity.finish();

                            } else if (response.equals("unsuccessful")){
                                Toast.makeText(activity, "Register fail.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Username Taken", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activity, "Unexpected error occured, registration not successful.", Toast.LENGTH_LONG).show();
                        }
                    }) {

                protected Map<String, String> getUserInfo() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(Config.TAG_USERNAME, user.getUsername());
                    params.put(Config.TAG_PASSWORD, user.getPassword());
                    return params; //return params to string request
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = getUserInfo();
                    if (params != null && params.size() > 0) {
                        return encodeParameters(params, getParamsEncoding());
                    }
                    return null;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(activity, "Password does not match", Toast.LENGTH_SHORT).show();
        }
    }
}
