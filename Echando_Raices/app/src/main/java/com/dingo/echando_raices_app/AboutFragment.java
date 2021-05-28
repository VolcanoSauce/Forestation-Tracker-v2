package com.dingo.echando_raices_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AboutFragment extends Fragment {
/*
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_about);
        TextView linkToSite = (TextView) getView().findViewById(R.id.textView9);
        linkToSite.setMovementMethod(LinkMovementMethod.getInstance());
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Intento de abrir el url al sitio oficial
        //View linkView = inflater.inflate(R.layout.fragment_about, container, false);
        //TextView linkToSite = (TextView) linkView.findViewById(R.id.textView9);
        //linkToSite.setMovementMethod(LinkMovementMethod.getInstance());

        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}