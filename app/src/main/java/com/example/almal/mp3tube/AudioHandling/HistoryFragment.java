package com.example.almal.mp3tube.AudioHandling;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.VideoInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnHistoryInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String sharedTag = "com.example.almal.mp3tube";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnHistoryInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = this.getActivity().getSharedPreferences(sharedTag,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("videoInfo","");
        Type type = new TypeToken<List<VideoInfo>>(){}.getType();
        List<VideoInfo> videoHistoryList= gson.fromJson(json, type);

        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.recycler_view_history_fragment);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        RecyclerViewAdapter RA = new RecyclerViewAdapter(videoHistoryList, new RecyclerViewAdapter.OnItemClickListener() {  @Override
        public void onItemClick(VideoInfo item) {
            Log.i("playsong",item.getTitle());
            mListener.onHistoryInteraction("play",item);

        }
        });
        rv.setAdapter(RA);

        }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryInteractionListener) {
            mListener = (OnHistoryInteractionListener) context;
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
    public interface OnHistoryInteractionListener {
        // TODO: Update argument type and name
        void onHistoryInteraction(String action,VideoInfo videoInfo);
    }
}
