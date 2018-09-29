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
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.Snippet;
import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;
import com.example.almal.mp3tube.utilities.GlobalEntities;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RequestQueue requestQueue;
    ArrayList<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
    private SearchPresenter mSearchPresenter;
    RecyclerView rv;
    EditText searchEditText;
    Button searchButton;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnResultInteractionListener mListener;

    public SearchFragment() {
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
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
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
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        /*
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.song_play);
        SearchView searchView = new SearchView(((AudioHandlingActivity) getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        *//*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (waitUsersAdapter != null){
                    waitUsersAdapter.getFilter().filter(query);
                    waitUsersAdapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("hereismenu","clicked");
                if (waitUsersAdapter != null){
                    newText = newText.toString().toLowerCase();

                    final List<UserEvents> filteredList = new ArrayList<>();

                    for (int i = 0; i < users.size(); i++) {

                        final String text = users.get(i).getName().toLowerCase();
                        if (text.contains(newText)) {

                            filteredList.add(users.get(i));
                        }
                    }

                    waitUsersAdapter = new WaitUsersAdapter(filteredList, new WaitUsersAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(UserEvents item) {
                            startActivity(VerifyNumberActivity.getStartIntent(getActivity()));
                        }
                    });

                    recyclerView.setAdapter(waitUsersAdapter);
                    waitUsersAdapter.notifyDataSetChanged();  // data set changed
                }
                return false;
            }
        });*/
    }



    public void init() {

        mSearchPresenter = new SearchPresenter(DataManager.getInstance(),getActivity());
        mSearchPresenter.attachView(this);

        rv = (RecyclerView) getView().findViewById(R.id.recycler_view_result_fragment);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        //when button clicked
        Button searchButton = (Button) getView().findViewById(R.id.search_fragment_search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(GlobalEntities.AudioHandling_ACTIVITY,"searchButton: isclicked");
                searchSong(view);
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
                    searchSong(view);
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
    }

    @Override
    public void showResults(List<Item> itemList) {

            Log.i(GlobalEntities.SEARCH_FRAGMENT,"Results are: "+itemList.toString());
            RecyclerViewAdapter RA = new RecyclerViewAdapter(itemList, new RecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Item item) {
                    mListener.onResultInteraction(GlobalEntities.PLAY_TAG, item);
                }
            });
            rv.setAdapter(RA);
            RA.notifyDataSetChanged();

    }

    public void searchSongAPICall(String text){

    }

    //to search for specific song
    private void searchSong(View view){
        String text = searchEditText.getText().toString();
        text.trim();
        if(!text.isEmpty()){
            View keyboard = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            mSearchPresenter.search_youtube_API(text);
        }
        else {
            Toast.makeText(getContext(),"You must enter a word to start search",Toast.LENGTH_LONG).show();
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
        void onResultInteraction(String action, Item item);
    }
}
