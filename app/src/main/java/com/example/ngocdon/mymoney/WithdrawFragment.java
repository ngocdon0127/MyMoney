package com.example.ngocdon.mymoney;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * Created by NgocDon on 9/16/2015.
 */
public class WithdrawFragment extends Fragment {

    RadioGroup rgWithdraw;
    RadioGroup rgWithdrawPos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.withdraw_fragment, container, false);
        rgWithdraw = (RadioGroup) v.findViewById(R.id.rgWithdraw);
        rgWithdrawPos = (RadioGroup) v.findViewById(R.id.rgWithdrawPos);
        return v;
    }

    public int getWithdrawAcc(){
        switch (rgWithdraw.getCheckedRadioButtonId()){
            case R.id.rbwithdrawAtm:
                return Transaction.ATM_WITHDRAW;
            case R.id.rbwithdrawVisa:
                return Transaction.VISA_WITHDRAW;
        }
        return -1;
    }

    public int getPos(){
        switch (rgWithdrawPos.getCheckedRadioButtonId()){
            case R.id.rbWithdrawLocalPos:
                return Transaction.LOCAL_POS;
            case R.id.rbWithdrawBroadPos:
                return Transaction.BROAD_POS;
        }
        return -1;
    }

}
