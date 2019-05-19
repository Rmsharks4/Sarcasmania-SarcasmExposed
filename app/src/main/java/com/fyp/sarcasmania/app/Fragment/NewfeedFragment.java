package com.fyp.sarcasmania.app.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyp.sarcasmania.app.R;

public class NewfeedFragment extends android.support.v4.app.Fragment {
    private OnFragmentInteractionListener mListener;

    public NewfeedFragment() {
    }

    public static NewfeedFragment newInstance(String param1, String param2) {
        NewfeedFragment fragment = new NewfeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newfeed, container, false);
    }

    //----------------newsfeed set karnay k liye interface------------------
    public interface newsFeedInterface {
        void setNewsFeed(); //implementation main activity mei or call onResume of this fragment
    }
    newsFeedInterface newsFeedInterface;

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            newsFeedInterface = (newsFeedInterface) context;//attaching interface, Main activity will implement
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
    public void onResume() {
        super.onResume();
        newsFeedInterface.setNewsFeed();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
