package com.example.sundforluft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HelpFragment extends Fragment implements View.OnClickListener {

    Button favorit, map, rankliste, scanner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        favorit = view.findViewById(R.id.favorit);
        map = view.findViewById(R.id.map);
        rankliste = view.findViewById(R.id.rankliste);
        scanner = view.findViewById(R.id.scanner);

        favorit.setOnClickListener(this);
        map.setOnClickListener(this);
        rankliste.setOnClickListener(this);
        scanner.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        switch (v.getId()){
            case R.id.favorit:
                mFragmentTransaction.replace(R.id.fragment_container, new HelpFavoritFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
                break;
            case R.id.map:
                mFragmentTransaction.replace(R.id.fragment_container, new HelpFavoritFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
                break;
            case R.id.rankliste:
                mFragmentTransaction.replace(R.id.fragment_container, new HelpFavoritFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
                break;
            case R.id.scanner:
                mFragmentTransaction.replace(R.id.fragment_container, new HelpFavoritFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
                break;
        }
    }
}
