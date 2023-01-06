package com.example.oasis.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.oasis.R;
import com.example.oasis.databinding.ControlFileFragmentBinding;
import com.example.oasis.interfaces.ControlListener;
import com.example.oasis.models.AudioFile;
import com.example.oasis.utils.DownloadImageTask;
import com.example.oasis.utils.OnSwipeTouch;
import com.example.oasis.utils.SwipeEnum;

import java.io.IOException;

public class ControlFragment extends Fragment {

    private ControlListener myListener;
    private Boolean isToggle = false;
    private ImageView btnPLay;
    private LinearLayout nav_bar_song;
    private SeekBar progress_bar;
    private FrameLayout nav_bar_frame_layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ControlFileFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.control_file_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nav_bar_song = (LinearLayout) getView().findViewById(R.id.nav_bar_song);
        progress_bar = (SeekBar) getView().findViewById(R.id.seekBar);
        this.setWeightProgressBar(0, 100);

        btnPLay = (ImageView) getView().findViewById(R.id.button_play);
        this.noSongSelected();
//        nav_bar_song.setVisibility(View.INVISIBLE);

        btnPLay.setOnClickListener(myListenerBtn);
        nav_bar_song.setOnTouchListener(myListenerSwip);
    }

    private OnSwipeTouch myListenerSwip = new OnSwipeTouch(getContext()) {
        public void onSwipe(SwipeEnum direction) {
            switch (direction) {
                case LEFT:
                case RIGHT:
                    try {
                        myListener.changeSong(direction == SwipeEnum.LEFT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    public void resetPlayImg() {
        btnPLay.setImageResource(R.drawable.play);
        isToggle = false;
        this.setWeightProgressBar(0, 100);
    }

    public void setWeightProgressBar(Integer value, Integer max) {
        this.progress_bar.setProgress(value);
        this.progress_bar.setMax(max);
    }

    private View.OnClickListener myListenerBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isToggle = !isToggle;
            if (!isToggle) {
                Log.e("PLAY", "play song");
                myListener.playSong();
                btnPLay.setImageResource(R.drawable.play);
            } else {
                Log.e("PAUSE", "pause song");
                myListener.pauseSong();
                btnPLay.setImageResource(R.drawable.pause);
            }
        }
    };

    public void currentSong(AudioFile audioFile) {
        btnPLay.setVisibility(View.VISIBLE);
        nav_bar_song.setVisibility(View.VISIBLE);
        TextView titleSong = getView().findViewById(R.id.title);
        titleSong.setText(audioFile.getTitle());
        ImageView image = getView().findViewById(R.id.song);
        if(audioFile.getImagePath() != null) {
            new DownloadImageTask(image).execute(audioFile.getImagePath());
        }else {
            image.setImageDrawable(getResources().getDrawable(R.drawable.song));
        }
    }

    public void setControl(ControlListener listener) {
        myListener = listener;
    }

    public void noSongSelected() {
        TextView titleSong = getView().findViewById(R.id.title);
        titleSong.setText("Aucune musique sélectionnée");
        btnPLay.setVisibility(View.INVISIBLE);
    }
}
