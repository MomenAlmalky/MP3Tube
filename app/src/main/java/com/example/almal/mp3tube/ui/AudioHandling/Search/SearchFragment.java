package com.example.almal.mp3tube.ui.AudioHandling.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.Snippet;
import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.example.almal.mp3tube.utilities.LikesRecyclerViewAdapter;
import com.example.almal.mp3tube.utilities.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnResultInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchContract.View {

    private SearchPresenter mSearchPresenter;
    RecyclerView rv;
    EditText searchEditText;



    private OnResultInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
/*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onResultInteraction("buttonBack","clicked");
        }
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }


    public void init() {

        mSearchPresenter = new SearchPresenter(DataManager.getInstance(), getActivity());
        mSearchPresenter.attachView(this);

        rv = (RecyclerView) getView().findViewById(R.id.recycler_view_result_fragment);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        searchSong(null,"song");
        //when button clicked
        Button searchButton = (Button) getView().findViewById(R.id.search_fragment_search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(GlobalEntities.AudioHandling_ACTIVITY, "searchButton: isclicked");
                String text = searchEditText.getText().toString();
                searchSong(view,text);
            }
        });

        //when enter clicked on keyboard
        searchEditText = (EditText) getView().findViewById(R.id.search_fragment_search_et);
        searchEditText.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    String text = searchEditText.getText().toString();
                    searchSong(view,text);
                    return true;
                }
                return false;
            }
        });


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
        mSearchPresenter.detachView();
    }

    @Override
    public void showResults(List<Item> itemList) {
        ArrayList<FirebaseTracks> firebaseTracksArrayList = new ArrayList<>();
        for(Item item:itemList){
            FirebaseTracks firebaseTrack = new FirebaseTracks();
            firebaseTrack.setTrack_id(item.getId().getVideoId());
            firebaseTrack.setTrack_title(item.getSnippet().getTitle());
            firebaseTrack.setTrack_author(item.getSnippet().getChannelTitle());
            firebaseTrack.setTrack_image(item.getSnippet().getThumbnails().getHigh().getUrl());
            firebaseTracksArrayList.add(firebaseTrack);
        }


        LikesRecyclerViewAdapter RA = new LikesRecyclerViewAdapter(getActivity(), firebaseTracksArrayList, new LikesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FirebaseTracks firebaseTracks) {
                mListener.onResultInteraction(GlobalEntities.PLAY_TAG, firebaseTracks);
            }
        });

        rv.setAdapter(RA);
        RA.notifyDataSetChanged();

    }

    public void searchSongAPICall(String text) {

    }

    //to search for specific song
    public void searchSong(View view,String text) {
        text.trim();
        if (!text.isEmpty()) {
            View keyboard = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            mSearchPresenter.search_youtube_API(text);
        } else {
            Toast.makeText(getContext(), "You must enter a word to start search", Toast.LENGTH_LONG).show();
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
    public interface OnResultInteractionListener {
        // TODO: Update argument type and name
        void onResultInteraction(String action, FirebaseTracks firebaseTracks);
    }
}
