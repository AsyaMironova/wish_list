package com.example.wishlist.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wishlist.R;
import com.example.wishlist.viewmodels.ProfileViewModel;
import com.example.wishlist.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SettingsFragment extends Fragment {

    private EditText editTextNickname, editTextUserId;
    private TextView textViewUserIdStatus;
    private ImageView imageStatus;
    private Button saveUserIdButton;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable checkRunnable;

    private ProfileViewModel profileViewModel;

    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        editTextNickname = view.findViewById(R.id.editTextNickname);
        editTextUserId = view.findViewById(R.id.editTextUserId);
        textViewUserIdStatus = view.findViewById(R.id.textViewUserIdStatus);
        imageStatus = view.findViewById(R.id.imageViewUserIdStatus);
        saveUserIdButton = view.findViewById(R.id.buttonSaveUserId);

        Button saveNicknameButton = view.findViewById(R.id.buttonSaveNickname);
        Button logoutButton = view.findViewById(R.id.buttonLogout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        if (user != null) {
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            editTextNickname.setText(documentSnapshot.getString("nickname"));
                            editTextUserId.setText(documentSnapshot.getString("user_id"));
                        }
                    });
        }

        saveNicknameButton.setOnClickListener(v -> {
            String newNickname = editTextNickname.getText().toString().trim();
            if (!newNickname.isEmpty() && user != null) {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(user.getUid())
                        .update("nickname", newNickname)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(requireContext(), "Имя обновлено", Toast.LENGTH_SHORT).show();
                            profileViewModel.fetchUser(); // <-- обновим профиль
                        });
            }
        });

        editTextUserId.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (checkRunnable != null) handler.removeCallbacks(checkRunnable);
                checkRunnable = () -> validateUserId(s.toString().trim());
                handler.postDelayed(checkRunnable, 400);
            }
        });

        saveUserIdButton.setOnClickListener(v -> {
            String newUserId = editTextUserId.getText().toString().trim();
            if (!newUserId.isEmpty() && user != null) {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(user.getUid())
                        .update("user_id", newUserId)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(requireContext(), "user_id обновлён", Toast.LENGTH_SHORT).show();
                            profileViewModel.fetchUser(); // <-- обновим профиль
                        });
            }
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }

    private void validateUserId(String userId) {
        if (userId.length() > 25) {
            showError("Максимум 25 символов");
            return;
        }

        if (userId.contains(" ")) {
            showError("Пробелы недопустимы");
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    boolean taken = false;
                    for (QueryDocumentSnapshot doc : snapshot) {
                        if (!doc.getId().equals(user.getUid())) {
                            taken = true;
                            break;
                        }
                    }

                    if (taken) {
                        showError("Занят");
                    } else {
                        showAvailable("Доступен");
                    }
                });
    }

    private void showError(String text) {
        textViewUserIdStatus.setText(text);
        textViewUserIdStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        imageStatus.setImageResource(R.drawable.ic_error_red);
        imageStatus.setVisibility(View.VISIBLE);
    }

    private void showAvailable(String text) {
        textViewUserIdStatus.setText(text);
        textViewUserIdStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        imageStatus.setImageResource(R.drawable.ic_check_green);
        imageStatus.setVisibility(View.VISIBLE);
    }
}