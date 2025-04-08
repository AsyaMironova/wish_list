package com.example.wishlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.models.WishList;

import java.util.List;

public class FriendWishlistsAdapter extends RecyclerView.Adapter<FriendWishlistsAdapter.ViewHolder> {

    public interface OnWishlistClickListener {
        void onViewGiftsClick(WishList wishlist);
    }

    private List<WishList> wishlistList;
    private OnWishlistClickListener listener;

    public FriendWishlistsAdapter(List<WishList> wishlistList, OnWishlistClickListener listener) {
        this.wishlistList = wishlistList;
        this.listener = listener;
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
        WishList wishlist = wishlistList.get(position);
        holder.title.setText(wishlist.getName());
        holder.viewGiftsButton.setOnClickListener(v -> listener.onViewGiftsClick(wishlist));
    }

    @Override
    public int getItemCount() {
        return wishlistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        Button viewGiftsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewWishlistName);
            viewGiftsButton = itemView.findViewById(R.id.buttonViewGifts);
        }
    }
}