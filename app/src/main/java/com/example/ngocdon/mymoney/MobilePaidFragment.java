package com.example.ngocdon.mymoney;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * Created by NgocDon on 9/15/2015.
 */
public class MobilePaidFragment extends Fragment {

    RadioGroup rgMobilePaid;
    View v;
    Spinner sTarget;
    String[] model;
    ArrayAdapter<String> adapter = null;
    private int target = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mobile_paid_fragment, container, false);
        rgMobilePaid = (RadioGroup) v.findViewById(R.id.rgMobilePaid);
        sTarget = (Spinner) v.findViewById(R.id.sTarget);
        model = new String[Information.PEOPLE.length];
        for(int i = 0; i < Information.PEOPLE.length; i++){
            model[i] = new String(Information.PEOPLE[i]);
        }
        sTarget = (Spinner) v.findViewById(R.id.sTarget);
        sTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                target = -1;
            }
        });

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, model);
        sTarget.setAdapter(adapter);
        return v;
    }

    public int getCheckedRadioButtonId(){
        return rgMobilePaid.getCheckedRadioButtonId();
    }

    public int getTarget() {
        return target;
    }
}
