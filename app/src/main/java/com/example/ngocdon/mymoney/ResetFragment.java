package com.example.ngocdon.mymoney;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by NgocDon on 9/19/2015.
 */
public class ResetFragment extends Fragment {

    EditText txtWallet;
    EditText txtAtm;
    EditText txtVisa;
    EditText txtDebt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reset_fragment, container, false);
        txtAtm = (EditText) v.findViewById(R.id.txtResetAtm);
        txtDebt = (EditText) v.findViewById(R.id.txtResetDebt);
        txtVisa = (EditText) v.findViewById(R.id.txtResetVisa);
        txtWallet = (EditText) v.findViewById(R.id.txtResetWallet);

        return v;
    }

    public long getWallet(){
        String w = txtWallet.getText().toString();
        if (w.length() < 1)
            return 0;
        return Long.valueOf(w);
    }

    public long getAtm(){
        String w = txtAtm.getText().toString();
        if (w.length() < 1)
            return 0;
        return Long.valueOf(w);
    }

    public long getVisa(){
        String w = txtVisa.getText().toString();
        if (w.length() < 1)
            return 0;
        return Long.valueOf(w);
    }

    public long getDebt(){
        String w = txtDebt.getText().toString();
        if (w.length() < 1)
            return 0;
        return Long.valueOf(w);
    }

}
