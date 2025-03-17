package com.example.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextPhone, editTextEmail, editTextPassword, editTextConfirmPassword;
    private ImageButton buttonShowPassword1, buttonShowPassword2;
    private boolean passwordVisible1 = false, passwordVisible2 = false;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword1);
        editTextConfirmPassword = findViewById(R.id.editTextPassword2);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonShowPassword1 = findViewById(R.id.buttonShowPassword1);
        buttonShowPassword2 = findViewById(R.id.buttonShowPassword2);

        buttonRegister.setOnClickListener(v -> registerUser());

        buttonBack.setOnClickListener(v -> finish());

        buttonShowPassword1.setOnClickListener(v -> togglePasswordVisibility(editTextPassword, buttonShowPassword1, true));
        buttonShowPassword2.setOnClickListener(v -> togglePasswordVisibility(editTextConfirmPassword, buttonShowPassword2, false));
    }

    private void registerUser() {
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Неверный формат email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this, "Неверный формат номера телефона", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        // Регистрация в Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserToFirestore(firebaseUser.getUid(), email, phone);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String userId, String email, String phone) {
        Map<String, Object> user = new HashMap<>();
        user.put("userId", userId);
        user.put("email", email);
        user.put("phone", phone);
        user.put("friends", null);
        user.put("wishlists", null);

        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Ошибка сохранения данных: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void togglePasswordVisibility(EditText editText, ImageButton button, boolean isFirst) {
        if (isFirst ? passwordVisible1 : passwordVisible2) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            button.setImageResource(R.drawable.ic_visibility_off);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            button.setImageResource(R.drawable.ic_visibility);
        }
        if (isFirst) {
            passwordVisible1 = !passwordVisible1;
        } else {
            passwordVisible2 = !passwordVisible2;
        }
        editText.setSelection(editText.getText().length());
    }
}