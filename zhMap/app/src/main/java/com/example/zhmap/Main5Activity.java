package com.example.zhmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class Main5Activity extends AppCompatActivity {
    public  static final  int CHOOSE_PHOTO=2;
    ImageView picture;
    String xx;
    String imagePath;
    WallpaperManager wallpaperManager;
        public   Bitmap bitmap;
     private  Button backtomain5;
     private  Button back;
     ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        Button chooseFromAlbum=findViewById(R.id.choose_from_album);
       picture=findViewById(R.id.picture);
       backtomain5=findViewById(R.id.shezhi);
       back=findViewById(R.id.back_main2);
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent();
               intent.putExtra("n",imagePath);
               setResult(RESULT_OK,intent);
               finish();
           }
       });
       backtomain5.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     wallpaperManager = WallpaperManager.getInstance(Main5Activity.this);
                     try {
                         wallpaperManager.setBitmap(bitmap);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }

                 }
             }).start();
               Toast.makeText(Main5Activity.this, "设置成功", Toast.LENGTH_SHORT).show();
           }
       });
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Main5Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Main5Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    openAlbum();
                else {
                    Toast.makeText(this, "you denied thr permisson", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
        }
    }
   public void openAlbum() {
       Intent intentn = new Intent("android.intent.action.GET_CONTENT");
       intentn.setType("image/*");
       startActivityForResult(intentn, CHOOSE_PHOTO);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch ((requestCode)){
            case  CHOOSE_PHOTO:
               handImageOnkitKat(data);
        }
    }
     private  void handImageOnkitKat(Intent data){
   imagePath=null;
         Uri uri=data.getData();
         if (DocumentsContract.isDocumentUri(this,uri)){
             String docId=DocumentsContract.getDocumentId(uri);
             if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                 String id=docId.split(":")[1];
                 String seliction= MediaStore.Images.Media._ID+"="+id;
               imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,seliction);
             }else  if("com.android. providers.downloads.document".equals(uri.getAuthority())){
                 Uri cotentUri= ContentUris.withAppendedId(uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                 imagePath=getImagePath(cotentUri,null);
             }}
             else  if ("content".equalsIgnoreCase(uri.getScheme())){
                 imagePath=getImagePath(uri,null);
             }
             else  if ("file".equalsIgnoreCase(uri.getScheme())){
                 imagePath=uri.getPath();
             }
          ;
        displayImage(imagePath);
     }
     private  String getImagePath(Uri uri,String selection){
        String path=null;
         Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
         if (cursor!=null){
             if (cursor.moveToFirst()){
                 path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
             }
             cursor.close();
         }
     return path;
    }
    private  void displayImage(String imagePath) {
        if (imagePath != null) {
            xx = imagePath;
            bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
}
}