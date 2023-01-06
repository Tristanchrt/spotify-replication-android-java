package com.example.oasis.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.oasis.models.AudioFile;
import com.example.oasis.models.SArtist;
import com.example.oasis.models.Similar;
import com.example.oasis.models.SimilarArtist;
import com.example.oasis.models.SimilarArtistResponse;
import com.example.oasis.models.Track;
import com.example.oasis.models.TrackImage;
import com.example.oasis.models.TrackResponse;
import com.example.oasis.models.WebParameters;
import com.example.oasis.utils.Config;
import com.example.oasis.utils.HttpProcotolEnum;
import com.example.oasis.utils.RequestGet;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebService extends Service {

    private final Binder binder = new WebBinder();
    private String baseUrl = Config.BaseUrl;

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY_COMPATIBILITY;
    }

    protected JSONObject request(String fullUrl, HttpProcotolEnum httpProtocol) {
        try {
            switch (httpProtocol) {
                case GET: {
                    JSONObject result = new RequestGet().execute(fullUrl).get();
                    return result;
                }
                case POST: {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AudioFile> fetchAllMusic(List<AudioFile> musicList) {
        for (AudioFile music : musicList) {
            List<WebParameters> webParameters = new ArrayList<WebParameters>(Config.webParameters);

            webParameters.add(new WebParameters("artist", music.getArtist()));
            webParameters.add(new WebParameters("track", music.getTitle()));
            String url = buildUrl("track.getInfo", webParameters);
            if (!url.isEmpty()) {
                String formatedUrl = url.replace(" ", "%20");
                Log.e("URL", formatedUrl);
                Track track = getTrack(formatedUrl);
                if (track != null) {
                    if (track.getAlbum() != null) {
                        if (!track.getAlbum().getImage().isEmpty()) {
                            for (TrackImage image : track.getAlbum().getImage()) {
                                music.setImagePath(image.getText());
                                Log.e("IMAGE", image.getText());
                            }
                        }
                    }
                }
            }
        }
        return musicList;
    }

    public AudioFile fetchMusic(AudioFile music) {
        List<WebParameters> webParameters = new ArrayList<WebParameters>(Config.webParameters);

        webParameters.add(new WebParameters("artist", music.getArtist()));
        webParameters.add(new WebParameters("track", music.getTitle()));
        String url = buildUrl("track.getInfo", webParameters);
        if (!url.isEmpty()) {
            String formatedUrl = url.replace(" ", "%20");
            Log.e("URL", formatedUrl);
            Track track = getTrack(formatedUrl);
            if (track != null) {
                if (track.getAlbum() != null) {
                    if (!track.getAlbum().getImage().isEmpty()) {
                        for (TrackImage image : track.getAlbum().getImage()) {
                            music.setImagePath(image.getText());
                            Log.e("IMAGE", image.getText());
                        }
                    }
                }
            }
        }
        return music;
    }

    public List<String> fetchSimilarArtistes(String artistName) {
        List<String> similar = new ArrayList<>();
        List<WebParameters> webParameters = new ArrayList<WebParameters>(Config.webParameters);
        webParameters.add(new WebParameters("artist", artistName));
        String url = buildUrl("artist.getInfo", webParameters);
        SimilarArtist similarArtist = getSimilarArtist(url);
        if (similarArtist != null) {
            if (similarArtist.getSimilar() != null) {
                if (!similarArtist.getSimilar().getArtist().isEmpty()) {
                    for (SArtist artist : similarArtist.getSimilar().getArtist()) {
                        similar.add(artist.getName());
                    }
                }
            }
        }
        return similar;
    }

    private String buildUrl(String method, List<WebParameters> params) {
        String fullUrl = this.baseUrl.concat(method);
        for (WebParameters param : params) {
            fullUrl = fullUrl.concat("&" + param.getKey() + "=" + param.getValue());
        }
        return fullUrl.contains("<unknown>") ? "" : fullUrl;
    }

    public Track getTrack(String url) {
        try {
            JSONObject result = request(url, HttpProcotolEnum.GET);
            ObjectMapper m = new ObjectMapper();
            TrackResponse track = m.readValue(result.toString(), TrackResponse.class);
            return track.getTrack();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SimilarArtist getSimilarArtist(String url) {
        try {
            JSONObject result = request(url, HttpProcotolEnum.GET);
            ObjectMapper m = new ObjectMapper();
            SimilarArtistResponse similarArtistResponse = m.readValue(result.toString(), SimilarArtistResponse.class);
            return similarArtistResponse.getArtist();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class WebBinder extends Binder {
        public WebService getService() {
            return WebService.this;
        }
    }
}
