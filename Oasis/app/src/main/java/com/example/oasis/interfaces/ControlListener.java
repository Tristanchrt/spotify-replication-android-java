package com.example.oasis.interfaces;

import java.io.IOException;

public interface ControlListener {
    public void pauseSong();

    public void playSong();

    public void changeSong(boolean isPrevious) throws IOException;
}
