package com.example.fooddelivery.chefFoodPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.example.fooddelivery.R;

public class chef_postDish extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_CROP_IMAGE = 3;
    private static final int REQUEST_PERMISSION = 4;

    ImageButton imageButton;
    Button post_dish;
    Spinner Dishes;
    TextInputLayout desc, qty, pri;
    String descrption, quantity, price, dishes;
    Uri imageuri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, dataa;
    FirebaseAuth Fauth;
    StorageReference ref;
    String ChefId, RandomUID, State, City, Area;
    private Uri photoURI;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_post_dish);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Dishes = findViewById(R.id.dishes);
        desc = findViewById(R.id.description);
        qty = findViewById(R.id.Quantity);
        pri = findViewById(R.id.price);
        post_dish = findViewById(R.id.post);
        Fauth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("FoodDetails");

        try {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
            dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Chef cheff = snapshot.getValue(Chef.class);
                    State = cheff.getState();
                    City = cheff.getCity();
                    Area = cheff.getArea();
                    imageButton = findViewById(R.id.image_upload);

                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(chef_postDish.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(chef_postDish.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                            } else {
                                pickImage();
                            }
                        }
                    });
                    post_dish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dishes = Dishes.getSelectedItem().toString().trim();
                            descrption = desc.getEditText().getText().toString().trim();
                            quantity = qty.getEditText().getText().toString().trim();
                            price = pri.getEditText().getText().toString().trim();

                            if (isValid()) {
                                uploadImage();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void cropImage(Uri sourceUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(sourceUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 512);
        intent.putExtra("outputY", 512);
        intent.putExtra("return-data", true);
        File croppedImageFile = null;
        try {
            croppedImageFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (croppedImageFile != null) {
            photoURI = FileProvider.getUriForFile(this, "com.example.foodapp.fileprovider", croppedImageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CROP_IMAGE);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                imageuri = data.getData();
                cropImage(imageuri);
            } else if (requestCode == REQUEST_CROP_IMAGE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = extras.getParcelable("data");
                imageButton.setImageBitmap(imageBitmap);
                photoURI = data.getData();
                Toast.makeText(this, "Cropped Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage() {
        if (photoURI != null) {
            final ProgressDialog progressDialog = new ProgressDialog(chef_postDish.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            RandomUID = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUID);
            ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FoodDetails info = new FoodDetails(dishes, quantity, price, descrption, String.valueOf(uri), RandomUID, ChefId);
                            firebaseDatabase.getInstance().getReference("FoodDetails").child(State).child(City).child(Area).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(chef_postDish.this, "Dish Posted Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(chef_postDish.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }

    private boolean isValid() {
        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValidDescription = false, isValidPrice = false, isValidQuantity = false, isValid = false;
        if (TextUtils.isEmpty(descrption)) {
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");
        } else {
            desc.setError(null);
            isValidDescription = true;
        }
        if (TextUtils.isEmpty(quantity)) {
            qty.setErrorEnabled(true);
            qty.setError("Enter number of Plates or Items");
        } else {
            isValidQuantity = true;
        }
        if (TextUtils.isEmpty(price)) {
            pri.setErrorEnabled(true);
            pri.setError("Please Mention Price");
        } else {
            isValidPrice = true;
        }
        isValid = (isValidDescription && isValidQuantity && isValidPrice);
        return isValid;
    }
}
