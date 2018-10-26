package com.example.almal.mp3tube.ui.AudioHandling.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.ui.AudioHandling.Profile.likes.LikeFragment;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.example.almal.mp3tube.utilities.LikesRecyclerViewAdapter;
import com.example.almal.mp3tube.utilities.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnHistoryInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ProfileContract.View {

    RecyclerView rv;
    LikesRecyclerViewAdapter likesRecyclerViewAdapter;
    CardView likesCv,playlistCv,historyCv;
    ImageView rvArrow;
    LikeFragment likeFragment;
    ProfilePresenter profilePresenter;
    Boolean expanded;

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
        expanded = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        likesCv = (CardView) view.findViewById(R.id.profile_like_cv);
        playlistCv = (CardView) view.findViewById(R.id.profile_playlist_cv);
        historyCv = (CardView) view.findViewById(R.id.profile_history_cv);
        rvArrow = (ImageView) view.findViewById(R.id.profile_history_arrow);

        rv = (RecyclerView) view.findViewById(R.id.profile_history_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);


        historyCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expanded){
                    expanded = false;
                    TransitionManager.beginDelayedTransition(rv);
                    rv.setVisibility(View.GONE);
                    rvArrow.setBackgroundResource(R.drawable.icons8_expand_arrow_100);
                }
                else {
                    expanded = true;
                    TransitionManager.beginDelayedTransition(rv);
                    rv.setVisibility(View.VISIBLE);
                    rvArrow.setBackgroundResource(R.drawable.icons8_arrow_up);
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        likeFragment = LikeFragment.newInstance();
        likesCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, likeFragment).commitNow();
            }
        });

        profilePresenter.getHistory();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        profilePresenter = new ProfilePresenter(DataManager.getInstance(), getActivity());
        profilePresenter.attachView(this);

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
        profilePresenter.detachView();
    }


    @Override
    public void showHistory(List<FirebaseTracks> firebaseTracks) {
        likesRecyclerViewAdapter = new LikesRecyclerViewAdapter(getActivity(), firebaseTracks, new LikesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FirebaseTracks firebaseTrack) {
                playmusic(firebaseTrack);
            }
        });
        rv.setAdapter(likesRecyclerViewAdapter);
        likesRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void playmusic(FirebaseTracks firebaseTrack) {
        if (mListener != null){
            mListener.onHistoryInteraction(GlobalEntities.PLAY_TAG, firebaseTrack);
        }
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
        void onHistoryInteraction(String tag, FirebaseTracks firebaseTrack);
    }
}
