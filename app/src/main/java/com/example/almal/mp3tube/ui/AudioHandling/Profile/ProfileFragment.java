package com.example.almal.mp3tube.ui.AudioHandling.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.ui.AudioHandling.Profile.likes.LikeFragment;
import com.example.almal.mp3tube.utilities.RecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnHistoryInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    ArrayList<VideoInfo> videoInfoArrayList = new ArrayList<>();
    RecyclerView rv;
    RecyclerViewAdapter RA;
    CardView likescv;
    LikeFragment likeFragment;

    private OnHistoryInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        likeFragment = LikeFragment.newInstance();
        likescv = (CardView) getView().findViewById(R.id.profile_like_cv);

        likescv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,likeFragment).commitNow();
            }
        });
        rv = (RecyclerView) getView().findViewById(R.id.profile_history_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        Log.i("sharedvideo",videoInfoArrayList.toString());

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



    public void getHistory(ArrayList<VideoInfo> videoInfosArray){


    }

    public void getUpdated(ArrayList<VideoInfo> videoInfosArray) {
        videoInfoArrayList.clear();
        videoInfoArrayList.addAll(videoInfosArray);
        RA.notifyDataSetChanged();
        rv.invalidate();
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
        void onHistoryInteraction(String action, VideoInfo videoInfo);
    }
}
