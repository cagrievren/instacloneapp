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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GirisActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailText2;
    EditText parolaText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        mAuth = FirebaseAuth.getInstance();
        emailText2 = findViewById(R.id.emailText2);
        parolaText2= findViewById(R.id.parolaText2);

    }

    public void girisYap(View view) {

        String email2 = emailText2.getText().toString();
        String parola2 = parolaText2.getText().toString();

        if (TextUtils.isEmpty(email2)) {
            Toast.makeText(this, "Lütfen e-mail adresinizi giriniz!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(parola2)) {
            Toast.makeText(this, "Lütfen parolanızı giriniz!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (parola2.length()<6) {
            Toast.makeText(this, "Parola en az 6 haneli olmalıdır!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email2,parola2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            startActivity(new Intent(GirisActivity.this, UyeActivity.class));
                            Toast.makeText(GirisActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(GirisActivity.this, "Lütfen bilgilerinizi kontrol edip tekrar deneyin!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
