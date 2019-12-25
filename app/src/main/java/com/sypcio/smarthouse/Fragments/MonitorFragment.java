package com.sypcio.smarthouse.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sypcio.smarthouse.ViewModel.MonitorViewModel;
import com.sypcio.smarthouse.R;

import java.util.ArrayList;
import java.util.List;

public class MonitorFragment extends Fragment {

    private static int MAXBATTERY = 10000;
    public int battery;
    public int solarPanel;
    private MonitorViewModel mViewModel;
    int counterForChart = 0;
    ArrayList<Entry> entrys = new ArrayList<>();
    ArrayList<Integer> labels = new ArrayList<>();

    public int getCounterForChart() {
        return counterForChart;
    }

    public void increaseCounterForChart() {
        this.counterForChart++;
    }

    public int getSolarPanel() {
        return solarPanel;
    }

    public void setSolarPanel(int solarPanel) {
        this.solarPanel = solarPanel;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public static MonitorFragment newInstance() {
        return new MonitorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.monitor_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LineChart lineChart;
        PieChart pieChart;

        lineChart = getActivity().findViewById(R.id.lineChart);
        pieChart = getActivity().findViewById(R.id.batteryChart);

        mViewModel = ViewModelProviders.of(this).get(MonitorViewModel.class);

        DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference("batteryChargeLevel");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    int value = data.getValue(Integer.class);
                    entrys.add(new Entry( getCounterForChart(),(float)value));
                    labels.add(getCounterForChart());
                    setUpLineChart(entrys,labels,lineChart);
                    increaseCounterForChart();
                    setBattery(value);
                    setUpBatteryChart(value, pieChart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("batteryChargeLevel");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int value = dataSnapshot.getValue(Integer.class);
                setBattery(value);
                setUpBatteryChart(value, pieChart);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("ecoSources");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("dupa",dataSnapshot.getValue(Integer.class).toString());
                setSolarPanel(dataSnapshot.getValue(Integer.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

    private void setUpLineChart(ArrayList<Entry> entrys, ArrayList<Integer> labels, LineChart lineChart){
        LineDataSet dataSet = new LineDataSet(entrys,"battery level");
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.invalidate();
    }

    private void setUpBatteryChart(int batteryLevel, PieChart pieChart){

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float)batteryLevel, ""));
        entries.add(new PieEntry((float)(MAXBATTERY - batteryLevel), ""));

        PieDataSet set = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.rgb(	139,	194,	139));  //bright green
            colors.add(Color.rgb(231,230,230)); //grey
        set.setColors(colors);

        PieData data = new PieData(set);
        Description description = new Description();
        description.setText("Battery level ");
        description.setTextSize(20);
        description.setTextColor(Color.rgb(14,110,14));
        description.setPosition(350,500);
        pieChart.setDescription(description);

        pieChart.setNoDataText("Downloading data...");
        pieChart.setMaxAngle(180f);
        pieChart.setHoleRadius(35);
        pieChart.setTransparentCircleRadius(50);
        pieChart.setRotationAngle(180f);
        pieChart.setBackgroundColor(Color.WHITE);
        pieChart.setHoleColor(Color.WHITE);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        int percents = (batteryLevel/100);
        pieChart.setTouchEnabled(false);
        pieChart.setCenterText(Integer.toString(percents)+ " %");
        pieChart.setCenterTextSize(25);
        pieChart.setData(data);
        pieChart.animateY(500);
        pieChart.invalidate(); // refresh
    }


}
