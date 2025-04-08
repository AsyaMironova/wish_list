package com.example.wishlist.ui.auth;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wishlist.R;

public class AgreementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        Button acceptButton = findViewById(R.id.buttonAcceptAgreement);
        acceptButton.setOnClickListener(v -> finish());
    }
}