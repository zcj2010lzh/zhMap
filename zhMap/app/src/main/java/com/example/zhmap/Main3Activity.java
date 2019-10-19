package com.example.zhmap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main3Activity extends AppCompatActivity {
               public  static  final int takePhone=1;
    private ImageView picture;
    private Uri imguri;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        final Button   take=findViewById(R.id.take_photo);
        picture=findViewById(R.id.picture);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputimage=new File(getExternalCacheDir(),"output_img.jpg");
                try{if (outputimage.exists()){
                    outputimage.delete();}
                    outputimage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>=24){
                    imguri= FileProvider.getUriForFile(Main3Activity.this,"com.example.zhmap.fileprovider",outputimage);
                }else {
                    imguri=Uri.fromFile(outputimage);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imguri);
                startActivityForResult(intent,takePhone);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case takePhone:
                if (resultCode==RESULT_OK){
                    try{
                        Bitmap  bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imguri));
                        picture.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
