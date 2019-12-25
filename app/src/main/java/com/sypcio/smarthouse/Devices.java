package com.sypcio.smarthouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class Devices extends AppCompatActivity {

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {
        ArrayList<String> result = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        for (String item : list) {
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    final ArrayList<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListView listView = new ListView(this);
        setContentView(R.layout.list_fragment);
        setContentView(listView);

        String category;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                category= null;
            } else {
                category= extras.getString("category");
            }
        } else {
            category= (String) savedInstanceState.getSerializable("category");
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("devices");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nameDevice = snapshot.getKey();
                    if(nameDevice.contains(category.toLowerCase())) {
                        nameList.add(nameDevice);
                    }
                }
                ArrayList<String> unique = removeDuplicates(nameList);
                ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(Devices.this, R.layout.list_fragment,  R.id.name, unique);
                listView.setAdapter(namesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(Devices.this, SingleDevice.class);
                intent.putExtra("key" ,nameList.get(i));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
