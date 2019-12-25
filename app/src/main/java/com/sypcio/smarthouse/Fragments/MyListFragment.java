package com.sypcio.smarthouse.Fragments;
import android.annotation.SuppressLint;
import android.support.v4.app.ListFragment;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sypcio.smarthouse.R;

import java.util.ArrayList;
import java.util.List;

public class MyListFragment extends ListFragment implements OnItemClickListener {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> nameList = new ArrayList<>();
        Bundle arguments = getArguments();
        String category = arguments.getString("key").toLowerCase();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("devices");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nameDevice = snapshot.getKey();
                    if(nameDevice.contains(category)) {
                        nameList.add(nameDevice);
                    }
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_fragment, nameList);
                setListAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value  TRZEBA OBSŁUŻYĆ
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: dupa" , Toast.LENGTH_SHORT).show();
    }


}