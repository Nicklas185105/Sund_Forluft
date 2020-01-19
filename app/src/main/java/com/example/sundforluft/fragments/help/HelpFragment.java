package com.example.sundforluft.fragments.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;

public class HelpFragment extends Fragment implements View.OnClickListener {

    Button favorit, skoler, rankliste, scanner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        favorit = view.findViewById(R.id.favorit);
        skoler = view.findViewById(R.id.skoler);
        rankliste = view.findViewById(R.id.rankliste);
        scanner = view.findViewById(R.id.scanner);

        favorit.setOnClickListener(this);
        skoler.setOnClickListener(this);
        rankliste.setOnClickListener(this);
        scanner.setOnClickListener(this);

        MainActivity.toggle.setDrawerIndicatorEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.toggle.syncState();
        MainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.drawer.openDrawer(GravityCompat.START);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        switch (v.getId()){
            case R.id.favorit:
                mFragmentTransaction.replace(R.id.fragment_container, new HelpFavoritFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
                break;
            case R.id.skoler:
                mFragmentTransaction.replace(R.id.fragment_container, new HelpSkolerFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
                break;
            case R.id.rankliste:
                mFragmentTransaction.replace(R.id.fragment_container, new HelpRanklisteFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
                break;
            case R.id.scanner:
                mFragmentTransaction.replace(R.id.fragment_container, new HelpScannerFragment()).commit();
                mFragmentTransaction.addToBackStack(null);
                break;
        }
    }
}
