package com.example.wishlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.models.Friend;
import com.example.wishlist.models.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    private List<Friend> friendList = new ArrayList<>();
    private final OnFriendClickListener listener;

    public interface OnFriendClickListener {
        void onFriendClick(Friend friend);
    }

    public FriendsAdapter(OnFriendClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void setFriendList(List<Friend> friends) {
        this.friendList = new ArrayList<>(friends);
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageAvatar;
        private final TextView textName;
        private final TextView textUsername;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
            textName = itemView.findViewById(R.id.textName);
            textUsername = itemView.findViewById(R.id.textUsername);
        }

        public void bind(Friend friend) {
            textName.setText(friend.getName());
            textUsername.setText(friend.getUsername());
            // TODO: Добавить подгрузку аватара, когда появится URL
            itemView.setOnClickListener(v -> listener.onFriendClick(friend));
        }
    }
}
