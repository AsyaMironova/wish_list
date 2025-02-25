package com.example.wishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private int verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.editTextEmail);
        Button buttonGetCode = findViewById(R.id.buttonGetCode);
        ImageButton buttonBack = findViewById(R.id.buttonBack);

        buttonGetCode.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();

            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Введите email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ForgotPasswordActivity.this, "Неверный формат email", Toast.LENGTH_SHORT).show();
                return;
            }

            verificationCode = generateVerificationCode();
            sendVerificationEmail(email, verificationCode);
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    private int generateVerificationCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    private void sendVerificationEmail(String recipientEmail, int code) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    String senderEmail = "your_email@gmail.com"; // Замените на ваш email
                    String senderPassword = "your_password"; // Замените на ваш пароль

                    Properties properties = new Properties();
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.starttls.enable", "true");
                    properties.put("mail.smtp.host", "smtp.gmail.com");
                    properties.put("mail.smtp.port", "587");

                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(senderEmail, senderPassword);
                        }
                    });

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(senderEmail));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                    message.setSubject("Код подтверждения");
                    message.setText("Ваш код подтверждения: " + code);

                    Transport.send(message);

                    runOnUiThread(() -> {
                        Toast.makeText(ForgotPasswordActivity.this, "Код отправлен на email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
                        intent.putExtra("email", recipientEmail);
                        intent.putExtra("code", code);
                        startActivity(intent);
                    });

                } catch (MessagingException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "Ошибка отправки кода", Toast.LENGTH_SHORT).show());
                }
                return null;
            }
        }.execute();
    }
}
