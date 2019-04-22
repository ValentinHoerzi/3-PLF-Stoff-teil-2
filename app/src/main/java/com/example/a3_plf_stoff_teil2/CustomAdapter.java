package com.example.a3_plf_stoff_teil2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    List<Student> data;
    int layoutId;
    LayoutInflater inflater;

    public CustomAdapter(Context context, int layoutId, List<Student> data){
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Student getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Student student = data.get(position);

        View listItem = (view==null)
                ? inflater.inflate(layoutId,null)
                : view;

        ((TextView)listItem.findViewById(R.id._textViewName)).setText(student.getName());
        ((TextView)listItem.findViewById(R.id._textViewAge)).setText(String.valueOf(student.getAge()));
        ((TextView)listItem.findViewById(R.id._textViewGraduated)).setText( student.isGraduated()
                ?"Graduated"
                :"Not Graduated"
        );
        ((TextView)listItem.findViewById(R.id._textViewJoinedSchool)).setText(MainActivity.simpleDateFormat.format(student.getJoinedSchool()));
        ((TextView)listItem.findViewById(R.id._textViewSchool)).setText(student.getSchoolName());
        ((TextView)listItem.findViewById(R.id._textViewSubjects)).setText(String.valueOf(student.getNumberOfSubjects()));

        return listItem;
    }
}
