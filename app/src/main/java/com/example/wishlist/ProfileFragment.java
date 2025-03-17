package com.example.wishlist;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.Drawable;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private AppBarLayout appBarLayout;
    private LinearLayout linearLayoutSearch;
    private RecyclerView recyclerViewWishlists;
    private TextView usernameTextView;
    private TextView userIdTextView;
    private EditText editTextAbout;
    private ImageView imageViewProfile;
    private TextView textViewName;
    private TextView textViewNickname;
    private FirebaseFirestore db;
    private final List<String> wishlists = new ArrayList<>();
    private WishlistAdapter adapter;
    private final List<Gift> gifts = new ArrayList<>();
    private ProgressBar progressBar;

    private String currentWishlistName;
    private String currentWishlistDescription;
    private String currentWishlistPrivacy;

    private ImageView clearSearchIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        appBarLayout = view.findViewById(R.id.appBarLayout);
        linearLayoutSearch = view.findViewById(R.id.search_bar_layout); // Инициализация linearLayoutSearch
        recyclerViewWishlists = view.findViewById(R.id.recyclerViewWishlists);
        usernameTextView = view.findViewById(R.id.textViewName);
        userIdTextView = view.findViewById(R.id.textViewNickname);
        editTextAbout = view.findViewById(R.id.editTextAbout);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        textViewName = view.findViewById(R.id.textViewName);
        textViewNickname = view.findViewById(R.id.textViewNickname);
        progressBar = view.findViewById(R.id.progressBar);
        clearSearchIcon = view.findViewById(R.id.clear_search_icon);

        recyclerViewWishlists.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WishlistAdapter(wishlists);
        recyclerViewWishlists.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    linearLayoutSearch.setVisibility(View.VISIBLE);
                } else if (verticalOffset == 0) {
                    linearLayoutSearch.setVisibility(View.GONE);
                }
            }
        });

        FloatingActionButton buttonCreateWishlist = view.findViewById(R.id.buttonCreateWishlist);
        buttonCreateWishlist.setOnClickListener(v -> {
            showWishlistDialog("New Wishlist"); // Передаем строку "New Wishlist"
        });

        loadUserData();
        loadAboutText();
        loadWishlists(); // Загрузка виш-листов при создании фрагмента

        editTextAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                saveAboutText(s.toString());
            }
        });

        adapter.setOnItemClickListener(wishlistName -> {
            showWishlistDialog(wishlistName);
        });

        // Добавляем TextWatcher для EditText поиска
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString();
                performSearch(searchText);
                clearSearchIcon.setVisibility(searchText.isEmpty() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}


        });

        return view;
    }

    private void loadUserData() {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String username = prefs.getString("username", "Имя");
            String userId = prefs.getString("userId", "12345");

            textViewName.setText(username);
            textViewNickname.setText(userId);
        } catch (Exception e) {
            Log.e("ProfileFragment", "Ошибка загрузки данных пользователя", e);
            Toast.makeText(getContext(), "Ошибка загрузки данных пользователя", Toast.LENGTH_SHORT).show();
        }
    }

    private void performSearch(String searchText) {
        List<String> filteredWishlists = new ArrayList<>();
        for (String wishlist : wishlists) {
            if (wishlist.toLowerCase().contains(searchText.toLowerCase())) {
                filteredWishlists.add(wishlist);
            }
        }
        adapter = new WishlistAdapter(filteredWishlists); // Создаем новый адаптер
        recyclerViewWishlists.setAdapter(adapter); // Устанавливаем новый адаптер
    }

    private void loadAboutText() {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String aboutText = prefs.getString("about", "");
            editTextAbout.setText(aboutText);
        } catch (Exception e) {
            Log.e("ProfileFragment", "Ошибка загрузки текста 'О себе'", e);
            Toast.makeText(getContext(), "Ошибка загрузки текста 'О себе'", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAboutText(String aboutText) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("about", aboutText);
            editor.apply();
        } catch (Exception e) {
            Log.e("ProfileFragment", "Ошибка сохранения текста 'О себе'", e);
            Toast.makeText(getContext(), "Ошибка сохранения текста 'О себе'", Toast.LENGTH_SHORT).show();
        }
    }
    private void showWishlistDialog(String wishlistName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_wishlist, null);
        builder.setView(dialogView);

        EditText editTextWishlistName = dialogView.findViewById(R.id.editTextWishlistName);
        EditText editTextWishlistDescription = dialogView.findViewById(R.id.editTextWishlistDescription);
        Spinner spinnerPrivacy = dialogView.findViewById(R.id.spinnerPrivacy);
        Button buttonAddGift = dialogView.findViewById(R.id.buttonAddGift);
        Button buttonSaveWishlist = dialogView.findViewById(R.id.buttonSaveWishlist);
        Button buttonDeleteWishlist = dialogView.findViewById(R.id.buttonDeleteWishlist);
        RecyclerView recyclerViewGifts = dialogView.findViewById(R.id.recyclerViewGifts);
        ProgressBar dialogProgressBar = dialogView.findViewById(R.id.progressBar);

        // Настройка Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.privacy_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrivacy.setAdapter(adapter);

        recyclerViewGifts.setLayoutManager(new LinearLayoutManager(getContext()));
        GiftAdapter giftAdapter = new GiftAdapter(gifts);
        recyclerViewGifts.setAdapter(giftAdapter);

        AlertDialog dialog = builder.create();
        dialog.show();

        if (wishlistName != null) {
            editTextWishlistName.setText(wishlistName);
            // Загрузка описания и приватности из данных виш-листа
            currentWishlistName = wishlistName;
            currentWishlistDescription = getWishlistDescription(wishlistName);
            currentWishlistPrivacy = getWishlistPrivacy(wishlistName);

            editTextWishlistDescription.setText(currentWishlistDescription);
            spinnerPrivacy.setSelection(adapter.getPosition(currentWishlistPrivacy));

            // Загрузка подарков для этого вишлиста
            loadGiftsForWishlist(wishlistName, recyclerViewGifts, giftAdapter, dialogProgressBar);
        } else {
            buttonDeleteWishlist.setVisibility(View.GONE);
            gifts.clear(); // Очистить список подарков при создании нового вишлиста
            dialogProgressBar.setVisibility(View.GONE);
        }

        buttonAddGift.setOnClickListener(v -> {
            showGiftDialog(null);
        });

        buttonSaveWishlist.setOnClickListener(v -> {
            String newWishlistName = editTextWishlistName.getText().toString();
            String newWishlistDescription = editTextWishlistDescription.getText().toString();
            String privacy = spinnerPrivacy.getSelectedItem().toString();

            if (!newWishlistName.isEmpty()) {
                if (wishlistName == null) {
                    // Создание нового виш-листа
                    wishlists.add(newWishlistName);
                } else {
                    // Редактирование существующего виш-листа
                    int index = wishlists.indexOf(wishlistName);
                    if (index != -1) {
                        wishlists.set(index, newWishlistName);
                    }
                }
                // Сохранение описания и приватности
                saveWishlistData(newWishlistName, newWishlistDescription, privacy, dialog);

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        buttonDeleteWishlist.setOnClickListener(v -> {
            if (wishlistName != null) {
                int index = wishlists.indexOf(wishlistName);
                if (index != -1) {
                    wishlists.remove(index);
                    deleteWishlistData(wishlistName);
                    // Удаление подарков для этого вишлиста
                    deleteGiftsForWishlist(wishlistName);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }
    private void showGiftDialog(Gift gift) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_gift, null);
        builder.setView(dialogView);

        EditText editTextGiftName = dialogView.findViewById(R.id.editTextGiftName);
        EditText editTextGiftDescription = dialogView.findViewById(R.id.editTextGiftDescription);
        EditText editTextGiftLink = dialogView.findViewById(R.id.editTextGiftLink);
        EditText editTextGiftPrice = dialogView.findViewById(R.id.editTextGiftPrice);
        Button buttonSaveGift = dialogView.findViewById(R.id.buttonSaveGift);
        Button buttonDeleteGift = dialogView.findViewById(R.id.buttonDeleteGift);

        if (gift != null) {
            editTextGiftName.setText(gift.getName());
            editTextGiftDescription.setText(gift.getDescription());
            editTextGiftLink.setText(gift.getLink());
            editTextGiftPrice.setText(gift.getPrice());
        } else {
            buttonDeleteGift.setVisibility(View.GONE);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonSaveGift.setOnClickListener(v -> {
            String name = editTextGiftName.getText().toString();
            String description = editTextGiftDescription.getText().toString();
            String link = editTextGiftLink.getText().toString();
            String price = editTextGiftPrice.getText().toString();

            if (!name.isEmpty()) {
                if (gift == null) {
                    gifts.add(new Gift(name, description, link, price));
                } else {
                    gift.setName(name); // Используем сеттеры
                    gift.setDescription(description);
                    gift.setLink(link);
                    gift.setPrice(price);
                }
                showWishlistDialog(currentWishlistName);
                dialog.dismiss();
            }
        });


        buttonDeleteGift.setOnClickListener(v -> {
            if (gift != null) {
                gifts.remove(gift);
                showWishlistDialog(currentWishlistName);
                dialog.dismiss();
            }
        });
    }

    private void saveWishlistData(String wishlistName, String description, String privacy, AlertDialog dialog) {
        Map<String, Object> wishlist = new HashMap<>();
        wishlist.put("name", wishlistName);
        wishlist.put("description", description);
        wishlist.put("privacy", privacy);

        db.collection("wishlists").document(wishlistName)
                .set(wishlist)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ProfileFragment", "Виш-лист успешно сохранен");
                    dialog.dismiss();
                    loadWishlists();
                })
                .addOnFailureListener(e -> {
                    Log.w("ProfileFragment", "Ошибка сохранения виш-листа", e);
                    Toast.makeText(getContext(), "Ошибка сохранения виш-листа", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteWishlistData(String wishlistName) {
        db.collection("wishlists").document(wishlistName)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("ProfileFragment", "Виш-лист успешно удален");
                    loadWishlists();
                })
                .addOnFailureListener(e -> {
                    Log.w("ProfileFragment", "Ошибка удаления виш-листа", e);
                    Toast.makeText(getContext(), "Ошибка удаления виш-листа", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveGiftForWishlist(String wishlistName, Gift gift, AlertDialog dialog, RecyclerView.Adapter adapter) {
        DocumentReference wishlistRef = db.collection("wishlists").document(wishlistName);
        wishlistRef.collection("gifts").document(gift.getName())
                .set(gift)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ProfileFragment", "Подарок успешно сохранен");
                    dialog.dismiss();
                    loadGiftsForWishlist(wishlistName, (RecyclerView) dialog.findViewById(R.id.recyclerViewGifts), adapter, dialog.findViewById(R.id.progressBar));
                })
                .addOnFailureListener(e -> {
                    Log.w("ProfileFragment", "Ошибка сохранения подарка", e);
                    Toast.makeText(getContext(), "Ошибка сохранения подарка", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateGiftForWishlist(String wishlistName, Gift gift, AlertDialog dialog, RecyclerView.Adapter adapter) {
        saveGiftForWishlist(wishlistName, gift, dialog, adapter);
    }

    private void deleteGiftFromWishlist(String wishlistName, Gift gift) {
        db.collection("wishlists").document(wishlistName).collection("gifts").document(gift.getName())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("ProfileFragment", "Подарок успешно удален"))
                .addOnFailureListener(e -> {
                    Log.w("ProfileFragment", "Ошибка удаления подарка", e);
                    Toast.makeText(getContext(), "Ошибка удаления подарка", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadGiftsForWishlist(String wishlistName, RecyclerView recyclerView, RecyclerView.Adapter adapter, ProgressBar progressBar) {
        gifts.clear();
        progressBar.setVisibility(View.VISIBLE);
        db.collection("wishlists").document(wishlistName).collection("gifts")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Gift gift = document.toObject(Gift.class);
                            gifts.add(gift);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("ProfileFragment", "Ошибка загрузки подарков", task.getException());
                        Toast.makeText(getContext(), "Ошибка загрузки подарков", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteGiftsForWishlist(String wishlistName) {
        db.collection("wishlists").document(wishlistName).collection("gifts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                    } else {
                        Log.w("ProfileFragment", "Ошибка удаления подарков", task.getException());
                        Toast.makeText(getContext(), "Ошибка удаления подарков", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadWishlists() {
        progressBar.setVisibility(View.VISIBLE);
        wishlists.clear();
        db.collection("wishlists")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            wishlists.add(document.getString("name"));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("ProfileFragment", "Ошибка загрузки виш-листов", task.getException());
                        Toast.makeText(getContext(), "Ошибка загрузки виш-листов", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getWishlistDescription(String wishlistName) {
        // Загрузка описания из Firestore
        // Загрузка описания из Firestore
        return ""; // Заменить на реальную загрузку
    }

    private String getWishlistPrivacy(String wishlistName) {
        // Загрузка приватности из Firestore
        return "Только я"; // Заменить на реальную загрузку
    }

    public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

        private final List<Gift> gifts;

        public GiftAdapter(List<Gift> gifts) {
            this.gifts = gifts;
        }

        @NonNull
        @Override
        public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent, false);
            return new GiftViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
            Gift gift = gifts.get(position);
            holder.textViewGiftName.setText(gift.getName());
            holder.textViewGiftDescription.setText(gift.getDescription());
            holder.textViewGiftLink.setText(gift.getLink());
            holder.textViewGiftPrice.setText(gift.getPrice());
        }

        @Override
        public int getItemCount() {
            return gifts.size();
        }

        public class GiftViewHolder extends RecyclerView.ViewHolder {

            final TextView textViewGiftName;
            final TextView textViewGiftDescription;
            final TextView textViewGiftLink;
            final TextView textViewGiftPrice;

            public GiftViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewGiftName = itemView.findViewById(R.id.textViewGiftName);
                textViewGiftDescription = itemView.findViewById(R.id.textViewGiftDescription);
                textViewGiftLink = itemView.findViewById(R.id.textViewGiftLink);
                textViewGiftPrice = itemView.findViewById(R.id.textViewGiftPrice);
            }
        }
    }
}