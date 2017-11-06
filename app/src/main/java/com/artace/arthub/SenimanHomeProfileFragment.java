package com.artace.arthub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.ViewPlugins.CircularNetworkImageView;
import com.artace.arthub.constant.Field;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.utils.YoutubeId;

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
    public TextView namaSeniman,keahlianSeniman;
    public EditText no_telp,jenis_kelamin,umur;
    public SharedPreferences sharedpreferences;
    public CircularNetworkImageView imageSeniman;
    public NetworkImageView portfolio;
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

        sharedpreferences = getActivity().getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        String Simage = sharedpreferences.getString(Field.getFOTO(),null);
        String Snama = sharedpreferences.getString(Field.getNAMA(),null);
        String Skeahlian= sharedpreferences.getString(Field.getKeahlianSpesifik(),null);
        String Sno_telp = sharedpreferences.getString(Field.getNoHp(),null);
        String Sjenis_kelamin = sharedpreferences.getString(Field.getJenisKelamin(),null);
        String Sumur = sharedpreferences.getString(Field.getUMUR(),null);
        String Sportfolio = sharedpreferences.getString(Field.getPORTFOLIO(),null);

        YoutubeId generateYoutubeId = new YoutubeId();
        final String idThumbnail = generateYoutubeId.generateId(Sportfolio);

        imageSeniman = (CircularNetworkImageView) rootView.findViewById(R.id.fragment_seniman_home_profile_imageSeniman);
        namaSeniman = (TextView) rootView.findViewById(R.id.fragment_seniman_home_profile_namaSeniman);
        keahlianSeniman = (TextView) rootView.findViewById(R.id.fragment_seniman_home_profile_keahlian);
        no_telp = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_noTelp);
        jenis_kelamin = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_jenisKelamin);
        umur = (EditText) rootView.findViewById(R.id.fragment_seniman_home_profile_umur);
        portfolio = (NetworkImageView) rootView.findViewById(R.id.fragment_seniman_home_profile_videoSeniman);

        portfolio.setImageUrl("https://img.youtube.com/vi/"+idThumbnail+"/0.jpg", AppController.getInstance().getImageLoader());
        imageSeniman.setImageUrl(Simage, AppController.getInstance().getImageLoader());
        namaSeniman.setText(Snama);
        keahlianSeniman.setText(Skeahlian);
        no_telp.setText(Sno_telp);
        jenis_kelamin.setText(Sjenis_kelamin);
        umur.setText(Sumur);

        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+idThumbnail));
                SenimanHomeProfileFragment.this.getContext().startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;

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