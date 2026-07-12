package com.dam.kineo.ui.social;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.kineo.databinding.ItemFriendRequestBinding;
import com.dam.kineo.domain.model.Friend;

public class FriendRequestsAdapter extends
        ListAdapter<Friend, FriendRequestsAdapter.ViewHolder> {

    private final SocialViewModel viewModel;

    public FriendRequestsAdapter(SocialViewModel viewModel) {
        super(new DiffUtil.ItemCallback<Friend>() {
            @Override
            public boolean areItemsTheSame(@NonNull Friend oldItem,
                                           @NonNull Friend newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Friend oldItem,
                                              @NonNull Friend newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFriendRequestBinding binding = ItemFriendRequestBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFriendRequestBinding binding;

        public ViewHolder(ItemFriendRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Friend friend) {
            binding.tvRequestName.setText(friend.getName());
            binding.tvRequestEmail.setText(
                    friend.getUsername() != null ? friend.getUsername() : "");
            binding.btnAcceptRequest.setOnClickListener(v ->
                    viewModel.acceptFriendRequest(friend.getId()));
        }
    }
}
