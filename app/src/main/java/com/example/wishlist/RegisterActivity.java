package com.example.wishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private ImageButton buttonShowPassword1;
    private ImageButton buttonShowPassword2;
    private boolean passwordVisible1 = false;
    private boolean passwordVisible2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword1);
        editTextConfirmPassword = findViewById(R.id.editTextPassword2);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonShowPassword1 = findViewById(R.id.buttonShowPassword1);
        buttonShowPassword2 = findViewById(R.id.buttonShowPassword2);

        buttonRegister.setOnClickListener(this::onClick);

        buttonBack.setOnClickListener(v -> finish());

        buttonShowPassword1.setOnClickListener(v -> {
            if (passwordVisible1) {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                buttonShowPassword1.setImageResource(R.drawable.ic_visibility_off);
            } else {
                editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                buttonShowPassword1.setImageResource(R.drawable.ic_visibility);
            }
            passwordVisible1 = !passwordVisible1;
            editTextPassword.setSelection(editTextPassword.getText().length());
        });

        buttonShowPassword2.setOnClickListener(v -> {
            if (passwordVisible2) {
                editTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                buttonShowPassword2.setImageResource(R.drawable.ic_visibility_off);
            } else {
                editTextConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                buttonShowPassword2.setImageResource(R.drawable.ic_visibility);
            }
            passwordVisible2 = !passwordVisible2;
            editTextConfirmPassword.setSelection(editTextConfirmPassword.getText().length());
        });
    }

    private boolean isUserExists(String phone, String email) {
        // TODO: Заменить на вызов API вашего сервера
        // Пример:
        // try {
        //     ApiService apiService = RetrofitClient.getApiService();
        //     Call<Boolean> call = apiService.checkUserExists(phone, email);
        //     return call.execute().body();
        // } catch (IOException e) {
        //     e.printStackTrace();
        //     return false;
        // }
        // Фиктивная проверка
        return phone.equals("+79991234567") || email.equals("test@example.com");
    }

    private void onClick(View v) {
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterActivity.this, "Неверный формат email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(RegisterActivity.this, "Неверный формат номера телефона", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return isUserExists(phone, email);
            }

            @Override
            protected void onPostExecute(Boolean exists) {
                if (exists) {
                    Toast.makeText(RegisterActivity.this, "Пользователь с таким номером или email уже существует", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO: Добавить логику регистрации пользователя

                Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        }.execute();
    }
}