package com.example.wishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private ImageButton buttonShowPassword;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonShowPassword = findViewById(R.id.buttonShowPassword);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            // TODO: Добавить логику аутентификации

            Toast.makeText(LoginActivity.this, "Вход выполнен", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish();
        });

        buttonBack.setOnClickListener(v -> finish());

        buttonShowPassword.setOnClickListener(v -> {
            if (passwordVisible) {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                buttonShowPassword.setImageResource(R.drawable.ic_visibility_off);
            } else {
                editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                buttonShowPassword.setImageResource(R.drawable.ic_visibility);
            }
            passwordVisible = !passwordVisible;
            editTextPassword.setSelection(editTextPassword.getText().length());
        });
    }
}