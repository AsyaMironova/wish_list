package com.example.wishlist.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishlist.R;
import com.example.wishlist.adapters.WishlistAdapter;
import com.example.wishlist.models.WishList;
import com.example.wishlist.ui.gifts.GiftListFragment;
import com.example.wishlist.viewmodels.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private WishlistAdapter wishlistAdapter;

    private EditText editTextNickname;
    private EditText editTextUserId;
    private EditText editTextAbout;
    private ImageView imageViewProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextNickname = view.findViewById(R.id.editTextNickname);
        editTextUserId = view.findViewById(R.id.editTextUserId);
        editTextAbout = view.findViewById(R.id.editTextProfileAbout);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        RecyclerView recyclerViewWishlists = view.findViewById(R.id.recyclerViewWishlists);
        FloatingActionButton fabCreateWishlist = view.findViewById(R.id.fabCreateWishlist);

        recyclerViewWishlists.setLayoutManager(new LinearLayoutManager(getContext()));
        wishlistAdapter = new WishlistAdapter(wishlist -> {
            Fragment fragment = GiftListFragment.newInstance(wishlist.getId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }, wishlist -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Удалить виш-лист?")
                    .setMessage("Вы уверены, что хотите удалить виш-лист \"" + wishlist.getName() + "\"?")
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        viewModel.deleteWishlist(wishlist);
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });
        recyclerViewWishlists.setAdapter(wishlistAdapter);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getUserNickname().observe(getViewLifecycleOwner(), editTextNickname::setText);
        viewModel.getUserId().observe(getViewLifecycleOwner(), editTextUserId::setText);
        viewModel.getUserAbout().observe(getViewLifecycleOwner(), editTextAbout::setText);
        viewModel.getWishlists().observe(getViewLifecycleOwner(), wishlistAdapter::setWishlists);
        viewModel.getProfileImageUrl().observe(getViewLifecycleOwner(), url ->
                Glide.with(requireContext()).load(url)
                        .placeholder(R.drawable.ic_profile)
                        .into(imageViewProfile)
        );

        editTextAbout.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) viewModel.updateAbout(editTextAbout.getText().toString());
        });

        editTextNickname.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) viewModel.updateNickname(editTextNickname.getText().toString());
        });

        editTextUserId.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) viewModel.updateUserId(editTextUserId.getText().toString());
        });

        fabCreateWishlist.setOnClickListener(v -> {
            WishlistBottomSheet sheet = new WishlistBottomSheet();
            sheet.setOnSaveListener((name, description) -> {
                WishList newWishlist = new WishList(name, description);
                viewModel.createWishlist(newWishlist);
            });
            sheet.show(getParentFragmentManager(), "WishlistBottomSheet");
        });

        viewModel.fetchUser();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume(); }
}