package com.example.sundforluft.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sundforluft.DAO.ClassroomModel;
import com.example.sundforluft.R;

import java.util.ArrayList;

public class TeacherCloudsOverviewAdapter extends BaseAdapter {

    ArrayList<ClassroomModel> classrooms;
    Context context;

    public TeacherCloudsOverviewAdapter(Context context, ArrayList<ClassroomModel> models) {
        this.context = context;
        this.classrooms = models;
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
        view = inflater.inflate(R.layout.activity_teacher_clouds_overview_listview, parent, false);

        TextView tvleft = view.findViewById(R.id.teacherCloudOverviewTextLeft);
        TextView tvright = view.findViewById(R.id.teacherCloudOverviewTextRight);

        ClassroomModel model = classrooms.get(position);
        tvleft.setText(model.deviceId);
        tvright.setText(model.name);

        return view;
    }
}
