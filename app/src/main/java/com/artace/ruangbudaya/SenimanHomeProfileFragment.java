package com.artace.ruangbudaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.ViewPlugins.CircularNetworkImageView;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.utils.StringPostRequest;
import com.artace.ruangbudaya.utils.VolleyResponseListener;
import com.artace.ruangbudaya.utils.YoutubeId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {} interface
 * to handle interaction events.
 * Use the {@link SenimanHomeProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SenimanHomeProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    RequestQueue queue;
    private String mParam1;
    private String mParam2;
    public String Mno_hp,Mportfolio,id_kelompok_seniman,Mnama;
    public TextView namaSeniman;
    public EditText no_hp,link_video,ubahvid;
    public SharedPreferences sharedpreferences;
    public CircularNetworkImageView imageSeniman;
    public NetworkImageView portfolio;
    public FloatingActionButton fab,fabs,fabc;
    public ImageView imgubahvid;
    boolean session;

//    private OnFragmentInteractionListener mListener;

    public SenimanHomeProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizerSenimanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SenimanHomeProfileFragment newInstance(String param1, String param2) {
        SenimanHomeProfileFragment fragment = new SenimanHomeProfileFragment();
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
        ConstraintLayout rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_seniman_home_profile, container, false);

        sharedpreferences = getActivity().getSharedPreferences(Field.getLoginSharedPreferences(), MODE_PRIVATE);
        final String Simage = sharedpreferences.getString(Field.getFOTO(),null);
        String Snama = sharedpreferences.getString(Field.getNAMA(),null);
        String Sno_hp = sharedpreferences.getString(Field.getNoHp(),null);
        String Sportfolio = sharedpreferences.getString(Field.getPORTFOLIO(),null);

        YoutubeId generateYoutubeId = new YoutubeId();
        final String idThumbnail = generateYoutubeId.generateId(Sportfolio);

        imageSeniman = (CircularNetworkImageView) rootView.findViewById(R.id.fragment_seniman_home_profile_imageSeniman);
        namaSeniman = (TextView) rootView.findViewById(R.id.fragment_seniman_home_profile_namaSeniman);
        no_hp = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_noHp);
        link_video = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_linkVideo);
        portfolio = (NetworkImageView) rootView.findViewById(R.id.fragment_seniman_home_profile_videoSeniman);

        //SET FIELD DISABLE
        no_hp.setEnabled(false);

        imageSeniman.setImageUrl(Simage, AppController.getInstance().getImageLoader());
        portfolio.setImageUrl("https://img.youtube.com/vi/"+idThumbnail+"/0.jpg", AppController.getInstance().getImageLoader());
        link_video.setText(Sportfolio);
        namaSeniman.setText(Snama);
        no_hp.setText(Sno_hp);

        imageSeniman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SenimanHomeProfileFragment.this.getContext(),ShowPictureActivity.class);
                intent.putExtra("imageViewExtra",Simage);
                startActivity(intent);
            }
        });


        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SenimanHomeProfileFragment.this.getContext(),YoutubePlayerViewActivity.class);
                intent.putExtra("idThumbnailExtra", idThumbnail);
                SenimanHomeProfileFragment.this.getContext().startActivity(intent);
            }
        });
        fab = (FloatingActionButton)rootView.findViewById(R.id.fragment_seniman_home_profile_floatingActionButton);
        fabs = (FloatingActionButton)rootView.findViewById(R.id.fragment_seniman_home_profile_floatingActionButtonSave);
        fabc = (FloatingActionButton) rootView.findViewById(R.id.fragment_seniman_home_profile_floatingActionButtonCancel);
        ubahvid = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_linkVideo);
        imgubahvid = (ImageView) rootView.findViewById(R.id.fragment_seniman_home_profile_img4);
        fabs.setVisibility(View.INVISIBLE);
        fabc.setVisibility(View.INVISIBLE);
        ubahvid.setVisibility(View.GONE);
        imgubahvid.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                no_hp.setEnabled(true);
                fab.setVisibility(View.GONE);
                ubahvid.setVisibility(View.VISIBLE);
                imgubahvid.setVisibility(View.VISIBLE);
                fabs.setVisibility(View.VISIBLE);
                fabc.setVisibility(View.VISIBLE);
//                fillJenisKelaminSpinner();
            }
        });
        fabs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                submitForm();
                no_hp.setEnabled(false);
                fab.setVisibility(View.VISIBLE);
                ubahvid.setVisibility(View.GONE);
                imgubahvid.setVisibility(View.GONE);
                fabs.setVisibility(View.INVISIBLE);
                fabc.setVisibility(View.INVISIBLE);

            }
        });
        fabc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_hp.setEnabled(false);
                fab.setVisibility(View.VISIBLE);
                ubahvid.setVisibility(View.GONE);
                imgubahvid.setVisibility(View.GONE);
                fabs.setVisibility(View.INVISIBLE);
                fabc.setVisibility(View.INVISIBLE);

                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;

    }

    private void submitForm(){
        Mnama = namaSeniman.getText().toString();
        Mportfolio = link_video.getText().toString();
        Mno_hp = no_hp.getText().toString();
        String Mjenis_kelamin;


        Map<String,String> params = new HashMap<String, String>();
        params.put("no_hp",Mno_hp);
        params.put("nama",Mnama);
        params.put("portfolio",Mportfolio);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Field.getSessionStatus(), true);
        editor.putString(Field.getNoHp(), Mno_hp);
        editor.putString(Field.getPORTFOLIO(), Mportfolio);
        editor.commit();

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        id_kelompok_seniman = sharedpreferences.getString(Field.getIdKelompokSeniman(),null);
        Log.d("LogUpdateSeniman","ID SENIMAN = "+id_kelompok_seniman);
        params.put("id_kelompok_seniman",id_kelompok_seniman);


        StringPostRequest strReq = new StringPostRequest();
        strReq.sendPost(getActivity(), params, DatabaseConnection.getUpdateSeniman(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
//                finish();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                Log.e("SenimanHomeProfile","Ada error : "+message);
            }
        });
    }

//    public void finish() {
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("resultMessage", "UPDATE_SENIMAN");
//        getActivity().setResult(getActivity().RESULT_OK,returnIntent);
//
//        super.getActivity().finish();
//    }
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