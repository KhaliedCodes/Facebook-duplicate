package com.example.facebookprofile_testproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private int PICK_PP = -1;
    private int PICK_CP = -1;
    private int POST = -1;

    private void setImage(int requestCode, int resultCode, @Nullable Intent data, View view) {
        final Uri imageUri = data.getData();
        final InputStream imageStream;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage;
            if (requestCode == PICK_PP) {
                selectedImage = BitmapFactory.decodeStream(imageStream);
                ((ImageView) view).setImageBitmap(selectedImage);
            } else {
                selectedImage = BitmapFactory.decodeStream(imageStream);
                Drawable d = new BitmapDrawable(getResources(), selectedImage);
                ((ImageView) view).setBackground(d);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.change_pp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.v("HEre", "WE are here");
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        PICK_PP = 1;
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_PP);
                    } else {
                        PICK_PP = 1;
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_PP);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.change_cp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        PICK_CP = 1;

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_CP);
                    } else {
                        PICK_CP = 1;

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_CP);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.timeline_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POST = 1;
                Intent intent = new Intent(MainActivity.this, PostActivity.class);

                startActivityForResult(intent, POST);


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_PP) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_PP);
            }
        } else if (requestCode == PICK_CP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_CP);
            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PP) {

            if (resultCode == RESULT_OK && data != null) {
                View view = findViewById(R.id.profile_pic);
                setImage(requestCode, resultCode, data, view);
                view = findViewById(R.id.post_pp);
                setImage(requestCode, resultCode, data, view);
                view = findViewById(R.id.timeline_img);
                setImage(requestCode, resultCode, data, view);
                PICK_PP = -1;
            }

        } else if (requestCode == PICK_CP) {
            if (resultCode == RESULT_OK && data != null) {
                View view = findViewById(R.id.cover_pic);
                setImage(requestCode, resultCode, data, view);
                PICK_CP = -1;
            }
        } else if (requestCode == POST && resultCode == RESULT_OK) {
            Log.v("Here", "DId you even come here?");
            String postText = data.getStringExtra("text");
            ((TextView) findViewById(R.id.post_text)).setText(postText);
            if (data.getParcelableExtra("img") != null) {
                ((ImageView) findViewById(R.id.post_img)).setImageBitmap((Bitmap) data.getParcelableExtra("img"));
            }
            POST = -1;
        } else {
            Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
        }
    }
}