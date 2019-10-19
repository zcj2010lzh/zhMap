package com.example.zhmap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Main4Activity extends AppCompatActivity {

ImageView view1;
String x;
String imgpath;
    Bitmap bitmap;
    EditText text;
    private static int code=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        final Button b=findViewById(R.id.click);
        view1=findViewById(R.id.iv);
        Resources res = getResources();

       text=findViewById(R.id.edit_text);
          Button choose_from_album=findViewById(R.id.choose_from_album);
       bitmap= BitmapFactory.decodeResource(res, getResources().getIdentifier("wallpaper"+3 , "drawable", "com.example.zhmap"));;
        Main5Activity z=new Main5Activity();
    b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               craete(bitmap);
            }
        });
    choose_from_album.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(Main4Activity.this,Main5Activity.class);
            startActivityForResult(intent,code);
        }
    });
    }
    private  void  craete(Bitmap bitmap){
        x=text.getText().toString();
        view1.setImageBitmap( QRCodeUtil.createQRCodeWithLogo(x,bitmap));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      switch (requestCode){
          case 1:
              if (resultCode==RESULT_OK){
               imgpath=data.getStringExtra("n");
                  bitmap = BitmapFactory.decodeFile(imgpath);
                  craete(bitmap);
              }
              break;
              default:break;
      }
    }
}
