package com.example.sundforluft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sundforluft.models.FavoritListviewModel;
import com.example.sundforluft.services.FavoritListviewAdapter;

public class FavoritFragment extends Fragment {

    FavoritListviewAdapter favoritListviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);

        favoritListviewAdapter = new FavoritListviewAdapter(this);
        favoritListviewAdapter.setClickListener(new FavoritListviewAdapter.ClickListener() {
            @Override
            public void onClick(FavoritListviewModel model, FavoritListviewAdapter.ClickListenerType type) {
                FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                switch (type) {
                    case School:
                        mFragmentTransaction
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                .replace(R.id.fragment_container, new FavoritDetailedFragment())
                                .commit();
                        mFragmentTransaction.addToBackStack(null);

                        break;
                    case Close:
                        favoritListviewAdapter.deleteSchoolByModel(model);
                        break;
                }
            }
        });

        FavoritListviewModel[] favoritListviewModels = new FavoritListviewModel[] {
                new FavoritListviewModel(this, "Gentofte Skole", 12),
                new FavoritListviewModel(this, "Dyssegård Skole", 14),
                new FavoritListviewModel(this, "Ishøj Skole", 4),
                new FavoritListviewModel(this, "Hellerup Skole", 42),
        };
        for (FavoritListviewModel favoritListviewModel : favoritListviewModels) { favoritListviewAdapter.addSchool(favoritListviewModel); }

        ListView schoolModelListView = view.findViewById(R.id.listView);
        schoolModelListView.setAdapter(favoritListviewAdapter);

        EditText editText = view.findViewById(R.id.editText);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    ViewAnimationUtils.createCircularReveal(editText,
                            (int) event.getX(),
                            (int) event.getY(),
                            0,
                            editText.getHeight() * 2).start();
                }
                return false;
            }
        });

        return view;
    }
}