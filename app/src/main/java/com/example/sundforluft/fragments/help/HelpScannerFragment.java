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

import com.example.sundforluft.R;

public class HelpScannerFragment extends Fragment implements View.OnClickListener {

    Button back, forward;

    ImageView imageView;

    int n;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_favorit, container, false);

        back = view.findViewById(R.id.back);
        forward = view.findViewById(R.id.forward);
        imageView = view.findViewById(R.id.imageView);

        back.setOnClickListener(this);
        forward.setOnClickListener(this);

        imageView.setImageResource(R.drawable.ic_help_1_place);
        n = 1;

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == back){
            switch (n){
                case 2:
                    imageView.setImageResource(R.drawable.ic_help_1_place);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.ic_help_2_place);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.ic_help_3_place);
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.ic_help_4_place);
                    break;
                case 6:
                    imageView.setImageResource(R.drawable.ic_help_5_place);
                    break;
                case 7:
                    imageView.setImageResource(R.drawable.ic_help_6_place);
                    break;
            }
            n--;
        }
        else if (v == forward){
            switch (n){
                case 1:
                    imageView.setImageResource(R.drawable.ic_help_2_place);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.ic_help_3_place);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.ic_help_4_place);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.ic_help_5_place);
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.ic_help_6_place);
                    break;
                case 6:
                    imageView.setImageResource(R.drawable.ic_help_7_place);
                    break;
            }
            n++;
        }
    }
}
