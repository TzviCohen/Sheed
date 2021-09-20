package com.postpc.Sheed;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
//import android.support.annotation.NonNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.postpc.Sheed.database.SheedUsersDB;
import com.postpc.Sheed.profile.ProfileFragment;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.postpc.Sheed.Utils.IMAGE_URL_INTENT;
import static com.postpc.Sheed.Utils.USER_INTENT_SERIALIZABLE_KEY;

public class ActivityAddPhoto extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    Context context;
    String check;

    SheedUser currentUser;
    SheedUsersDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);


        Intent intentOpenedMe = getIntent();
        check = intentOpenedMe.getStringExtra("check");


        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        //mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        //mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        context = this;

        mButtonChooseImage.setOnClickListener(v -> openFileChooser());
        mButtonUpload.setOnClickListener(v -> {
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(ActivityAddPhoto.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }

        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            System.out.println(mImageUri);
            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if (mImageUri != null) {

            String file_name = System.currentTimeMillis() + "." + getFileExtension(mImageUri);
            StorageReference fileReference = mStorageRef.child(file_name);
            System.out.println("fileReference " + fileReference);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {

                        mStorageRef.child(file_name).getDownloadUrl().addOnSuccessListener(uri -> {
                            Toast.makeText(ActivityAddPhoto.this, "uploaded image successfully", Toast.LENGTH_LONG).show();
                            System.out.println("!!!!!!!!!!3");
                            if (check.equals("sign")){
                                System.out.println("!!!!!!!!!!1");
                                Intent signActivityIntent = new Intent(context, ActivityRegister.class);
                                signActivityIntent.putExtra(IMAGE_URL_INTENT, uri.toString());
                                startActivity(signActivityIntent);
                            } else {
                                Log.d("bla", "bla!3");
                                System.out.println("!!!!!!!!!!2");
                                db = SheedApp.getDB();
                                db.currentSheedUser.imageUrl = uri.toString();
                                db.updateUser(db.currentSheedUser);
//
////                                Intent ProfileFragment = new Intent(context, ProfileFragment.class);
////                                signActivityIntent.putExtra(IMAGE_URL_INTENT, uri.toString());
////                                startActivity(ProfileFragment);
//
//
                                setResult(RESULT_OK, getIntent());
                                finish();
                                return;


//
//                                Bundle bundle = new Bundle();
//                                bundle.putString(IMAGE_URL_INTENT, uri.toString());
//// set Fragmentclass Arguments
//                                ProfileFragment fragObj = ProfileFragment.newInstance();
//                                fragObj.setArguments(bundle);
//
//
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragObj).commit();


                            }
//                            Intent signActivityIntent = new Intent(context, ActivitySignIn.class);
//                            signActivityIntent.putExtra(IMAGE_URL_INTENT, uri.toString());
//                            startActivity(signActivityIntent);
                        }).addOnFailureListener(e -> Toast.makeText(ActivityAddPhoto.this, e.getMessage(), Toast.LENGTH_SHORT).show());

                    })
                    .addOnFailureListener(e -> Toast.makeText(ActivityAddPhoto.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}