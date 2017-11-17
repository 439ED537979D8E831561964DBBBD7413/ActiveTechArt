package com.artace.arthub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.ViewPlugins.CircularNetworkImageView;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.constant.Field;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.utils.StringPostRequest;
import com.artace.arthub.utils.VolleyResponseListener;
import com.artace.arthub.utils.YoutubeId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
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
    public String Mno_hp,Mjenis_kelamin,Mumur,Mportfolio,id_seniman,Mnama;
    public TextView namaSeniman,keahlianSeniman;
    public EditText no_hp,jenis_kelamin,umur,link_video,ubahvid;
    public SharedPreferences sharedpreferences;
    public CircularNetworkImageView imageSeniman;
    public NetworkImageView portfolio;
    public FloatingActionButton fab,fabs,fabc;
    public ImageView imgubahvid;
    public Spinner spinnerJK;
    List<String> listDisplayJK;
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
        String Skeahlian= sharedpreferences.getString(Field.getKeahlianSpesifik(),null);
        String Sno_hp = sharedpreferences.getString(Field.getNoHp(),null);
        String Sjenis_kelamin = sharedpreferences.getString(Field.getJenisKelamin(),null);
        String Sumur = sharedpreferences.getString(Field.getUMUR(),null);
        String Sportfolio = sharedpreferences.getString(Field.getPORTFOLIO(),null);

        YoutubeId generateYoutubeId = new YoutubeId();
        final String idThumbnail = generateYoutubeId.generateId(Sportfolio);

        imageSeniman = (CircularNetworkImageView) rootView.findViewById(R.id.fragment_seniman_home_profile_imageSeniman);
        namaSeniman = (TextView) rootView.findViewById(R.id.fragment_seniman_home_profile_namaSeniman);
        keahlianSeniman = (TextView) rootView.findViewById(R.id.fragment_seniman_home_profile_keahlian);
        no_hp = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_noHp);
        umur = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_umur);
        link_video = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_linkVideo);
        portfolio = (NetworkImageView) rootView.findViewById(R.id.fragment_seniman_home_profile_videoSeniman);

        //SET FIELD DISABLE
        no_hp.setEnabled(false);
        umur.setEnabled(false);

        imageSeniman.setImageUrl(Simage, AppController.getInstance().getImageLoader());
        portfolio.setImageUrl("https://img.youtube.com/vi/"+idThumbnail+"/0.jpg", AppController.getInstance().getImageLoader());
        link_video.setText(Sportfolio);
        namaSeniman.setText(Snama);
        keahlianSeniman.setText(Skeahlian);
        no_hp.setText(Sno_hp);
        umur.setText(Sumur);

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
        spinnerJK = (Spinner) rootView.findViewById(R.id.fragment_seniman_home_profile_jenisKelaminSpinner);
        fillJenisKelaminSpinner();
        spinnerJK.setEnabled(false);
        fabs.setVisibility(View.INVISIBLE);
        fabc.setVisibility(View.INVISIBLE);
        ubahvid.setVisibility(View.GONE);
        imgubahvid.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                no_hp.setEnabled(true);
                umur.setEnabled(true);
                fab.setVisibility(View.GONE);
                ubahvid.setVisibility(View.VISIBLE);
                imgubahvid.setVisibility(View.VISIBLE);
                fabs.setVisibility(View.VISIBLE);
                fabc.setVisibility(View.VISIBLE);
                spinnerJK.setEnabled(true);
//                fillJenisKelaminSpinner();
            }
        });
        fabs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                submitForm();
                no_hp.setEnabled(false);
                umur.setEnabled(false);
                fab.setVisibility(View.VISIBLE);
                ubahvid.setVisibility(View.GONE);
                imgubahvid.setVisibility(View.GONE);
                fabs.setVisibility(View.INVISIBLE);
                fabc.setVisibility(View.INVISIBLE);
                spinnerJK.setEnabled(false);

            }
        });
        fabc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_hp.setEnabled(false);
                umur.setEnabled(false);
                fab.setVisibility(View.VISIBLE);
                ubahvid.setVisibility(View.GONE);
                imgubahvid.setVisibility(View.GONE);
                fabs.setVisibility(View.INVISIBLE);
                fabc.setVisibility(View.INVISIBLE);
                spinnerJK.setEnabled(false);

                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;

    }
    private void fillJenisKelaminSpinner(){

        listDisplayJK = new ArrayList<String>();
        sharedpreferences = getActivity().getSharedPreferences(Field.getLoginSharedPreferences(), MODE_PRIVATE);
        String Sjenis_kelamin = sharedpreferences.getString(Field.getJenisKelamin(),null);

        if (Sjenis_kelamin.equals("Wanita"))
        {
            listDisplayJK.add(Sjenis_kelamin);
            listDisplayJK.add("Pria");
            listDisplayJK.add("Group Campuran");
        }
        else if (Sjenis_kelamin.equals("Pria"))
        {
            listDisplayJK.add(Sjenis_kelamin);
            listDisplayJK.add("Wanita");
            listDisplayJK.add("Group Campuran");
        }
        else if (Sjenis_kelamin.equals("Group Campuran"))
        {
            listDisplayJK.add(Sjenis_kelamin);
            listDisplayJK.add("Pria");
            listDisplayJK.add("Wanita");
        }
        else
        {
            listDisplayJK.add("Pria");
            listDisplayJK.add("Wanita");
            listDisplayJK.add("Group Campuran");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, listDisplayJK);

        spinnerJK.setAdapter(dataAdapter);
    }
    private void submitForm(){
        Mnama = namaSeniman.getText().toString();
        Mportfolio = link_video.getText().toString();
        Mno_hp = no_hp.getText().toString();
        String Mjenis_kelamin;
        Mjenis_kelamin = listDisplayJK.get(spinnerJK.getSelectedItemPosition());
        Mumur = umur.getText().toString();

        Map<String,String> params = new HashMap<String, String>();
        params.put("no_hp",Mno_hp);
        params.put("jenis_kelamin",Mjenis_kelamin);
        params.put("umur",Mumur);
        params.put("nama",Mnama);
        params.put("portfolio",Mportfolio);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Field.getSessionStatus(), true);
        editor.putString(Field.getJenisKelamin(), Mjenis_kelamin);
        editor.putString(Field.getNoHp(), Mno_hp);
        editor.putString(Field.getUMUR(), Mumur);
        editor.putString(Field.getPORTFOLIO(), Mportfolio);
        editor.commit();

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        id_seniman = sharedpreferences.getString(Field.getIdSeniman(),null);
        Log.d("LogUpdateSeniman","ID SENIMAN = "+id_seniman);
        params.put("id_seniman",id_seniman);


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