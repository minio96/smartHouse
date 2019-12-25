package com.sypcio.smarthouse.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.sypcio.smarthouse.ViewModel.AccountViewModel;
import com.sypcio.smarthouse.LoginActivity;
import com.sypcio.smarthouse.R;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_fragment, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        Button logoutBtn = getActivity().findViewById(R.id.logoutBtn);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        // TODO: Use the ViewModel
    }

}
