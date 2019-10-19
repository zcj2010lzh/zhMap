package com.example.zhmap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    WallpaperManager wallpaperManager;
    Bitmap bitmap;
    int i;
    String xw;
    Button b1;
    int month;
    int day;
    Random random=new Random();
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM:dd");
    Date date=new Date();
    SharedPreferences preferences;
    private ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
 wallpaperManager = WallpaperManager.getInstance(this);
        String x=simpleDateFormat.format(date);
         month=Integer.parseInt(x.substring(0,2));
       day=Integer.parseInt(x.substring(3,5));

         b1=findViewById(R.id.b1);
             preferences = PreferenceManager.getDefaultSharedPreferences(this);
         i=random.nextInt(18);
       if (preferences.getInt("month",-1)>0){
           if(preferences.getInt("month",-1)<month)
               setWallpaperManager();
           else {
               if (preferences.getInt("day",33)<day)
                   setWallpaperManager();
           }
       }
    ;
     b1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             setWallpaperManager();
         }
     });
     Button b2=findViewById(R.id.b2);
     b2.setOnClickListener(new View.OnClickListener() {
         public void onClick(View view) {
             showProgressDialog();

          new Thread(new Runnable() {
              @Override
              public void run() {
                  try{


                      wallpaperManager.setBitmap(bitmap);
                  }catch (IOException e){
                      e.printStackTrace();
                  }
              }
          }).start();
          closeProgressDialog();
     }
     });
    }
    private  void setWallpaperManager(){
        if (preferences.getInt("lasti",-1)>-1){
            while (preferences.getInt("lasti",-1)==i){
                i=random.nextInt(18);
            }
        }
        Resources res = getResources();
        bitmap= BitmapFactory.decodeResource(res, getResources().getIdentifier("wallpaper"+i , "drawable", "com.example.zhmap"));

                showProgressDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Main2Activity.this).edit();
                            editor.putInt("lasti",i);
                            editor.putInt("month",month);
                            editor.putInt("day",day);
                            editor.apply();
                            wallpaperManager.setBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();

                            }
                        });
                    }
                }).start();
            }


    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(Main2Activity.this);
            progressDialog.setMessage("load....");
            progressDialog.setCancelable(false);

        }
        progressDialog.show();
    }
    private  void closeProgressDialog(){
        Toast.makeText(Main2Activity.this, "设置成功", Toast.LENGTH_SHORT).show();
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}
