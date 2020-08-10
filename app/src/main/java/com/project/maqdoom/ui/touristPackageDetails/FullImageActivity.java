package com.project.maqdoom.ui.touristPackageDetails;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.project.maqdoom.R;

public class FullImageActivity extends AppCompatActivity {

    ImageView imgview;
    String imageurl;
    ImageButton navClose;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimage);
        imgview=findViewById(R.id.imageview);
        navClose=findViewById(R.id.navClose);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            imageurl= null;
        } else {
            imageurl= extras.getString("imageurls");
            Glide.with(this).load(imageurl).into(imgview);
        }
        navClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
