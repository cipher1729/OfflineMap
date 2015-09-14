package com.example.chandanj.offlinemap;

import android.content.Entity;
import android.net.http.AndroidHttpClient;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by cipher1729 on 8/26/2015.
 */
public class HttpManager {

   public static String getData(String uri) {
       try {
           URL url = new URL(uri);
           HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
           StringBuilder sb = new StringBuilder();
           BufferedReader reader;
           reader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
           String line = reader.readLine();
          // line= reader.readLine();
           while(line!= null)
           {
               sb.append(line+ "\n");
               line=reader.readLine();
           }
           reader.close();
           return sb.toString();
       } catch (MalformedURLException e) {
           e.printStackTrace();
           return null;
       } catch (IOException e) {
           e.printStackTrace();
            return null;
       }
       finally {

       }

   }


    public static String getData(String uri, String username, String password) {
        try {
            URL url = new URL(uri);
            byte[] loginBytes = (username+":"+password).getBytes();
            StringBuilder loginBuider = new StringBuilder().append("Basic ")
                    .append(Base64.encodeToString(loginBytes,Base64.DEFAULT));

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestProperty("Authorization",loginBuider.toString());
            StringBuilder sb = new StringBuilder();
            BufferedReader reader;
            reader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = reader.readLine();
            // line= reader.readLine();
            while(line!= null)
            {
                sb.append(line+ "\n");
                line=reader.readLine();
            }
            reader.close();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {

        }

    }
}
