package com.example.oasis.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oasis.R;
import com.example.oasis.adapters.SortedListAdapter;
import com.example.oasis.databinding.SortedListFragmentBinding;
import com.example.oasis.interfaces.SortedListener;
import com.example.oasis.utils.OnSwipeTouch;
import com.example.oasis.utils.SwipeEnum;

import java.util.ArrayList;
import java.util.List;


public class SortedListFragment extends Fragment {

    private SortedListener myListener;
    List<String> listSortedFrg = new ArrayList<>();
    SortedListFragmentBinding binding;
    RecyclerView listSorted;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.sorted_list_fragment, container, false);

        binding.listSortedDisplay.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));

        binding.listSortedDisplay.setAdapter(new SortedListAdapter(this.listSortedFrg, myListener));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listSorted = getView().findViewById(R.id.list_sorted_display);
        listSorted.setOnTouchListener(myListenerSwip);
    }

    private OnSwipeTouch myListenerSwip = new OnSwipeTouch(getContext()) {
        public void onSwipe(SwipeEnum direction) {
            if (direction == SwipeEnum.RIGHT) {
                Log.e("RIGHT RIGHT", "RIGHT RIGHT");
                myListener.backActivity();
            }
        }
    };

    public void setControl(SortedListener listener) {
        myListener = listener;
    }

    public void setDisplayList(List<String> value) {
        listSortedFrg = value;
    }
}