package com.example.wishlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.models.Gift;

import java.util.List;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.GiftViewHolder> {

    private List<Gift> gifts;

    public GiftsAdapter() {
    }


    public void setData(List<Gift> gifts) {
        this.gifts.clear();
        this.gifts.addAll(gifts);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gift, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        Gift gift = gifts.get(position);
        holder.textViewName.setText(gift.getName());
        holder.textViewPrice.setText(gift.getPrice() + " ₽");
        holder.textViewDesc.setText(gift.getDescription());
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public void setGiftList(List<Gift> gifts) {
    }


    static class GiftViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewDesc;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewGiftName);
            textViewPrice = itemView.findViewById(R.id.textViewGiftPrice);
            textViewDesc = itemView.findViewById(R.id.textViewGiftDescription);
        }
    }
}