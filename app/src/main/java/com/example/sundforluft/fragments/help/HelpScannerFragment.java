package com.example.sundforluft.fragments.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;

public class HelpScannerFragment extends Fragment {

    Button back, forward;
    ImageView imageView,  help_container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_help, container, false);

        back = view.findViewById(R.id.back);
        forward = view.findViewById(R.id.forward);
        imageView = view.findViewById(R.id.imageView);
        help_container = view.findViewById(R.id.help_container);

        imageView.setImageResource(R.drawable.ic_help_qr);
        help_container.setImageResource(R.drawable.qrscanner_help);

        back.setVisibility(view.GONE);
        forward.setVisibility(view.GONE);

        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*if (v.getId() == R.id.rankliste)
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragment_container, new RanklistFragment()).commit();
                    else{
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.fragment_container, new HelpFragment()).commit();
                    }*/
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
