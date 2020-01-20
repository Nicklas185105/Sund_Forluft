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

import java.util.Objects;

public class HelpScannerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_help, container, false);

        Button back = view.findViewById(R.id.back);
        Button forward = view.findViewById(R.id.forward);
        ImageView imageView = view.findViewById(R.id.imageView);
        ImageView help_container = view.findViewById(R.id.help_container);

        imageView.setImageResource(R.drawable.ic_help_qr);
        help_container.setImageResource(R.drawable.qrscanner_help);

        back.setVisibility(View.GONE);
        forward.setVisibility(View.GONE);

        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        MainActivity.toolbar.setNavigationOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        return view;
    }
}
