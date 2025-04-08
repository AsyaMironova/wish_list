package com.example.wishlist.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wishlist.HomeActivity;
import com.example.wishlist.R;
import com.example.wishlist.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.editTextEmail);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText passwordEditText = findViewById(R.id.editTextPassword);
        Button registerButton = findViewById(R.id.buttonRegister);
        CheckBox agreementCheckBox = findViewById(R.id.checkboxAgreement);
        @SuppressLint("WrongViewCast") MaterialButton googleButton = findViewById(R.id.buttonGoogleSignIn);

        registerButton.setOnClickListener(v -> {
            if (!agreementCheckBox.isChecked()) {
                Toast.makeText(this, "Подтвердите согласие с пользовательским соглашением", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите почту и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                User newUser = new User(firebaseUser.getUid(), "", firebaseUser.getEmail(), "");
                                FirebaseFirestore.getInstance().collection("users")
                                        .document(firebaseUser.getUid())
                                        .set(newUser);
                            }

                            if (auth.getCurrentUser() != null && !auth.getCurrentUser().isEmailVerified()) {
                                auth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(verifyTask -> {
                                            if (verifyTask.isSuccessful()) {
                                                Toast.makeText(this, "Письмо с подтверждением отправлено", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(this, LoginActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(this, "Ошибка отправки письма", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        TextView agreementLink = findViewById(R.id.textViewAgreement);
        agreementLink.setOnClickListener(v ->
                startActivity(new Intent(this, AgreementActivity.class))
        );
    }
}
