package com.artace.arthub;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.artace.arthub.adapter.EventAdapter;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {} interface
 * to handle interaction events.
 * Use the {@link OrganizerEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizerEventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    RequestQueue queue;
    String urlRead = DatabaseConnection.getReadEventorganizerEvents();
    RecyclerView recyclerView;
    List<PojoEvent> eventList = new ArrayList<PojoEvent>();
    EventAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar mLoadingAnim;

    public OrganizerEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizerEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganizerEventsFragment newInstance(String param1, String param2) {
        OrganizerEventsFragment fragment = new OrganizerEventsFragment();
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
        FrameLayout rootView = (FrameLayout) inflater.inflate(R.layout.fragment_organizer_events, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.organizer_events_recyclerview);
        adapter = new EventAdapter(getContext(), eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

        final FrameLayout rootViewFinal = rootView;

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.organizer_events_swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvents(rootViewFinal);
            }
        });

        getEvents(rootView);

        // Inflate the layout for this fragment
        return rootView;

    }

    private void getEvents(FrameLayout rootView){
        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();

        //Set loading anim
        mLoadingAnim = (ProgressBar) rootView.findViewById(R.id.organizer_events_progressbar);
        mLoadingAnim.setVisibility(View.VISIBLE);

        //empty eventList
        eventList.clear();

        //Volley's inbuilt class to make Json array request
        final FrameLayout rootViewFinal = rootView;
        JsonArrayRequest newsReq = new JsonArrayRequest(urlRead, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoEvent event = new PojoEvent(obj.getInt("id_event"),obj.getInt("id_event_organizer"),obj.getString("nama"),obj.getString("tanggal"),obj.getString("lokasi"),obj.getString("keterangan"),DatabaseConnection.getBaseUrl() + obj.getString("foto"),obj.getString("nama_eo"));

                            // adding event to events array
                            eventList.add(event);

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
