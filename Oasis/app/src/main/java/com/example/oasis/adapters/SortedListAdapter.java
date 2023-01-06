package com.example.oasis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oasis.R;
import com.example.oasis.databinding.ItemSortedBinding;
import com.example.oasis.interfaces.SortedListener;
import com.example.oasis.viewmodels.SortedListViewModel;

import java.util.ArrayList;
import java.util.List;

public class SortedListAdapter extends RecyclerView.Adapter<SortedListAdapter.ViewHolder> {
    List<String> listSorted = new ArrayList<>();
    SortedListener sortedListener = null;

    public SortedListAdapter(List<String> listSorted, SortedListener sortedListener) {
        this.listSorted = listSorted;
        this.sortedListener = sortedListener;
    }

    @NonNull
    @Override
    public SortedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSortedBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_sorted, parent, false);
        return new ViewHolder(binding, sortedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SortedListAdapter.ViewHolder holder, int position) {
        String title = listSorted.get(position);
        holder.viewModel.setTitle(title);
    }

    @Override
    public int getItemCount() {
        return listSorted.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemSortedBinding binding;
        private SortedListViewModel viewModel = new SortedListViewModel();

        ViewHolder(ItemSortedBinding binding, SortedListener sortedListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setSortedListViewModel(viewModel);
        }
    }
}
