package org.com2027.group11.beerhere.utilities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageHandler {

    private static final StorageReference IMAGES_REF = FirebaseStorage.getInstance().getReference().child("images");


    public static void setImageFromFirebase(String imageID, SimpleDraweeView view) {
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

}
