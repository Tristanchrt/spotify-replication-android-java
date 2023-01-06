package com.example.oasis.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.oasis.databinding.ActivityMain2Binding;
import com.example.oasis.fragments.FirstFragment;
import com.example.oasis.fragments.SortedListFragment;
import com.example.oasis.interfaces.SortedListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.oasis.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;
    private FirstFragment firstFragment;
    private SortedListFragment sortedListFragment;
    List<String> musicArtistes = new ArrayList<>();
    List<String> similarArtist = new ArrayList<>();
    List<String> musicGenres = new ArrayList<>();
    List<String> musicAlbums = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        musicArtistes = (List<String>) getIntent().getSerializableExtra("artiste_list");
        musicGenres = (List<String>) getIntent().getSerializableExtra("genre_list");
        musicAlbums = (List<String>) getIntent().getSerializableExtra("album_list");
        similarArtist = (List<String>) getIntent().getSerializableExtra("similar_list");

//        Log.e("List artiste", String.valueOf(musicArtistes));
//        Log.e("List genre", String.valueOf(musicGenres));
//        Log.e("List album", String.valueOf(musicAlbums));
//        Log.e("List similar", String.valueOf(similarArtist));

        firstFragment = new FirstFragment();
        sortedListFragment = new SortedListFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        sortedListFragment.setDisplayList(musicAlbums);
        transaction.replace(R.id.menu_sorted, firstFragment);
        transaction.replace(R.id.list_sorted_layout, sortedListFragment);

        firstFragment.setControl(sortedListener);
        sortedListFragment.setControl(sortedListener);
        transaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        firstFragment.selectCreation();
    }

    public SortedListener sortedListener = getSortedListener();

    public void refreshView() {
        sortedListFragment.getFragmentManager().beginTransaction().detach(sortedListFragment).commit();
        sortedListFragment.getFragmentManager().beginTransaction().attach(sortedListFragment).commit();
    }

    @NonNull
    private SortedListener getSortedListener() {
        return new SortedListener() {
            @Override
            public void sortedByAlbum() {
                Log.e("ALBUM", "ALBM");
                sortedListFragment.setDisplayList(musicAlbums);
                refreshView();
            }

            @Override
            public void sortedByUser() {
                Log.e("USER", "USER");
                sortedListFragment.setDisplayList(musicArtistes);
                refreshView();
            }

            @Override
            public void sortedByGenre() {
                Log.e("GENRE", "GENRE");
                sortedListFragment.setDisplayList(musicGenres);
                refreshView();
            }

            @Override
            public void similarArtiste() {
                sortedListFragment.setDisplayList(similarArtist);
                refreshView();
            }

            @Override
            public void backActivity() {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        };
    }
}