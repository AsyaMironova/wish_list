package com.example.wishlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.models.WishList;

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private List<WishList> wishlists = new ArrayList<>();
    private final OnWishlistClickListener listener;

    public WishlistAdapter(OnWishlistClickListener listener) {
        this.listener = listener;
    }

    public void setWishlists(List<WishList> wishlists) {
        this.wishlists = wishlists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        WishList wishlist = wishlists.get(position);
        holder.titleTextView.setText(wishlist.getName());
        holder.itemView.setOnClickListener(v -> listener.onWishlistClick(wishlist));
    }

    @Override
    public int getItemCount() {
        return wishlists.size();
    }

    public interface OnWishlistClickListener {
        void onWishlistClick(WishList wishlist);
    }

    static class WishlistViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewWishlistName);
        }
    }
}