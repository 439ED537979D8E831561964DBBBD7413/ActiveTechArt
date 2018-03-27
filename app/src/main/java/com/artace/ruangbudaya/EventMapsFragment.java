package com.artace.ruangbudaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.artace.ruangbudaya.adapter.CustomWindowMapsAdapter;
import com.artace.ruangbudaya.adapter.EventAdapter;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventMapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventMapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventMapsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RequestQueue queue;
    String urlRead;
    RecyclerView recyclerView;
    List<PojoEvent> eventList = new ArrayList<PojoEvent>();
    public EventAdapter adapter;
//    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar mLoadingAnim;
    FloatingActionButton mFab;
    RelativeLayout rootView;
    Toolbar mToolbar;
    OrganizerMainActivity mainActivity;
    Bundle savedInstance;
    GoogleMap googleMapGlobal;

    //MAPS
    private MapView mMapView;
    private GoogleMap mGoogleMap;

    public EventMapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventMapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventMapsFragment newInstance(String param1, String param2) {
        EventMapsFragment fragment = new EventMapsFragment();
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
        savedInstance = savedInstanceState;
        setHasOptionsMenu(true);
        rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_event_maps, container, false);
        //TODO : ganti jd jd maps
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.event_maps_recyclerview);

        final RelativeLayout rootViewFinal = rootView;
        mainActivity = (OrganizerMainActivity) getActivity();

//        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.event_maps_swipeRefreshLayout);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getEvents(googleMapGlobal);
//            }
//        });

        mFab = (FloatingActionButton) rootView.findViewById(R.id.event_maps_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),TambahEventActivity.class);
                startActivityForResult(intent, 5);
            }
        });

        setToolbar();

        initMaps();

        return rootView;
    }

    private void initMaps(){
        mMapView = (MapView) rootView.findViewById(R.id.event_maps_maps);
        mMapView.onCreate(savedInstance);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMapGlobal = googleMap;
                getEvents(googleMap);
            }
        });
    }

    public void setToolbar(){
        mToolbar = (Toolbar) rootView.findViewById(R.id.event_maps_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(mainActivity.title);
    }

    public void getEvents(GoogleMap googleMap){
        urlRead = DatabaseConnection.getReadEventorganizerEvents();

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);

        if (session && sharedpreferences.getString(Field.getJenisUser(),null).equals("event_organizer")){
            urlRead += "?id_penyelenggara_acara=" + sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null);
            mFab.setVisibility(View.VISIBLE);
        }
        else{
            mFab.setVisibility(View.GONE);
        }

        eventList.clear();
        requestData(urlRead, googleMap);
    }

    public void requestData(String url, final GoogleMap googleMap){
        //Set loading anim
        mLoadingAnim = (ProgressBar) rootView.findViewById(R.id.event_maps_progressbar);
        mLoadingAnim.setVisibility(View.VISIBLE);

        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();

        //empty eventList
        eventList.clear();

        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoEvent event = new PojoEvent(obj.getInt("id_acara"),obj.getInt("id_penyelenggara_acara"),obj.getString("nama"),obj.getString("tanggal"),obj.getString("keterangan"),DatabaseConnection.getBaseUrl() + obj.getString("foto"),obj.getString("nama_eo"), obj.getDouble("latitude"), obj.getDouble("longitude"));

                            // adding event to events array
                            // eventList.add(event);
//                                    LatLng itemMap = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));
//                                    googleMap.addMarker(new MarkerOptions().position(itemMap)
//                                            .title(event.getNama())
//                                            .snippet(event.getTanggal())
//                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_black_24dp))
//                                            );
//                                    Log.d("MapsFragment","Lat : "+obj.getDouble("latitude")+" Long :"+obj.getDouble("longitude"));

                            LatLng position = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(position)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_face));

                            PojoEvent eventTag = new PojoEvent();
                            eventTag.setNama(obj.getString("nama"));
                            eventTag.setKeterangan(obj.getString("keterangan"));
                            eventTag.setTanggal(obj.getString("tanggal"));
                            eventTag.setFoto("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRemZK6JDPffh4b-VWkHCj86RUYaMG3GtTrJUw60uPX2vXCA1nW");

                            CustomWindowMapsAdapter customInfoWindow = new CustomWindowMapsAdapter(getContext());
                            googleMap.setInfoWindowAdapter(customInfoWindow);

                            Marker m = googleMap.addMarker(markerOptions);
                            m.setTag(eventTag);

                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                        }
                    }
                }catch (Exception e){
                    System.out.println("LOG gamao diluar! = " + e.getMessage());
                }
                finally {
                    mLoadingAnim.setVisibility(View.GONE);
//                    mSwipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_list, menu);
        MenuItem itemMenu = menu.findItem(R.id.menulist);

        itemMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ((OrganizerMainActivity)getActivity()).showEventList();
                return false;
            }
        });
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
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
