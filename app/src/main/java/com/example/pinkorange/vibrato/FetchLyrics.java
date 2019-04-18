package com.example.pinkorange.vibrato;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchLyrics {
    private String API_KEY = "ca280d47ab2f9b5409ff36d18071383e";
    private String title, artist;
    public int trackId = 123;
    private Context context;
    Boolean trackFound;

    FetchLyrics(String SongTitle, String SongArtist, Context context){
        this.title = SongTitle;
        this.artist = SongArtist;
        this.context = context;
        this.trackFound = false;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void findTrackId(){
        String url = "https://api.musixmatch.com/ws/1.1/";
        url = url + "track.search?q_track=" + this.title;
        url = url + (this.artist.equals("Unknown Artist")? "" : "&q_artist=" + this.artist);
        url = url + "&apikey=" + API_KEY;
        RequestQueue queue = Volley.newRequestQueue(this.context);
        Log.d("URL!!!!!", url +"");
        JsonObjectRequest jor = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject message = response.getJSONObject("message");
                            JSONObject body = message.getJSONObject("body");
                            JSONArray track_list = body.getJSONArray("track_list");
                            JSONObject object = track_list.getJSONObject(0);
                            JSONObject track = object.getJSONObject("track");
                            trackId = track.getInt("track_id");
                            trackFound = true;
                            Log.d("Inside!!!!", "I am here");
                        } catch(JSONException e){
                            Log.d("JSON error", "wtf!");
                            e.printStackTrace();
                        }

                        Log.d("Track Id!!!!!", trackId+ "");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        trackId = 0;

                    }
                });
        queue.add(jor);

    }

}
