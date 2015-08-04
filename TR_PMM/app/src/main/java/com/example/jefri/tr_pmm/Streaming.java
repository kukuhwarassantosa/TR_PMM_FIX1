package com.example.jefri.tr_pmm;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Streaming extends ActionBarActivity {

    // Declare variables
    private static final String TAG_D1 = "d1";
    private static final String TAG_DIR = "dir";
    private static final String TAG_FILES = "files";
    private static final String TAG_NAME = "name";

    JSONArray cekVideo = null;
    private String ident, url;

    ProgressDialog pDialog;
    VideoView videoview;

    String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        // Find your VideoView in your video_main.xml layout
        videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask

        Bundle bun = getIntent().getExtras();
        ident = bun.getString("ident");
        url = "https://archive.org/metadata/" + ident;
        Log.i("Test : ", url);

        new getVideo().execute();
    }

    class getVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
            pDialog = new ProgressDialog(Streaming.this);
            // Set progressbar title
            pDialog.setTitle("Android Video Streaming");
            // Set progressbar message
            pDialog.setMessage("Buffering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            // Show progressbar
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String vidAddress = "";
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

                    vidAddress = d1 + dir + "/" + name;
                    Log.i("Test URL Video : ", vidAddress);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("Service Handler", "Couldn't get any data from the url");
            }
            Log.i("Test URL Video 2 : ", vidAddress);
            VideoURL = "http://"+vidAddress;
            Log.i("Test URL Video 3 : ", VideoURL);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(
                        Streaming.this);
                mediacontroller.setAnchorView(videoview);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(VideoURL);
                videoview.setMediaController(mediacontroller);
                videoview.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            videoview.requestFocus();
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    pDialog.dismiss();
                    videoview.start();
                }
            });
        }
    }
}
