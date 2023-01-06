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

import com.example.oasis.R;
import com.example.oasis.adapters.SortedListAdapter;
import com.example.oasis.databinding.FragmentFirstBinding;
import com.example.oasis.interfaces.SortedListener;

import java.util.ArrayList;
import java.util.List;


public class FirstFragment extends Fragment {

    private SortedListener myListener;
    private ImageView sorted_album;
    private ImageView sorted_genre;
    private ImageView sorted_user;
    private ImageView sorted_similar;
    List<String> listSorted = new ArrayList<>();
    private Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentFirstBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_first, container, false);

        context = binding.getRoot().getContext();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sorted_album = (ImageView) getView().findViewById(R.id.sorted_album);
        sorted_genre = (ImageView) getView().findViewById(R.id.sorted_genre);
        sorted_user = (ImageView) getView().findViewById(R.id.sorted_avatar);
        sorted_similar = (ImageView) getView().findViewById(R.id.sorted_similar);

        sorted_user.setOnClickListener(on_click_sorted_list);
        sorted_genre.setOnClickListener(on_click_sorted_list);
        sorted_album.setOnClickListener(on_click_sorted_list);
        sorted_similar.setOnClickListener(on_click_sorted_list);
    }

    private View.OnClickListener on_click_sorted_list = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sorted_album.setColorFilter(null);
            sorted_genre.setColorFilter(null);
            sorted_user.setColorFilter(null);

            switch (v.getId()) {
                case R.id.sorted_album:
                    myListener.sortedByAlbum();
                    notif("Triée par album");
                    sorted_album.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    break;
                case R.id.sorted_genre:
                    myListener.sortedByGenre();
                    sorted_genre.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    notif("Triée par genre");
                    break;
                case R.id.sorted_avatar:
                    myListener.sortedByUser();
                    sorted_user.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    notif("Triée par artiste");
                    break;
                case R.id.sorted_similar:
                    myListener.similarArtiste();
                    sorted_similar.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    notif("Triée par similar");
                    break;
            }
        }
    };

    public void setControl(SortedListener listener) {
        myListener = listener;
    }

    public void selectCreation() {
        sorted_album.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }


    public void notif(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}