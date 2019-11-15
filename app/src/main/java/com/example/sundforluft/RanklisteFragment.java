package com.example.sundforluft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RanklisteFragment extends Fragment implements View.OnClickListener {

    private Button nortjutland, midjutland, southjutland, zealand, copenhagen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranklist, container, false);

        nortjutland = view.findViewById(R.id.northjutland);
        midjutland = view.findViewById(R.id.midjutland);
        southjutland = view.findViewById(R.id.southjutland);
        zealand = view.findViewById(R.id.zealand);
        copenhagen = view.findViewById(R.id.copenhagen);

        nortjutland.setOnClickListener(this);
        midjutland.setOnClickListener(this);
        southjutland.setOnClickListener(this);
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
                        .replace(R.id.fragment_container, new RanklisteNorthJutlandFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
            else if (view == midjutland){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklisteNorthJutlandFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
            else if (view == southjutland){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklisteNorthJutlandFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
            else if (view == zealand){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklisteNorthJutlandFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
            else if (view == copenhagen){
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, new RanklisteNorthJutlandFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
