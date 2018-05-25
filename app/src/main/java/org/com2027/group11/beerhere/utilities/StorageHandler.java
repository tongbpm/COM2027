package org.com2027.group11.beerhere.utilities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.com2027.group11.beerhere.R;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import java.io.ByteArrayOutputStream;


public  class StorageHandler {

    private static final StorageReference IMAGES_REF = FirebaseStorage.getInstance().getReference().child("images");
    private static final String LOG_TAG = "Storage Handler";

    public static void setImageFromFirebase(String imageID, SimpleDraweeView view){
        StorageReference imageRef = IMAGES_REF.child(imageID+".jpg");
        Log.d("Storage Reference: ", imageRef.toString());
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("Download URI", uri.toString());
                view.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void saveBitmapForBeerToFirebase(String imageID, Bitmap bitmap) throws NullPointerException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();

        StorageReference imageReference = IMAGES_REF.child(imageID + ".jpg");


        Log.d(LOG_TAG, "Uploading Image");
        UploadTask task = imageReference.putBytes(data);
        task.addOnFailureListener(e -> {
            e.printStackTrace();
        });
        task.addOnSuccessListener(taskSnapshot -> {
            Log.d(LOG_TAG, "Upload Successful");
        });

    }
}
