package com.example.wishlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SettingsFragment extends PreferenceFragmentCompat {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        mAuth = FirebaseAuth.getInstance();

        loadUserData();

        findPreference("username").setOnPreferenceChangeListener((preference, newValue) -> {
            saveUserData("username", (String) newValue);
            return true;
        });
        findPreference("email").setOnPreferenceChangeListener((preference, newValue) -> {
            saveUserData("email", (String) newValue);
            return true;
        });
    }

    private void loadUserData() {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String username = prefs.getString("username", "");
            String email = prefs.getString("email", "");

            findPreference("username").setSummary(username);
            findPreference("email").setSummary(email);
        } catch (Exception e) {
            Log.e("SettingsFragment", "Ошибка загрузки данных пользователя", e);
            Toast.makeText(getContext(), "Ошибка загрузки данных пользователя", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(String key, String value) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.apply();

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                db.collection("users").document(user.getUid())
                        .update(key, value)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("SettingsFragment", "Данные пользователя успешно сохранены в Firestore");
                            Toast.makeText(getContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.w("SettingsFragment", "Ошибка сохранения данных пользователя в Firestore", e);
                            Toast.makeText(getContext(), "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getContext(), "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("SettingsFragment", "Ошибка сохранения данных пользователя", e);
            Toast.makeText(getContext(), "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
        }
    }
}