package com.example.ngocdon.mymoney;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * Created by NgocDon on 9/17/2015.
 */
public class MonthlyFeeFragment extends Fragment {

    RadioGroup rgMonthlyFee;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.acc_fragment, container, false);
        rgMonthlyFee = (RadioGroup) v.findViewById(R.id.rgAcc);
        return v;
    }

    public int getType(){
        switch (rgMonthlyFee.getCheckedRadioButtonId()){
            case R.id.rbAtm:
                return Transaction.ATM_MONTHLY_FEE;
            case R.id.rbVisa:
                return Transaction.VISA_MONTHLY_FEE;
            default:
                return -1;
        }
    }

}
