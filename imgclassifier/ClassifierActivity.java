package com.wuggs.imgclassifier;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ClassifierActivity extends AppCompatActivity {
    private Button mTakePhotoButton;
    private Button mUploadPhotoButton;
    private Button mAboutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier);

        mTakePhotoButton = (Button) findViewById(R.id.take_photo);
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), TakePhotoActivity.class);
                startActivity(intent);
            }
        });

        mAboutButton = (Button) findViewById(R.id.about);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
