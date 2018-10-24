package com.example.almal.mp3tube.ui.AudioHandling.Profile.likes;

import android.content.Context;
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
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.FirebaseTracks;

import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.example.almal.mp3tube.utilities.LikesRecyclerViewAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link LikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikeFragment extends Fragment implements LikeContract.View{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters
    private LikePresenter likePresenter;
    private OnFirebaseLikeListener mListener;
    private RecyclerView rv;

    public LikeFragment() {
        // Required empty public constructor
    }

    public static LikeFragment newInstance() {
        LikeFragment fragment = new LikeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_like, container, false);

        rv = (RecyclerView) view.findViewById(R.id.like_fragment_recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        likePresenter = new LikePresenter(DataManager.getInstance(),getActivity());
        likePresenter.attachView(this);

        if (context instanceof OnFirebaseLikeListener) {
            mListener = (OnFirebaseLikeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        likePresenter.getLikes();
    }
        @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        likePresenter.detachView();
    }


    @Override
    public void showLikes(List<FirebaseTracks> itemsList) {
        if(itemsList !=null){
            LikesRecyclerViewAdapter RA = new LikesRecyclerViewAdapter(getActivity(),itemsList, new LikesRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(FirebaseTracks firebaseTrack) {
                    playmusic(firebaseTrack);
                }
            });

            rv.setAdapter(RA);
            RA.notifyDataSetChanged();


        }

    }

    @Override
    public void playmusic(FirebaseTracks firebaseTrack) {
        if (mListener != null){
            mListener.OnFirebaseLikePlay(GlobalEntities.PLAY_TAG, firebaseTrack);
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
    public interface OnFirebaseLikeListener {
        // TODO: Update argument type and name
        void OnFirebaseLikePlay(String tag,FirebaseTracks firebaseTrack);
    }
}
