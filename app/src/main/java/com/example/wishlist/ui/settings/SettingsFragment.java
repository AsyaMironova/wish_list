package com.example.wishlist.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wishlist.R;
import com.example.wishlist.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        EditText nicknameEditText = view.findViewById(R.id.editTextNickname);
        Button saveButton = view.findViewById(R.id.buttonSaveNickname);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String currentNickname = documentSnapshot.getString("nickname");
                            nicknameEditText.setText(currentNickname);
                        }
                    });
        }

        saveButton.setOnClickListener(v -> {
            String newNickname = nicknameEditText.getText().toString().trim();
            if (!newNickname.isEmpty()) {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(user.getUid())
                        .update("nickname", newNickname)
                        .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Имя успешно обновлено", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Ошибка при обновлении", Toast.LENGTH_SHORT).show());
            }
        });

        return view;
    }
}