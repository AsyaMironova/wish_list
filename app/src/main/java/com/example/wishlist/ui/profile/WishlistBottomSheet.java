package com.example.wishlist.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wishlist.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.example.wishlist.ui.gifts.GiftBottomSheet;

import java.util.Arrays;

public class WishlistBottomSheet extends BottomSheetDialogFragment {

    private OnSaveListener onSaveListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wishlist_bottom_sheet, container, false);

        EditText nameEditText = view.findViewById(R.id.editTextWishlistName);
        EditText descEditText = view.findViewById(R.id.editTextWishlistDescription);
        Button saveButton = view.findViewById(R.id.buttonSaveWishlist);

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String desc = descEditText.getText().toString().trim();
            if (name.isEmpty()) {
                nameEditText.setError("Введите название списка");
                return;
            }
            if (onSaveListener != null) {
                onSaveListener.onSave(name, desc);
            }
            dismiss();
        });

        Spinner privacySpinner = view.findViewById(R.id.spinnerPrivacy);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Arrays.asList("Только я", "Друзья", "Все")
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        privacySpinner.setAdapter(adapter);

        Button addGiftButton = view.findViewById(R.id.buttonAddGift);
        addGiftButton.setOnClickListener(v -> {
            GiftBottomSheet giftBottomSheet = new GiftBottomSheet();
            giftBottomSheet.show(getParentFragmentManager(), "GiftBottomSheet");
        });

        return view;
    }

    public void setOnSaveListener(OnSaveListener listener) {
        this.onSaveListener = listener;
    }

    public interface OnSaveListener {
        void onSave(String name, String description);
    }
}