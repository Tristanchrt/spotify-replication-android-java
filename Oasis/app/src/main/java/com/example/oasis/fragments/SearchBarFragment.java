package com.example.oasis.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.example.oasis.R;
import com.example.oasis.databinding.SearchBarFragmentBinding;
import com.example.oasis.interfaces.SearchBarListener;

import java.util.function.LongBinaryOperator;

public class SearchBarFragment extends Fragment {
    private ImageView menuSearchBar;
    private SearchBarListener myListener;
    private SearchView inputSearchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SearchBarFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.search_bar_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuSearchBar = (ImageView) getView().findViewById(R.id.menu);
        inputSearchBar = (SearchView) getView().findViewById(R.id.search_input);
        menuSearchBar.setOnClickListener(menuSearchBarListener);
        inputSearchBar.setOnQueryTextListener(inputSearchBarListener);
    }

    private View.OnClickListener menuSearchBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myListener.openListPage();
        }
    };

    private SearchView.OnQueryTextListener inputSearchBarListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            myListener.filterSearch(s);
            return false;
        }
    };

    public void setControl(SearchBarListener listener) {
        myListener = listener;
    }
}
