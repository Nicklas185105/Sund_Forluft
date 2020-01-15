package com.example.sundforluft.fragments.ranklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sundforluft.R;

public class RanklistFragment extends Fragment implements View.OnClickListener {

    private Button nortjutland, midjutland, southdenmark, zealand, copenhagen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranklist, container, false);

        nortjutland = view.findViewById(R.id.northjutland);
        midjutland = view.findViewById(R.id.midjutland);
        southdenmark = view.findViewById(R.id.southdenmark);
        zealand = view.findViewById(R.id.zealand);
        copenhagen = view.findViewById(R.id.copenhagen);

        nortjutland.setOnClickListener(this);
        midjutland.setOnClickListener(this);
        southdenmark.setOnClickListener(this);
        zealand.setOnClickListener(this);
        copenhagen.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        try {
            if (view == nortjutland){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklistNorthJutlandFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
            else if (view == midjutland){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklistMidJutlandFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
            else if (view == southdenmark){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklistSouthdenmarkFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
            else if (view == zealand){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklistZealandFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
            else if (view == copenhagen){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklistCopenhagenFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
