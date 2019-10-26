package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailText;
    EditText parolaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailText);
        parolaText = findViewById(R.id.parolaText);

    }

    public void uyeOl(View view) {
        
        String email = emailText.getText().toString();
        String parola = parolaText.getText().toString();
        
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Lütfen e-mail adresinizi giriniz!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(parola)) {
            Toast.makeText(this, "Lütfen parolanızı giriniz!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (parola.length()<6) {
            Toast.makeText(this, "Parola en az 6 haneli olmalıdır!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mAuth.createUserWithEmailAndPassword(email,parola)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Üye oluşturuldu!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Bu e-mail adresiyle daha önce kayıt olunmuş!", Toast.LENGTH_SHORT).show();
                    }
                });
                
    }

    public void uyeGiris(View view) {
        Intent intent = new Intent(getApplicationContext(),GirisActivity.class);
        startActivity(intent);
    }

}
