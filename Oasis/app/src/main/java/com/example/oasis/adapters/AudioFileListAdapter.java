package com.example.oasis.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oasis.databinding.AudioFileItemBinding;
import com.example.oasis.interfaces.ListListener;
import com.example.oasis.viewmodels.AudioFileViewModel;
import com.example.oasis.R;
import com.example.oasis.models.AudioFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioFileListAdapter extends
        RecyclerView.Adapter<AudioFileListAdapter.ViewHolder> {
    List<AudioFile> audioFileList = new ArrayList<>();
    ListListener listListener = null;
    private int positionList;

    public AudioFileListAdapter(List<AudioFile> fileList, ListListener listListener) {
        audioFileList = fileList;
        this.listListener = listListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AudioFileItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.audio_file_item, parent, false);
        return new ViewHolder(binding, listListener);
    } 

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioFile file = audioFileList.get(position);
        holder.viewModel.setAudioFile(file);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return audioFileList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private AudioFileItemBinding binding;
        private AudioFileViewModel viewModel = new AudioFileViewModel();
        private int position;

        ViewHolder(AudioFileItemBinding binding, ListListener listListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAudioFileViewModel(viewModel);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        listListener.selectSong(binding.getAudioFileViewModel().getAudioFile(), position);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}