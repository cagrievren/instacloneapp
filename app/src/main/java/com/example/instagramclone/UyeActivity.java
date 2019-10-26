package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class UyeActivity extends AppCompatActivity {

    ListView listView;
    PostClass adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String> uyeEmailFirebase;
    ArrayList<String> uyeResimFirebase;
    ArrayList<String> uyeYorumFirebase;

    //Menü kullanmak için gerekli method
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.gonderi_ekle, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Menüde item seçildiğinde ne olacağını yöneten method
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.gonderi_ekle) {
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye);

        uyeEmailFirebase = new ArrayList<String>();
        uyeResimFirebase = new ArrayList<String >();
        uyeYorumFirebase = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new PostClass(uyeEmailFirebase, uyeResimFirebase, uyeYorumFirebase, this);

        listView = findViewById(R.id.listView);

        listView.setAdapter(adapter);

        getDataFromFirebase();

    }

    public void getDataFromFirebase() {

        DatabaseReference newReference = firebaseDatabase.getReference("Gönderiler");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //Firebaseden çekilen verilerin ArrayListlere aktarılması
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    uyeEmailFirebase.add(hashMap.get("Üye Email"));
                    uyeResimFirebase.add(hashMap.get("İndirme Bağlantısı"));
                    uyeYorumFirebase.add(hashMap.get("Yorum"));
                    //adapter'e değişiklik olduğunun bildirilmesi
                    adapter.notifyDataSetChanged();

                }

                //Verileri ters çevirme işlemi
                Collections.reverse(uyeEmailFirebase);
                Collections.reverse(uyeResimFirebase);
                Collections.reverse(uyeYorumFirebase);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
