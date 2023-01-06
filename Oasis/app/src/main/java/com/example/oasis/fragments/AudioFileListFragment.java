package com.example.oasis.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oasis.R;
import com.example.oasis.activities.MainActivity;
import com.example.oasis.adapters.AudioFileListAdapter;
import com.example.oasis.databinding.AudioFileListFragmentBinding;
import com.example.oasis.interfaces.ListListener;
import com.example.oasis.models.AudioFile;

import java.util.ArrayList;
import java.util.List;

public class AudioFileListFragment extends Fragment {

    public ListListener myListener;
    private Context context;
    private List<AudioFile> musicList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AudioFileListFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.audio_file_list_fragment, container, false);

        binding.audioFileList.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));

        context = binding.getRoot().getContext();

        binding.audioFileList.setAdapter(new AudioFileListAdapter(this.musicList, myListener));
        return binding.getRoot();
    }


    public void setMusicList(List<AudioFile> musicList) {
        this.musicList = musicList;

    }

    public void setControl(ListListener listener) {
        myListener = listener;
    };

    public AudioFile nextSong(int position) {
        return this.musicList.get(position + 1);
    }

    public AudioFile prevSong(int position) {
        return this.musicList.get(position - 1);
    }

}

