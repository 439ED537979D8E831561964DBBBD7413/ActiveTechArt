package com.artace.arthub;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.artace.arthub.adapter.EventAdapter;
import com.artace.arthub.adapter.ListTawaranAdapter;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.constant.Field;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoEvent;
import com.artace.arthub.pojo.PojoListTawaran;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {} interface
 * to handle interaction events.
 * Use the {@link SenimanListTawaranFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SenimanListTawaranFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    RequestQueue queue;
    String urlRead = DatabaseConnection.getReadTawaranTampil();
    RecyclerView recyclerView;
    List<PojoListTawaran> eventList = new ArrayList<PojoListTawaran>();
    ListTawaranAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar mLoadingAnim;
    FrameLayout rootView;

    public SenimanListTawaranFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SenimanListTawaranFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SenimanListTawaranFragment newInstance(String param1, String param2) {
        SenimanListTawaranFragment fragment = new SenimanListTawaranFragment();
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
        rootView = (FrameLayout) inflater.inflate(R.layout.fragment_seniman_list_tawaran, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_seniman_list_tawaran_recyclerview);
        adapter = new ListTawaranAdapter(getContext(), eventList, SenimanListTawaranFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

        final FrameLayout rootViewFinal = rootView;

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_seniman_list_tawaran_swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvents();
            }
        });

        getEvents();

        // Inflate the layout for this fragment
        return rootView;
    }

    public void getEvents(){
        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();
        Log.d("SenimanList","Tertrigger");

        //Set loading anim
        mLoadingAnim = (ProgressBar) rootView.findViewById(R.id.fragment_seniman_list_tawaran_progressbar);
        mLoadingAnim.setVisibility(View.VISIBLE);

        //empty eventList
        eventList.clear();

        //Volley's inbuilt class to make Json array request
        final FrameLayout rootViewFinal = rootView;

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        String id_seniman = sharedpreferences.getString(Field.getIdSeniman(),null);
        if (id_seniman == null){

        }
            urlRead += "?id_seniman=" + sharedpreferences.getString(Field.getIdSeniman(), null);

        JsonArrayRequest newsReq = new JsonArrayRequest(urlRead, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoListTawaran event = new PojoListTawaran(obj.getInt("id_tawaran_tampil"), obj.getInt("id_event") , obj.getInt("id_seniman"), obj.getInt("id_event_organizer"), obj.getString("harga"), obj.getString("status"), obj.getString("nama"), obj.getString("tanggal"), obj.getString("lokasi"), obj.getString("keterangan"), obj.getString("foto"), obj.getString("eo"), obj.getString("nama_eo"));
                            Log.e("asd","aaaa");
                            // adding event to events array
                            eventList.add(event);
                        } catch (Exception e) {
                            Log.e("LOG gamao! = ", e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            adapter.notifyItemChanged(i);
                            Log.e("asd","final");
                        }
                    }
                }catch (Exception e){
                    Log.e("LOG gamao diluar! = ",e.getMessage());
                }
                finally {
                    mLoadingAnim.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("senimanlisttawaran = ",error.getMessage());

            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
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