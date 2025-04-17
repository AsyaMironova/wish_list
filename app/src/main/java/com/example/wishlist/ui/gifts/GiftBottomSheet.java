package com.example.wishlist.ui.gifts;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class GiftBottomSheet extends BottomSheetDialogFragment {

    private String wishlistId;
    private Gift gift;

    public static GiftBottomSheet newInstance(Gift gift, String wishlistId) {
        GiftBottomSheet sheet = new GiftBottomSheet();
        Bundle args = new Bundle();
        args.putString("wishlistId", wishlistId);
        args.putString("giftId", gift.getId());
        args.putString("name", gift.getName());
        args.putString("description", gift.getDescription());
        args.putString("price", gift.getPrice());
        args.putString("link", gift.getLink());
        sheet.setArguments(args);
        return sheet;
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

        if (getArguments() != null) {
            wishlistId = getArguments().getString("wishlistId");

            gift = new Gift();
            gift.setId(getArguments().getString("giftId"));
            gift.setName(getArguments().getString("name"));
            gift.setDescription(getArguments().getString("description"));
            gift.setPrice(getArguments().getString("price"));
            gift.setLink(getArguments().getString("link"));

            nameEditText.setText(gift.getName());
            descEditText.setText(gift.getDescription());
            linkEditText.setText(gift.getLink());
            priceEditText.setText(gift.getPrice());

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

            Gift newGift = new Gift();
            newGift.setId(gift != null ? gift.getId() : null);
            newGift.setName(name);
            newGift.setDescription(desc);
            newGift.setLink(link);
            newGift.setPrice(price);

            CollectionReference giftsRef = FirebaseFirestore.getInstance()
                    .collection("wishlists")
                    .document(wishlistId)
                    .collection("gifts");

            if (newGift.getId() != null) {
                giftsRef.document(newGift.getId()).set(newGift);
            } else {
                giftsRef.add(newGift);
            }

            dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            if (wishlistId == null || gift == null || gift.getId() == null) {
                Log.e("GiftBottomSheet", "Ошибка удаления: отсутствует wishlistId или gift");
                return;
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle("Удалить подарок?")
                    .setMessage("Вы уверены, что хотите удалить этот подарок?")
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        FirebaseFirestore.getInstance()
                                .collection("wishlists")
                                .document(wishlistId)
                                .collection("gifts")
                                .document(gift.getId())
                                .delete();

                        dismiss();
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        return view;
    }
}