package com.example.wishlist.ui.gifts;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wishlist.R;
import com.example.wishlist.models.Gift;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class GiftBottomSheet extends BottomSheetDialogFragment {

    private OnGiftSaveListener onGiftSaveListener;
    private OnGiftDeleteListener onGiftDeleteListener;

    private boolean isEditMode = false;
    private String initialGiftId;

    public void setEditMode(String giftId) {
        isEditMode = true;
        initialGiftId = giftId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_bottom_sheet, container, false);

        EditText nameEditText = view.findViewById(R.id.editTextGiftName);
        EditText descEditText = view.findViewById(R.id.editTextGiftDescription);
        EditText linkEditText = view.findViewById(R.id.editTextGiftLink);
        EditText priceEditText = view.findViewById(R.id.editTextGiftPrice);
        Button saveButton = view.findViewById(R.id.buttonSaveGift);
        Button deleteButton = view.findViewById(R.id.buttonDeleteGift);

        if (isEditMode) {
            deleteButton.setVisibility(View.VISIBLE);
        }

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String desc = descEditText.getText().toString().trim();
            String link = linkEditText.getText().toString().trim();
            String price = priceEditText.getText().toString().trim();

            if (name.isEmpty()) {
                nameEditText.setError("Введите название подарка");
                return;
            }
            if (price.isEmpty()) {
                priceEditText.setError("Введите цену");
                return;
            }

            if (onGiftSaveListener != null) {
                onGiftSaveListener.onSave(name, desc, price, link);
            }
            dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Удалить подарок?")
                    .setMessage("Вы уверены, что хотите удалить этот подарок?")
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        if (onGiftDeleteListener != null) {
                            onGiftDeleteListener.onDelete(initialGiftId);
                        }
                        dismiss();
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        return view;
    }

    public void setOnGiftSaveListener(OnGiftSaveListener listener) {
        this.onGiftSaveListener = listener;
    }

    public void setOnGiftDeleteListener(OnGiftDeleteListener listener) {
        this.onGiftDeleteListener = listener;
    }

    public interface OnGiftSaveListener {
        void onSave(String name, String description, String price, String link);
    }

    public static GiftBottomSheet newInstance(Gift gift) {
        GiftBottomSheet bottomSheet = new GiftBottomSheet();
        Bundle args = new Bundle();
        args.putString("giftId", gift.getId());
        args.putString("title", gift.getTitle());
        args.putString("description", gift.getDescription());
        args.putString("link", gift.getLink());
        bottomSheet.setArguments(args);
        return bottomSheet;
    }

    public void deleteGift(String wishlistId, String giftId) {
        FirebaseFirestore.getInstance()
                .collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .document(giftId)
                .delete();
    }

    public interface OnGiftDeleteListener {
        void onDelete(String giftId);
    }
}