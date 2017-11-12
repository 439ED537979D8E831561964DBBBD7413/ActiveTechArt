package com.artace.arthub;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
import com.squareup.picasso.Picasso;

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
    String urlRead = DatabaseConnection.getReadSenimanList();
    RecyclerView recyclerView;
    List<PojoSeniman> senimanList = new ArrayList<PojoSeniman>();
    SenimanPortfolioListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar mLoadingAnim;
    Toolbar mToolbar;
    OrganizerMainActivity mainActivity;
    RelativeLayout rootView;
    int idJenisSeniman;

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
                getEvents();
            }
        });

        getEvents();

        setToolbar();

        // Inflate the layout for this fragment
        return rootView;

    }

    private void getEvents(){
        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();

        //Set loading anim
        mLoadingAnim = (ProgressBar) rootView.findViewById(R.id.organizer_portfolio_progressbar);
        mLoadingAnim.setVisibility(View.VISIBLE);

        //empty eventList
        senimanList.clear();

        if(mainActivity.title.equals("Musisi")){
            idJenisSeniman = 1;
        }
        else if(mainActivity.title.equals("Penari")){
            idJenisSeniman = 2;
        }
        else if(mainActivity.title.equals("Bondres")){
            idJenisSeniman = 3;
        }

        urlRead += "?id_jenis_seniman="+idJenisSeniman;

        JsonArrayRequest newsReq = new JsonArrayRequest(urlRead, new Response.Listener<JSONArray>() {
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

    private void setToolbar(){
        mToolbar = (Toolbar) rootView.findViewById(R.id.organizer_portfolio_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(mainActivity.title);
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
