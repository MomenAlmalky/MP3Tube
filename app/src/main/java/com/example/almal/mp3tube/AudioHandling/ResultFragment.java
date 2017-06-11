package com.example.almal.mp3tube.AudioHandling;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.VideoInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnResultInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RequestQueue requestQueue;
    ArrayList <VideoInfo> videoInfos= new ArrayList<VideoInfo>();



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnResultInteractionListener mListener;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onResultInteraction("buttonBack","clicked");
        }
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResultInteractionListener) {
            mListener = (OnResultInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void search(String searchword){
        videoInfos.clear();
        requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&type=video&q="+searchword+"&key=AIzaSyAsIU3kmaiwxqnEbtI0IvvdVCxxNixbE6c";
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();

                    jsonArray = (JSONArray) response.get("items");
                    Log.i("response",jsonObject.toString());
                    for(int i = 0; i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String title = ((JSONObject) jsonObject.get("snippet")).getString("title");
                        String info = ((JSONObject) jsonObject.get("snippet")).getString("channelTitle");
                        String imageurl = ((JSONObject)((JSONObject)((JSONObject) jsonObject.get("snippet")).get("thumbnails")).get("default")).getString("url");
                        String videourl = ((JSONObject) jsonObject.get("id")).getString("videoId");
                        VideoInfo videoInfo = new VideoInfo(title,info,imageurl,videourl,getContext());
                        Log.i("VideoInfo "+i+"= ",videoInfo.toString());
                        videoInfos.add(videoInfo);
                    }

                    RecyclerView rv = (RecyclerView) getView().findViewById(R.id.recycler_view_result_fragment);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    rv.setLayoutManager(llm);

                    RecyclerViewAdapter RA = new RecyclerViewAdapter(videoInfos, new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(VideoInfo item) {
                            Log.i("playsong",item.getTitle());
                            mListener.onResultInteraction("play",item);

                        }
                    });
                    rv.setAdapter(RA);
                    RA.notifyDataSetChanged();
                    Log.i("response",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnResultInteractionListener {
        // TODO: Update argument type and name
        void onResultInteraction(String action , VideoInfo videoInfo);
    }
}
