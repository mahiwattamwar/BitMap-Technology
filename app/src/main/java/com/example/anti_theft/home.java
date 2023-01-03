package com.example.anti_theft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.anti_theft.Stolen.getlocation;

public class home extends AppCompatActivity {

    String sms;
    String fsms;
    String username;
    WifiManager wifiManager;

    
    public class BG extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("pre");

        }

        @Override
        protected String doInBackground(String... strings) {

            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"),null,null,null,null);
            cursor.moveToFirst();

             sms = cursor.getString(12);
             System.out.println(sms);
             if(sms.equals(username))
             {

                 Intent intent = new Intent(getApplicationContext(), getlocation.class);
                 startActivity(intent);


             }
//            if(sms.equals("1212"))
//            {
//
//            }



            return sms;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            System.out.println(s);
//            System.out.println(sms);


        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try{
            SharedPreferences prf = getSharedPreferences("My", Context.MODE_PRIVATE);
            username = prf.getString("pin", "");

            wifiManager =(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);

            BG mytask = new BG();
            mytask.execute();
        }catch(Exception e)
        {

        }








        
    }


}