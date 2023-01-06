package com.example.oasis.activities;

import static android.content.Context.BIND_AUTO_CREATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.oasis.R;
import com.example.oasis.databinding.ActivityMainBinding;
import com.example.oasis.fragments.AudioFileListFragment;
import com.example.oasis.fragments.ControlFragment;
import com.example.oasis.fragments.SearchBarFragment;
import com.example.oasis.interfaces.ControlListener;
import com.example.oasis.interfaces.ListListener;
import com.example.oasis.interfaces.SearchBarListener;
import com.example.oasis.models.AudioFile;
import com.example.oasis.notification.CreateNotification;
import com.example.oasis.services.PlayerService;
import com.example.oasis.services.WebService;
import com.example.oasis.utils.Config;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.LongBinaryOperator;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 123456;
    private ActivityMainBinding binding;
    private Cursor musicTracksCursor;
    private Integer currentPosition;
    private ControlFragment controlFragment;
    private AudioFileListFragment audioFragment;
    private SearchBarFragment searchBarFragment;
    private List<AudioFile> musicList;
    PlayerService playerService;
    WebService webService;
    private Boolean isPause = false;
    private Integer currentTimeSong = 0;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        startWebService();
        checkUserPermission();
        getAllMusicTracks();
        musicList = getAudioList();
        startMediaPlayerService();
        showNotification();
        showStartup();
    }

    private void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID, "Oasis Music", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showStartup() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        audioFragment = new AudioFileListFragment();
        controlFragment = new ControlFragment();
        searchBarFragment = new SearchBarFragment();

        audioFragment.setMusicList(this.musicList);
        transaction.replace(R.id.fragment_container, audioFragment);
        transaction.replace(R.id.control_container, controlFragment);
        transaction.replace(R.id.search_bar_input, searchBarFragment);

        controlFragment.setControl(controlListener);
        searchBarFragment.setControl(searchBarListener);
        audioFragment.setControl(listListener);

        transaction.commit();
    }

    private void startMediaPlayerService() {
        MainActivity activity = this;
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                playerService = ((PlayerService.PlayerBinder) iBinder).getService();
                playerService.setMusicList(activity.musicList);
                playerService.setControl(listListener);
                threadWebService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                //playerService = null;
            }
        };
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void checkUserPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }

    private void startWebService() {
        MainActivity activity = this;
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                webService = ((WebService.WebBinder) iBinder).getService();
//                activity.musicList = webService.fetchAllMusic(activity.musicList);
                // fetch musiques
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                //webService = null;
            }
        };
        Intent intent = new Intent(this, WebService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void getAllMusicTracks() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.YEAR
        };
        //chemin du fichier, ID, titre, artist

        this.musicTracksCursor = getContentResolver().query(uri, projection, null, null, null);
        Log.e("ITEMS : ", "" + this.musicTracksCursor.getCount());
        //path peut être utilisé directement avec Mediaplayer.setDataSource(path)
        // String path = cursor.getString(0);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
                // User refused to grant permission.
            }
        }
    }


    @SuppressLint("Range")
    public List<AudioFile> getAudioList() {
        List<AudioFile> audioList = new ArrayList<>();
        if (isMusicTracksCursorExist()) {
            Cursor cursor = getMusicTracksCursor();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                AudioFile audio = new AudioFile();
                audio.setFilePath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                audio.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                audio.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                audio.setDuration(Math.round(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) / 1000));
                audio.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                audio.setYear(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
                audioList.add(audio);
            }
            cursor.close();

            return audioList;
        }
        return null;
    }

    public boolean isMusicTracksCursorExist() {
        return this.musicTracksCursor != null;
    }

    public Cursor getMusicTracksCursor() {
        return this.musicTracksCursor;
    }

    public SearchBarListener searchBarListener = new SearchBarListener() {
        @Override
        public void filterSearch(String s) {
            List<AudioFile> newMusicList = new ArrayList<>();
            for (int i = 0; i < musicList.size(); i++) {
                if (musicList.get(i).getTitle().toLowerCase().contains(s.toLowerCase())) {
                    newMusicList.add(musicList.get(i));
                }
            }
            Log.e("MUSIC LIST SEARCH", "SEARCH LIST : " + newMusicList);
            audioFragment.setMusicList(newMusicList);
            audioFragment.getFragmentManager().beginTransaction().detach(audioFragment).commit();
            audioFragment.getFragmentManager().beginTransaction().attach(audioFragment).commit();
        }

        @Override
        public void openListPage() {
            Log.e("OPEN NEW PAGE", "NEW PAGE");
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("artiste_list", (Serializable) playerService.getMusicArtistes());
            intent.putExtra("genre_list", (Serializable) playerService.getMusicGenres());
            intent.putExtra("album_list", (Serializable) playerService.getMusicAlbums());
            intent.putExtra("similar_list", (Serializable) playerService.getSimilarArtistes());

            startActivity(intent);
        }
    };

    public ControlListener controlListener = new ControlListener() {
        @Override
        public void pauseSong() {
            isPause = true;
            playerService.stop();
        }

        @Override
        public void playSong() {
            isPause = false;
            playerService.resume();
        }

        @Override
        public void changeSong(boolean isPrevious) throws IOException {
            AudioFile audioFile = isPrevious ? audioFragment.nextSong(currentPosition) : audioFragment.prevSong(currentPosition);
            currentPosition = isPrevious ? currentPosition + 1 : currentPosition - 1;
            listListener.selectSong(audioFile, currentPosition);
        }
    };

    public ListListener listListener = new ListListener() {
        @Override
        public void selectSong(AudioFile audio, int position) throws IOException {
            currentPosition = position;
            controlFragment.currentSong(audio);
            controlFragment.resetPlayImg();
            playerService.setCurrentSong(audio);
            playerService.play(audio.getFilePath());
            MainActivity.this.threadProgressBar();
//            MainActivity.this.threadNotif();
        }
    };

    public void createNotification(AudioFile audioFile) {
        CreateNotification.createNotification(this, audioFile, R.drawable.play, 1, musicList.size() - 1);
    }

    public void threadProgressBar() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    controlFragment.setWeightProgressBar((playerService.getCurrentTime() / 1000), playerService.getCurrentMusic().getDuration());
                    // createNotification(audio);
                });
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    public void threadNotif() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    createNotification(playerService.getCurrentMusic());
                });
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    public void threadWebService() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                playerService.setWebService(MainActivity.this.webService);
            }
        });
        t.start();
    }
}

