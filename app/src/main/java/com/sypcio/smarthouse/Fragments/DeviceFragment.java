package com.sypcio.smarthouse.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sypcio.smarthouse.Devices;
import com.sypcio.smarthouse.ViewModel.ControlViewModel;
import com.sypcio.smarthouse.R;

public class DeviceFragment extends Fragment implements View.OnClickListener {

    private ControlViewModel mViewModel;

    private Button agdButton;
    private Button rtvButton;
    private Button lightButton;
    private Button gateButton;
    private Button acButton;
    private Button heatingButton;

    public static DeviceFragment newInstance() {
        return new DeviceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ControlViewModel.class);

        agdButton = getActivity().findViewById(R.id.agd);
        agdButton.setOnClickListener(this::onClick);

        rtvButton = getActivity().findViewById(R.id.rtv);
        rtvButton.setOnClickListener(this::onClick);

        lightButton = getActivity().findViewById(R.id.light);
        lightButton.setOnClickListener(this::onClick);

        gateButton = getActivity().findViewById(R.id.gate);
        gateButton.setOnClickListener(this::onClick);

        acButton = getActivity().findViewById(R.id.ac);
        acButton.setOnClickListener(this::onClick);

        heatingButton = getActivity().findViewById(R.id.heating);
        heatingButton.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.agd:
                subFragment((String) agdButton.getText());
                break;
            case R.id.rtv:
                subFragment((String) rtvButton.getText());
                break;
            case R.id.light:
                subFragment((String) lightButton.getText());
                break;
            case R.id.gate:
                subFragment((String) gateButton.getText());
                break;
            case R.id.ac:
                subFragment((String) acButton.getText());
                break;
            case R.id.heating:
                subFragment((String) heatingButton.getText());
                break;
        }
    }

    public void subFragment(String category){
        Intent intent = new Intent(getContext(), Devices.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
