package com.example.jefri.tr_pmm;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivityPhysics extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get atribut of VIDEO, JSON
    //private static String url = "http://archive.org/advancedsearch.php?q=more_animation&fl[]=date&fl[]=size&fl[]=format&fl[]=identifier&fl[]=mediatype&fl[]=title&sort[]=createdate+desc&sort[]=&sort[]=&rows=10&page=1&output=json&callback=callback&save=yes";
    private static String url = "http://archive.org/advancedsearch.php?q=physics%20video&fl[]=mediatype&fl[]=title&sort[]=&sort[]=&sort[]=&rows=10&page=1&indent=yes&output=json";
    //private static String url = "http://api.androidhive.info/contacts/";

    //JSON Node names
    private static final String TAG_RESPONSE = "response";
    private static final String TAG_DOCS = "docs";

    private static final String TAG_TITLE = "title";
    private static final String TAG_MEDIATYPE = "mediatype";
    private static final String TAG_DURATION = "";
    private static final String TAG_SIZE = "";
    private static final String TAG_IDENTIFIER = "identifier";

    EditText search;
    ListAdapter adapter;

    // contacts JSONArray
    JSONArray videos = null;

    // Hasmap for listview
    ArrayList<HashMap<String, String>> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_physics);

        videoList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem


                String size = ((TextView) view.findViewById(R.id.text_size_video)).getText().toString();
                String identifier = ((TextView) view.findViewById(R.id.tv_identifier)).getText().toString();

//                Toast.makeText(getApplicationContext(), "Coba : "
//                                + name + " | "
//                                + format + " | "
//                                + durasi + " | "
//                                + size + " | "
//                                + identifier,
//                        Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(), Streaming.class);
                in.putExtra("ident", identifier);
                startActivity(in);
            }
        });

          new GetVideos().execute();
    }

    private class GetVideos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivityPhysics.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject(TAG_RESPONSE);
                    // Getting Json Array node

                    videos = jsonObject1.getJSONArray(TAG_DOCS);

                    // looping through All Contacts
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject v = videos.getJSONObject(i);

                        String title = "Unknown", format = "Unknown";

                        if (v.has(TAG_TITLE)) title = v.getString(TAG_TITLE);
                        if (v.has(TAG_MEDIATYPE)) format = v.getString(TAG_MEDIATYPE);

                        // tmp hashmap for single video
                        HashMap<String, String> video = new HashMap<String, String>();

                        // adding each child node to hashmap key => value
                        video.put(TAG_TITLE, title);
                        video.put(TAG_MEDIATYPE, format);

                        // addding video to video list
                        videoList.add(video);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing())
                pDialog.dismiss();
            // Dismiss the progress)
            adapter = new SimpleAdapter(
                    MainActivityPhysics.this, videoList,
                    R.layout.listview,
                    new String[]{TAG_TITLE, TAG_MEDIATYPE, TAG_DURATION, TAG_SIZE, TAG_IDENTIFIER},
                    new int[]{
                           R.id.text_size_video,
                            R.id.tv_identifier});
            setListAdapter(adapter);
        }
    }
}
