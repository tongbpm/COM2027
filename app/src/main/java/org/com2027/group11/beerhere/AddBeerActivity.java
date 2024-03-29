package org.com2027.group11.beerhere;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.utilities.StorageHandler;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class AddBeerActivity extends AppCompatActivity {

    private static final  int REQUEST_IMAGE_CAPTURE = 1;
    private static final int LOAD_IMAGE = 2;
    private static final int REQUEST_CAMERA =  10;
    private static final int REQUEST_GALLERY = 20;
    private static final String[] COUNTRIES = {"Austria",  "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "United Kingdom"};
    private static final String TAG = "Add_Beer_Activity" ;

    private static final SynchronisationManager syncManager = SynchronisationManager.getInstance();

    private Spinner mCountry = null;
    private ArrayList<String> mCountryList;
    private ImageButton mImageButton;
    private AlertDialog mDialog;
    private Bitmap mBitmap = null;
    private SimpleDraweeView mDraweeView;
    private String mCurrentPhotoPath;
    private EditText mNameEditText;
    private Button mSubmitButton;
    private  String mImageId;
    private Uri mphotoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generateImageId();

        setContentView(R.layout.activity_add_beer);


        mCountry = findViewById(R.id.country_spinner);
        populateCountryLists();
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCountryList);
        mCountry.setAdapter(countryAdapter);
        //Make default selection the same as the device default location

        mImageButton = findViewById(R.id.image_button);
        mDraweeView = findViewById(R.id.image_thumbnail);


        mNameEditText = findViewById(R.id.beer_name_edit_text);
        mDialog = buildDialog();

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
            }
        });

        mSubmitButton = findViewById(R.id.beer_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCountry.getSelectedItem().toString().length() > 0 && mNameEditText.getText().toString().length() > 0){
                    //If both compulsory fields have been filled out
                    String countryName = mCountry.getSelectedItem().toString();
                    countryName = countryName.replace(' ', '_');
                    Beer beer = new Beer(mNameEditText.getText().toString(), mImageId, FirebaseAuth.getInstance().getUid());
                    beer.imageID = mImageId;
                    Toast.makeText(AddBeerActivity.this, "Beer Saved", Toast.LENGTH_LONG).show();
                    syncManager.saveBeerToFirebase(countryName, beer.name, beer);
                    if(mBitmap != null) {
                        StorageHandler.saveBitmapForBeerToFirebase(mImageId, mBitmap);
                    }
                    syncManager.loggedInUser.addSubmission(FirebaseDatabase.getInstance().getReference("countries/"+countryName+"/beers/"+beer.name));
                    finish();
                }else{
                    Toast.makeText(AddBeerActivity.this, R.string.fill_required_fields, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private AlertDialog buildDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddBeerActivity.this );

            builder.setItems(R.array.image_source_array, (dialog, which) -> {
                switch (which){
                    case 0:
                        //Camera
                        String[] cameraPermissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        if(checkPermissions(cameraPermissions)){
                            dispatchTakePictureIntent();
                        }else{
                            requestPermissions( cameraPermissions, REQUEST_CAMERA);
                        }
                        break;
                    case  1:
                        //Gallery
                        String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        if(checkPermissions(galleryPermissions)){
                            dispatchGalleryIntent();
                        }else{
                            requestPermissions(galleryPermissions, REQUEST_GALLERY);
                        }
                        break;
                }
            });
            return  builder.create();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null) {
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch(IOException e){
                e.printStackTrace();
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            }
            if(photoFile!=null){
                 mphotoURI = FileProvider.getUriForFile(this, "org.com2027.group11.beerhere.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mphotoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            galleryAddPic();
        }else if(requestCode == LOAD_IMAGE && resultCode == RESULT_OK) {
            mphotoURI =  data.getData();
        }
        if(mphotoURI != null) {
            mDraweeView.setImageURI(mphotoURI);
            mDraweeView.setVisibility(View.VISIBLE);
            getBitmapFromDrawee();
        }
    }

    private void getBitmapFromDrawee() {

        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                CloseableReference<CloseableBitmap> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableBitmap> closeableReference = imageReference.clone();
                    try {
                        CloseableBitmap closeableBitmap = closeableReference.get();
                        Bitmap bitmap  = closeableBitmap.getUnderlyingBitmap();
                        if(bitmap != null && !bitmap.isRecycled()) {
                            Log.d(TAG, "Got bitmap");
                            mBitmap = bitmap;
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }
            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                // handle failure
            }
        };
        getBitmap(this, mphotoURI, 200, 200, dataSubscriber);

    }


    public void getBitmap(Context context, Uri uri, int width, int height, DataSubscriber dataSubscriber){
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        if(width > 0 && height > 0){
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(request, context);
        dataSource.subscribe(dataSubscriber, UiThreadImmediateExecutorService.getInstance());
    }



    private  void galleryAddPic() {
        Log.d(TAG, "Writing to Gallery");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void populateCountryLists() {
        //Use a set so duplicate countries do not get added
        Set countrySet = new ArraySet<>();

        //Add all countries to the list
        for (String country : COUNTRIES) {
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
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == REQUEST_GALLERY){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                dispatchGalleryIntent();
            }else{
                Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void dispatchGalleryIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, LOAD_IMAGE);
    }

    private File createImageFile() throws IOException{
        File storageDir = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        Log.d(TAG, storageDir.getPath());
        File image = File.createTempFile(mImageId, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean checkPermissions(String... permissions){
        if(permissions != null){
            for(String permission : permissions){
                if(checkSelfPermission(permission)!=PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    private void generateImageId(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageId = FirebaseAuth.getInstance().getUid()+"_"+timeStamp;
    }
}
