package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    ImageView secimView;
    EditText yorumText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    Uri secilenResim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        secimView = findViewById(R.id.secimView);
        yorumText = findViewById(R.id.yorumText);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    public void yukle(View view) {

        //Resimleri her seferinde eşsiz isimlerle kayıt etmek için UUID nesnesinin üretilmesi
        UUID uuid = UUID.randomUUID();
        final String resimAd = "Resimler/" + uuid+ ".jpg";

        StorageReference storageReference = mStorageRef.child(resimAd);
        storageReference.putFile(secilenResim).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                StorageReference newReference = FirebaseStorage.getInstance().getReference(resimAd);
                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String indirmeBaglanti = uri.toString();

                        //Daha sonra göstermek üzere mevcut kullanıcıyı uyeEmail değişkenine aktarma işlemi
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uyeEmail = user.getEmail();

                        String uyeYorum = yorumText.getText().toString();

                        UUID uuid1 = UUID.randomUUID();
                        String uuidString = uuid1.toString();

                        //Firebase kayıt işlemleri
                        myRef.child("Gönderiler").child(uuidString).child("Üye Email").setValue(uyeEmail);
                        myRef.child("Gönderiler").child(uuidString).child("Yorum").setValue(uyeYorum);
                        myRef.child("Gönderiler").child(uuidString).child("İndirme Bağlantısı").setValue(indirmeBaglanti);

                        Toast.makeText(UploadActivity.this, "Gönderi paylaşıldı!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), UyeActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Resim seçme işlemi
    public void gonderiSec(View view) {

        //İzin kontrolü
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //İzin verilmemiş ise izin isteme işlemi
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            //İzin verilmiş ise galeriye gitme
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }

    }

    //Sorulan izinin sonucunun değerlendirilmesi
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {

            //Eğer sorulan izine olumlu yanıt verilirse galeriye gitme
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Galeriye gittikten sonra yapılan seçimin değerlendirilmesi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            //Eğer bir resim seçildiyse ve boş değilse..
            secilenResim = data.getData();

            try {
                //imageView'e seçilen resimin gösterilmesi
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), secilenResim);
                secimView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
