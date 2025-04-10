// WishlistAdapter.java
package com.example.wishlist.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.models.WishList;

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    public interface OnWishlistClickListener {
        void onWishlistClick(WishList wishlist);
    }

    public interface OnWishlistDeleteListener {
        void onWishlistDelete(WishList wishlist);
    }

    private List<WishList> wishlists = new ArrayList<>();
    private final OnWishlistClickListener clickListener;
    private final OnWishlistDeleteListener deleteListener;

    public WishlistAdapter(OnWishlistClickListener clickListener, OnWishlistDeleteListener deleteListener) {
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    public void setWishlists(List<WishList> wishlists) {
        this.wishlists = wishlists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_wishlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WishList wishlist = wishlists.get(position);
        holder.nameTextView.setText(wishlist.getName());
        holder.viewGiftsButton.setOnClickListener(v -> clickListener.onWishlistClick(wishlist));
        holder.deleteButton.setOnClickListener(v -> deleteListener.onWishlistDelete(wishlist));
    }

    @Override
    public int getItemCount() {
        return wishlists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button viewGiftsButton;
        MaterialButton deleteButton;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewWishlistName);
            viewGiftsButton = itemView.findViewById(R.id.buttonViewGifts);
            deleteButton = itemView.findViewById(R.id.buttonDeleteWishlist);
        }
    }
}
