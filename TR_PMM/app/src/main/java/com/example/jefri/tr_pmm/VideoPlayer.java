package com.example.jefri.tr_pmm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoPlayer extends Activity implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener {

    private static final String TAG_D1 = "d1";
    private static final String TAG_DIR = "dir";
    private static final String TAG_FILES = "files";
    private static final String TAG_NAME = "name";

    JSONArray cekVideo = null;
    private ProgressDialog pDialog;

    private String ident, url;

    private MediaPlayer mediaPlayer;
    private SurfaceHolder vidHolder;
    private SurfaceView vidSurface;

    private String vidAdress = "http://archive.org/download/popeye_taxi-turvey"
            + "/popeye_taxi-turvey_512kb.mp4";

    ImageButton play_video, pause_video, stop_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);

        vidSurface = (SurfaceView) findViewById(R.id.videoView1);
        play_video = (ImageButton) findViewById(R.id.media_play);
        pause_video = (ImageButton) findViewById(R.id.media_pause);
        stop_video = (ImageButton) findViewById(R.id.media_stop);

        Bundle bun = getIntent().getExtras();
        ident = bun.getString("ident");
        url = "https://archive.org/metadata/" + ident;

        play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        pause_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        stop_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (Exception e) {
                }
            }
        });

        vidHolder = vidSurface.getHolder();

        new GetURLVideo().execute();
        vidHolder.addCallback(this);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(vidHolder);
            mediaPlayer.setDataSource(vidAdress);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    private class GetURLVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VideoPlayer.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response : ", " > " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    String d1 = "Unknown", dir = "Unknown", file = "Unknown", name = "Unknown";
                    if (jsonObject.has(TAG_D1)) d1 = jsonObject.getString(TAG_D1);
                    if (jsonObject.has(TAG_DIR)) dir = jsonObject.getString(TAG_DIR);
                    //JSONObject jsonObject1 = jsonObject.getJSONObject(TAG_D1);
                    //JSONObject jsonObject1 = jsonObject.getJSONObject(TAG_FILES);
                    cekVideo = jsonObject.getJSONArray(TAG_FILES);

                    for (int i = 0; i < cekVideo.length(); i++) {
                        JSONObject cek = cekVideo.getJSONObject(i);

                        if (cek.getString(TAG_NAME).contains(".mp4")) {
                            name = cek.getString(TAG_NAME);
                            break;
                        }
                    }

                    vidAdress = d1 + dir + "/" + name;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("Service Handler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing()) pDialog.dismiss();

            mediaPlayer.start();
        }
    }

}
