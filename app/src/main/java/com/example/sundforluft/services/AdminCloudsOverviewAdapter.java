package com.example.sundforluft.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.DAO.SchoolModel;
import com.example.sundforluft.R;

import java.util.ArrayList;

public class AdminCloudsOverviewAdapter extends BaseAdapter {

    ArrayList<ClassroomModel> classrooms;
    SchoolModel[] schools;
    Context context;

    public AdminCloudsOverviewAdapter(Context context, ArrayList<ClassroomModel> classroomModels, SchoolModel[] schoolModels) {
        this.context = context;
        this.classrooms = classroomModels;
        this.schools = schoolModels;
    }

    @Override
    public int getCount() {
        return classrooms.size();
    }

    @Override
    public Object getItem(int position) {
        return classrooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_admin_all_schools_listview, parent, false);

        TextView tvleft = view.findViewById(R.id.adminCloudOverviewTextLeft);
        TextView tvmid = view.findViewById(R.id.adminCloudOverviewTextMid);
        TextView tvright = view.findViewById(R.id.adminCloudOverviewTextRight);

        ClassroomModel model = classrooms.get(position);
        tvleft.setText(model.deviceId);
        tvmid.setText(schools[model.id].Name);
        tvright.setText(model.name);

        return view;
    }
}