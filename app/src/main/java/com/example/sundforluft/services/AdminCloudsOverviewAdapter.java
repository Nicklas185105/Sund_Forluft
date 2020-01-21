package com.example.sundforluft.services;

import android.annotation.SuppressLint;
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
import java.util.Arrays;

public class AdminCloudsOverviewAdapter extends BaseAdapter {

    ArrayList<ClassroomModel> classrooms;
    ArrayList<SchoolModel> schools;
    Context context;

    public AdminCloudsOverviewAdapter(Context context, ArrayList<ClassroomModel> classroomModels, SchoolModel[] schoolModels) {
        this.context = context;
        this.classrooms = classroomModels;
        this.schools = new TypeArrayCaster<SchoolModel> (Arrays.stream(schoolModels).filter(c -> c.Id !=0).toArray()).getOutput();
    }

    @Override
    public int getCount() {
        return schools.size();
    }

    @Override
    public Object getItem(int position) {
        return schools.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_admin_all_schools_listview, parent, false);

        TextView tvleft = view.findViewById(R.id.adminCloudOverviewTextLeft);
        TextView tvmid = view.findViewById(R.id.adminCloudOverviewTextMid);

        StringBuilder builder = new StringBuilder();
        tvleft.setText(schools.get(position).Name);

        ArrayList<ClassroomModel> classroomModels  = new TypeArrayCaster<ClassroomModel>(
                classrooms.stream().filter(e -> e.id == schools.get(position).Id)
                        .toArray()).getOutput();

        for (ClassroomModel classroomModel : classroomModels){
            if (builder.length() == 0){
                builder.append(classroomModel.name);
            } else {
                builder.append(String.format(", %s", classroomModel.name));
            }
        }

        tvmid.setText(builder.toString());

        return view;
    }
}