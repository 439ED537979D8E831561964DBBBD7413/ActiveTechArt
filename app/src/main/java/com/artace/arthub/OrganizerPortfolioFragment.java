package com.artace.arthub;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.artace.arthub.adapter.EventAdapter;
import com.artace.arthub.adapter.SenimanPortfolioListAdapter;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.constant.Field;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoEvent;
import com.artace.arthub.pojo.PojoSeniman;
import com.mikepenz.iconics.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {} interface
 * to handle interaction events.
 * Use the {@link OrganizerPortfolioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizerPortfolioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RequestQueue queue;
    String urlRead;
    String urlReadJenisSeniman = DatabaseConnection.getReadJenisSeniman();
    RecyclerView recyclerView;
    List<PojoSeniman> senimanList = new ArrayList<PojoSeniman>();
    SenimanPortfolioListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar mLoadingAnim;
    Toolbar mToolbar;
    OrganizerMainActivity mainActivity;
    RelativeLayout rootView;
    SearchView searchView;
    SearchView.OnQueryTextListener queryTextListener;
    String id_jenis_seniman;

//    private OnFragmentInteractionListener mListener;

    public OrganizerPortfolioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizerPortfolioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganizerPortfolioFragment newInstance(String param1, String param2) {
        OrganizerPortfolioFragment fragment = new OrganizerPortfolioFragment();
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
        setHasOptionsMenu(true);
        rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_organizer_portfolio, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.organizer_portfolio_recyclerview);
        adapter = new SenimanPortfolioListAdapter(getContext(), senimanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

        mainActivity = (OrganizerMainActivity) getActivity();

        final RelativeLayout rootViewFinal = rootView;

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.organizer_portfolio_swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        if(mainActivity.title.equals("Musisi")){
            id_jenis_seniman = "1";

        }
        else if(mainActivity.title.equals("Penari")){
            id_jenis_seniman = "2";
        }
        else if(mainActivity.title.equals("Bondres")){
            id_jenis_seniman = "3";
        }

        getData();

        setToolbar();
        // Inflate the layout for this fragment
        return rootView;

    }

    private void initGetData(){
        //Set loading anim
        mLoadingAnim = (ProgressBar) rootView.findViewById(R.id.organizer_portfolio_progressbar);
        mLoadingAnim.setVisibility(View.VISIBLE);
    }

    public void getData(){
        urlRead = DatabaseConnection.getReadSenimanList();
        urlRead += "?id_jenis_seniman="+id_jenis_seniman;
        Log.d("OrganizerSF","URL = "+urlRead);
        //empty list
        senimanList.clear();
        requestData(urlRead);
    }

    public void getDataSearch(String searchString){
        urlRead = DatabaseConnection.getReadSenimanList();
        urlRead += "?id_jenis_seniman="+id_jenis_seniman+"&search="+searchString;
        urlRead = urlRead.replaceAll(" ","%20");
        //empty list
        senimanList.clear();
        requestData(urlRead);
    }

    private void requestData(String url){
        initGetData();
        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();

        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoSeniman event = new PojoSeniman(obj.getInt("id_seniman"),obj.getInt("id_jenis_seniman"),obj.getInt("id_user"),obj.getString("nama"),obj.getString("jenis_kelamin"),obj.getString("portfolio"),obj.getString("no_hp"),obj.getString("umur"), DatabaseConnection.getBaseUrl() + obj.getString("foto"),obj.getString("keahlian_spesifik"),obj.getString("format_solo_grup"));

                            // adding event to events array
                            senimanList.add(event);

                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            adapter.notifyItemChanged(i);
                        }
                    }
                }catch (Exception e){
                    System.out.println("LOG gamao diluar! = " + e.getMessage());
                }
                finally {
                    mLoadingAnim.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("LOG_OrganizerEventsFragment : "+error.getMessage());

            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    public void setToolbar(){
        mToolbar = (Toolbar) rootView.findViewById(R.id.organizer_portfolio_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(mainActivity.title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem cari = menu.findItem(R.id.searchbox);

        final SearchView searchView = new SearchView(((OrganizerMainActivity) getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(cari, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(cari, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showSoftwareKeyboard(false);
                getDataSearch(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
//                getDataSearch(query);

                return true;
            }

        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
    }


    protected void showSoftwareKeyboard(boolean showKeyboard){
        final Activity activity = getActivity();
        final InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), showKeyboard ? InputMethodManager.SHOW_FORCED : InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
