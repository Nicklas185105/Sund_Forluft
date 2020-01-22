package com.example.sundforluft.fragments.schools;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sundforluft.DAL.DataAccessLayer;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.MainActivity;
import com.example.sundforluft.R;
import com.example.sundforluft.fragments.favorite.FavoriteFragment;
import com.example.sundforluft.services.CacheSchoolMananger;
import com.example.sundforluft.services.SchoolsFragmentListviewAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class SchoolsFragment extends Fragment {
    private SchoolsFragmentListviewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);

        SchoolsFragment self = this;
        EditText filter = rootView.findViewById(R.id.editText);

        ListView listView = rootView.findViewById(R.id.listView);
        ArrayList<String> schools = new ArrayList<>();
        for (SchoolModel model : DataAccessLayer.getInstance().getSchools()) {
            if (model.Id != 0) {
                schools.add(model.Name);
            }
        }

        adapter = new SchoolsFragmentListviewAdapter(this, schools);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        filter.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        if (getActivity() != null) {
                            MainActivity.hideKeyboard(getActivity());
                        }
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (self).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            //here you can use the position to determine what checkbox to check
            //this assumes that you have an array of your checkboxes as well. called checkbox
            String name = adapter.getItem(position);
            CacheSchoolMananger.getInstance().addFavoriteSchool(
                DataAccessLayer.getInstance().getSchoolByName(name).Id
            );

            if (getActivity() != null) {
                MainActivity.hideKeyboard(getActivity());
            }

            FragmentTransaction mFragmentTransaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container, new FavoriteFragment()).commit();

        });

        // Set title of toolbar
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.menuSchool);
        ((MainActivity) getActivity()).navigationView.setCheckedItem(R.id.nav_map);

        return rootView;

    }


}
