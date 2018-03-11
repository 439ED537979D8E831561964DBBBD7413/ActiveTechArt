package com.artace.ruangbudaya;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.artace.ruangbudaya.adapter.ListDiundangAdapter;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoSeniman;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {} interface
 * to handle interaction events.
 * Use the {@link OrganizerDiundangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizerDiundangFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RelativeLayout rootView;
    RecyclerView recyclerView;
    ListDiundangAdapter adapter;
    List<PojoSeniman> senimanList = new ArrayList<PojoSeniman>();;
    ProgressBar mLoadingAnim;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RequestQueue queue;
    String urlRead = DatabaseConnection.getReadListTawaranTampil();
    OrganizerMainActivity mainActivity;
    TextView mJudul, mSubJudul;
    int IdBidangSeni;
    Toolbar mToolbar;

//    private OnFragmentInteractionListener mListener;

    public OrganizerDiundangFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizerDiundangFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganizerDiundangFragment newInstance(String param1, String param2) {
        OrganizerDiundangFragment fragment = new OrganizerDiundangFragment();
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

        rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_organizer_diundang, container, false);

        mainActivity = (OrganizerMainActivity) getActivity();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.organizer_diundang_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.organizer_diundang_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ListDiundangAdapter(getContext(), senimanList, OrganizerDiundangFragment.this);
        recyclerView.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.organizer_diundang_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        setToolbar();

        getData();


        return rootView;
    }

    public void getData(){
        urlRead = DatabaseConnection.getReadListTawaranTampil();
        SharedPreferences sharedpreferences = getActivity().getApplicationContext().getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);
        String id_penyelenggara_acara = "";
        if (session || sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null) != null){
            id_penyelenggara_acara = sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null);
        }

        if(mainActivity.title.equals("Musisi")){
            IdBidangSeni = 1;
        }
        else if(mainActivity.title.equals("Tari")){
            IdBidangSeni = 2;
        }
        else if(mainActivity.title.equals("Komedian")){
            IdBidangSeni = 3;
        }
        else if(mainActivity.title.equals("Wayang")){
            IdBidangSeni = 4;
        }
        else if(mainActivity.title.equals("Teater")){
            IdBidangSeni = 5;
        }

        urlRead += "?id_bidang_seni="+IdBidangSeni+"&id_penyelenggara_acara="+id_penyelenggara_acara;
        Log.e("DiundangFrag","id eo : "+id_penyelenggara_acara);
        Log.e("DiundangFrag1","id bidang : "+IdBidangSeni);
        senimanList.clear();
        requestData(urlRead);
    }

    public void getDataSearch(String searchString){
        urlRead = DatabaseConnection.getReadListTawaranTampil();
        SharedPreferences sharedpreferences = getActivity().getApplicationContext().getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);
        String id_penyelenggara_acara = "";
        if (session || sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null) != null){
            id_penyelenggara_acara = sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null);
        }

        if(mainActivity.title.equals("Musisi")){
            IdBidangSeni = 1;
        }
        else if(mainActivity.title.equals("Tari")){
            IdBidangSeni = 2;
        }
        else if(mainActivity.title.equals("Komedian")){
            IdBidangSeni = 3;
        }
        else if(mainActivity.title.equals("Wayang")){
            IdBidangSeni = 4;
        }
        else if(mainActivity.title.equals("Teater")){
            IdBidangSeni = 5;
        }

        urlRead += "?id_bidang_seni="+IdBidangSeni+"&id_penyelenggara_acara="+id_penyelenggara_acara+"&search="+searchString;
        Log.e("DiundangFrag","id eo : "+id_penyelenggara_acara);
        Log.e("DiundangFrag1","id bidang : "+IdBidangSeni);
        urlRead = urlRead.replaceAll(" ","%20");

        //empty list
        senimanList.clear();
        requestData(urlRead);
    }

    public void requestData(String url){

        mLoadingAnim = (ProgressBar) rootView.findViewById(R.id.organizer_diundang_progressbar);
        mLoadingAnim.setVisibility(View.VISIBLE);

        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();

        //empty eventList
        senimanList.clear();
        adapter.notifyDataSetChanged();

        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoSeniman seniman = new PojoSeniman(obj.getInt("id_kelompok_seniman"),obj.getInt("id_bidang_seni"),obj.getString("nama"),DatabaseConnection.getBaseUrl() + obj.getString("foto"),obj.getString("status"), obj.getInt("id_tawaran_tampil"),obj.getInt("status_kehadiran"), obj.getString("namaevent"));

                            // adding event to events array
                            senimanList.add(seniman);
                            Log.d("DiundangFrag","Iterasi diundang frag");

                        } catch (Exception e) {
                            Log.e("DiundangFrag","LOG gamao ! = " + e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            adapter.notifyItemChanged(i);
                        }
                    }
                }catch (Exception e){
                    Log.e("DiundangFrag","LOG gamao diluar! = " + e.getMessage());

                }
                finally {
                    mLoadingAnim.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DiundangFrag","gamao ! = " + error.getMessage());

            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
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

    public void setToolbar(){
        mToolbar = (Toolbar) rootView.findViewById(R.id.organizer_diundang_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(mainActivity.title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
