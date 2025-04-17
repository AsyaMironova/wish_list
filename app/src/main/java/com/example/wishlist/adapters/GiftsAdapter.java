package com.example.wishlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.models.Gift;

import java.util.ArrayList;
import java.util.List;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.GiftViewHolder> {

    private final OnGiftClickListener clickListener;
    private final OnGiftDeleteListener deleteListener;
    private List<Gift> giftList = new ArrayList<>();

    public GiftsAdapter(OnGiftClickListener clickListener, OnGiftDeleteListener deleteListener) {
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    public void setGiftList(List<Gift> gifts) {
        this.giftList = gifts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        Gift gift = giftList.get(position);
        holder.bind(gift);
    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    class GiftViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, descView, priceView;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textViewGiftName);
            descView = itemView.findViewById(R.id.textViewGiftDescription);
            priceView = itemView.findViewById(R.id.textViewGiftPrice);

            itemView.setOnClickListener(v -> clickListener.onGiftClick(giftList.get(getAdapterPosition())));
            itemView.setOnLongClickListener(v -> {
                deleteListener.onGiftDelete(giftList.get(getAdapterPosition()));
                return true;
            });
        }

        public void bind(Gift gift) {
            nameView.setText(gift.getName());
            descView.setText(gift.getDescription());
            priceView.setText(gift.getPrice() + " ₽");
        }
    }

    public interface OnGiftClickListener {
        void onGiftClick(Gift gift);
    }

    public interface OnGiftDeleteListener {
        void onGiftDelete(Gift gift);
    }
}