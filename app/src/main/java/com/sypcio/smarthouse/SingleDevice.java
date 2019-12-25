package com.sypcio.smarthouse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class SingleDevice extends AppCompatActivity {
    HashMap<String,Integer> allDevices = new HashMap<>();
    DatabaseReference databaseReference;
    TextView name;
    TextView value;
    Switch aSwitch;
    TextView onoff;
    String device;
    int deviceValue;

    public int getDeviceValue() {
        return deviceValue;
    }

    public void setDeviceValue(int deviceValue) {
        this.deviceValue = deviceValue;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_device);
        name = findViewById(R.id.singledevice);
        value = findViewById(R.id.singledevicevalue);
        aSwitch = findViewById(R.id.aSwitch);
        onoff = findViewById(R.id.onoff);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                device= null;
            } else {
                setDevice(extras.getString("key"));
            }
        } else {
            setDevice((String) savedInstanceState.getSerializable("key"));
        }

        allDevices.put("ac",200);
        allDevices.put("gate_1",10);
        allDevices.put("heating",200);
        allDevices.put("light_0",5);
        allDevices.put("light_1",5);
        allDevices.put("light_2",8);
        allDevices.put("refrigerator_agd",300);
        allDevices.put("tv_rtv",30);

        databaseReference = FirebaseDatabase.getInstance().getReference("devices");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nameDevice = snapshot.getKey();
                    if(nameDevice.equals(device.toLowerCase())) {
                        name.setText(device);
                        if(nameDevice.contains("gate")){
                            onoff.setText("Open/Close");
                        }
                        int tempValue = snapshot.getValue(Integer.class);
                        value.setText(Integer.toString(tempValue));
                        setDeviceValue(tempValue);
                        if(tempValue==0){
                            aSwitch.setChecked(false);
                        }else {
                            aSwitch.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int consumption = allDevices.get(device);
                if(getDeviceValue() == 0){
                    setDeviceValue(consumption);
                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference("devices");
                    HashMap<String,Object> map = new HashMap<>();
                    map.put(device,consumption);
                    mDatabase.updateChildren(map);
                }else {
                    setDeviceValue(0);
                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference("devices");
                    HashMap<String,Object> map = new HashMap<>();
                    map.put(device,0);
                    mDatabase.updateChildren(map);
                }

            }
        });
    }
}
