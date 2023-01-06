package com.example.oasis.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.oasis.interfaces.ListListener;
import com.example.oasis.models.AudioFile;
import com.example.oasis.utils.AsyncMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlayerService extends Service implements MediaPlayer.OnErrorListener {

    private final Binder binder = new PlayerBinder();
    private List<AudioFile> musicList;
    private List<String> musicArtistes = new ArrayList<>();
    private List<String> similarArtistes = new ArrayList<>();
    private List<String> musicGenres = new ArrayList<>();
    private List<String> musicAlbums = new ArrayList<>();
    private AudioFile currentMusic = null;
    private MediaPlayer mediaPlayer;
    private WebService webService;
    private int position;
    ListListener listListener;

    public void setControl(ListListener listener) {
        listListener = listener;
    };

    public List<String> getMusicArtistes() {
        return musicArtistes;
    }

    public List<String> getSimilarArtistes() {
        return similarArtistes;
    }

    public List<String> getMusicGenres() {
        return musicGenres;
    }

    public List<String> getMusicAlbums() {
        return musicAlbums;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY_COMPATIBILITY;
    }

    public void play(String path) throws IOException {
        try {
            if (currentMusic != null) {
                AudioFile result = this.webService.fetchMusic(currentMusic);
                if (result.getFilePath() != null) {
                    musicList.get(musicList.indexOf(currentMusic)).setImagePath(result.getImagePath());
                }
            }
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            } else {
                mediaPlayer = new MediaPlayer();
            }

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer preparedPlayer) {
                    preparedPlayer.start();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    //player.reset();
                    try {
                        listListener.selectSong(musicList.get(musicList.indexOf(currentMusic) + 1), musicList.indexOf(currentMusic) + 1)    ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
            mediaPlayer.start();
        }
    }

    public int getCurrentTime() {
        return mediaPlayer.getCurrentPosition();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            position = mediaPlayer.getCurrentPosition();
        }
    }

    public void setListCategories(List<AudioFile> musicList) {
        for (int i = 0; i < musicList.size(); i++) {

            if (musicList.get(i).getGenre() != null)
                this.musicGenres.add(musicList.get(i).getGenre());

            if (!musicList.get(i).getArtist().equals("<unknown>"))
                this.musicArtistes.add(musicList.get(i).getArtist());

            if (!musicList.get(i).getAlbum().equals("<unknown>"))
                this.musicAlbums.add(musicList.get(i).getAlbum());
        }
        for(String artist: this.musicArtistes) {
            List<String> result =  webService.fetchSimilarArtistes(artist);
           this.similarArtistes.addAll(result);
        }
    }

    public void setMusicList(List<AudioFile> musicList) {
        this.musicList = musicList;
    }

    public void setCurrentSong(AudioFile audioFile) {
        this.currentMusic = audioFile;
    }

    public AudioFile getCurrentMusic() {
        return this.currentMusic;
    }

    public void setWebService(WebService webService) {
        this.webService = webService;
        this.musicList = webService.fetchAllMusic(this.musicList);
        this.setListCategories(musicList);

//        try {
//            // this.musicList = (List<AudioFile>) new AsyncMethods().execute(webService.fetchAllMusic(this.musicList)).get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}