package com.example.oasis.interfaces;

import com.example.oasis.models.AudioFile;

import java.io.IOException;

public interface ListListener {
    public void selectSong(AudioFile audio, int position) throws IOException;
}
