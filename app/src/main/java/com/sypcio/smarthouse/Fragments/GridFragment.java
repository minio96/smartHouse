package com.sypcio.smarthouse.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sypcio.smarthouse.ViewModel.AlarmViewModel;
import com.sypcio.smarthouse.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GridFragment extends Fragment {

    private AlarmViewModel mViewModel;

    public static GridFragment newInstance() {
        return new GridFragment();
    }

    Switch modeSwitch;

    ArrayList<Entry> entrys = new ArrayList<>();
    ArrayList<Integer> labels = new ArrayList<>();
    int counterForChart = 0;

    public int getCounterForChart() {
        return counterForChart;
    }

    public void increaseCounterForChart() {
        this.counterForChart++;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        // TODO: Use the ViewModel
        LineChart lineChart;
        lineChart = getActivity().findViewById(R.id.lineChart);


        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("toGrid");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    int value = data.getValue(Integer.class);
                    entrys.add(new Entry( getCounterForChart(),(float)value));
                    labels.add(getCounterForChart());
                    setUpLineChart(entrys,labels,lineChart);
                    increaseCounterForChart();
//                    setBattery(value);
//                    setUpBatteryChart(value, pieChart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        modeSwitch = getActivity().findViewById(R.id.modeSwitch);

        databaseReference = FirebaseDatabase.getInstance().getReference("normalMode");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    int value = data.getValue(Integer.class);
                    if (value == 0){
                        modeSwitch.setChecked(false);
                    }else {
                        modeSwitch.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        modeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeSwitch.isChecked())
                    changeMode(1);
                else
                    changeMode(0);
            }
        });
    }

    public void changeMode(Integer integer){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("normalMode");
        HashMap<String,Object> map = new HashMap<>();
        map.put("normalMode",integer);
        mDatabase.updateChildren(map);
    }

    private void setUpLineChart(ArrayList<Entry> entrys, ArrayList<Integer> labels, LineChart lineChart){
        LineDataSet dataSet = new LineDataSet(entrys,"to grid");
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.invalidate();
    }

}
