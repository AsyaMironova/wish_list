package com.example.wishlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private List<String> wishlists;
    private OnItemClickListener listener; // Добавлен слушатель

    // Добавлен конструктор
    public WishlistAdapter(List<String> wishlists) {
        this.wishlists = wishlists;
    }

    // Добавлен метод для установки слушателя
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        String wishlist = wishlists.get(position);
        holder.wishlistName.setText(wishlist);

        // Обработка нажатия на элемент списка
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(wishlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlists.size();
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {

        TextView wishlistName;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            wishlistName = itemView.findViewById(R.id.wishlistName);
        }
    }

    // Интерфейс слушателя
    public interface OnItemClickListener {
        void onItemClick(String wishlistName);
    }
}