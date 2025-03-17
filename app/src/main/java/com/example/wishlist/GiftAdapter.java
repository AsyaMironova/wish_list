package com.example.wishlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    private List<Gift> gifts;

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

    public static class GiftViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGiftName;
        TextView textViewGiftDescription;
        TextView textViewGiftLink;
        TextView textViewGiftPrice;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGiftName = itemView.findViewById(R.id.textViewGiftName);
            textViewGiftDescription = itemView.findViewById(R.id.textViewGiftDescription);
            textViewGiftLink = itemView.findViewById(R.id.textViewGiftLink);
            textViewGiftPrice = itemView.findViewById(R.id.textViewGiftPrice);
        }
    }
}