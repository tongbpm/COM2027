package org.com2027.group11.beerhere;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddBeerActivity extends AppCompatActivity {

    static final  int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_CAMERA =  10;


    private Spinner mCountry = null;
    private ArrayList<String> mCountryList;
    private ImageButton mImageButton;
    private AlertDialog mDialog;
    private Bitmap mBitmap = null;
    private ImageView mImageView;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        mCountry = findViewById(R.id.country_spinner);
        populateCountryLists();
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCountryList);
        mCountry.setAdapter(countryAdapter);
        //Make default selection the same as the device default location
        mCountry.setSelection(countryAdapter.getPosition(getResources().getConfiguration().locale.getDisplayCountry()));

        mImageButton = findViewById(R.id.image_button);

        mImageView = findViewById(R.id.image_thumbnail);

        mDialog = buildDialog();

        mImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mDialog.show();
            }
        });
    }

    private AlertDialog buildDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddBeerActivity.this );

            builder.setItems(R.array.image_source_array, (dialog, which) -> {
                switch (which){
                    case 0:
                        //Camera
                        final List<String> permissionList = new ArrayList<String>();
                        if(checkSelfPermission(Manifest.permission.CAMERA) ==PackageManager.PERMISSION_GRANTED ){
                            disptachTakePictureIntent();
                        }
                        break;
                    case  1:
                        //Gallery
                        Log.d("Add_Beer_Activity", "Gallery");
                        break;
                }
            });
            return  builder.create();
    }

    private void disptachTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * This is called after the picture has been taken
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    private void populateCountryLists() {
        //Use a set so duplicate countries do not get added
        Set countrySet = new ArraySet<>();
        Locale[] locales = Locale.getAvailableLocales();

        //Add all countries to the list
        String country;
        for (Locale locale : locales) {
            country = locale.getDisplayCountry();
            if (country.length() > 0) {
                countrySet.add(country);
            }
        }
        mCountryList = new ArrayList<>(countrySet);
        Collections.sort(mCountryList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                disptachTakePictureIntent();
            }else{
                Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
