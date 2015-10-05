package com.example.ngocdon.mymoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by NgocDon on 9/15/2015.
 */
public class HomeActivity extends TabActivity {

    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    ArrayAdapter<Transaction> adapterTransaction = null;
    String[] model;
    ArrayAdapter<String> adapter = null;
    Spinner sTypeTransaction;
    TextView tvWallet;
    TextView tvAtm;
    TextView tvVisa;
    TextView tvDebt;
    TextView tvAvailable;
    EditText txtMoney;
    FrameLayout flTransaction;
    Fragment fragment;
    ListView lvTransactions;
    ProgressDialog progressDialog;
    Handler handler = new Handler();

    private int transactionType = 0;
    private int target = 0;
    private String details;
	private long wallet = 0;
	private long atm = 0;
	private long visa = 0;
	private long debt = 0;
	private Date date;
	private int position = 0;
	private long money = 0;
    private int tType = 0;
    private int progress = 0;

    public static int flagOrientation;

    private Transaction firstTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.home);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if ((activeNetwork == null) || (!activeNetwork.isConnected())){
            finish();
        }

        Configuration configuration = getResources().getConfiguration();
        flagOrientation = configuration.orientation;
        System.out.println(flagOrientation);
        setTitle("Tab Main");
        TabHost tabHost = getTabHost();
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.home, tabHost.getTabContentView(), true);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Main").setContent(R.id.rlMain));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("All Transaction").setContent(R.id.lvTransactions));

        tvWallet = (TextView) findViewById(R.id.tvWallet);
        tvAtm = (TextView) findViewById(R.id.tvAtm);
        tvVisa = (TextView) findViewById(R.id.tvVisa);
        tvDebt = (TextView) findViewById(R.id.tvDebt);
        tvAvailable = (TextView) findViewById(R.id.tvAvailable);
        tvAvailable.setTypeface(null, Typeface.BOLD);
        sTypeTransaction = (Spinner) findViewById(R.id.sTypeTransaction);
        flTransaction = (FrameLayout) findViewById(R.id.flTransaction);
        txtMoney = (EditText) findViewById(R.id.txtMoney);
        lvTransactions = (ListView) findViewById(R.id.lvTransactions);

        list();

        model = new String[Information.TRANSACTION_TYPE.length];
        for(int i = 0; i < Information.TRANSACTION_TYPE.length; i++){
            model[i] = new String(Information.TRANSACTION_TYPE[i]);
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, model);

        sTypeTransaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                tType = position;
                //Toast.makeText(getApplicationContext(), String.valueOf(transactionType), Toast.LENGTH_SHORT).show();
                switch (position){
                    case Transaction.TYPE_MOBILE_PAID:
                        fragment = new MobilePaidFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_WITHDRAW:
                        fragment = new WithdrawFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_RECHARGE:
                        fragment = new RechargeFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_MONTHLY_FEE:
                        fragment = new MonthlyFeeFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_ONLINE_PAID:
                        fragment = new OnlinePaidFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_BORROW:
                        fragment = new BorrowFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_PAY_BACK:
                        fragment = new PayBackFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_INCREASE:
                        fragment = new IncreaseFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_DECREASE:
                        fragment = new DecreaseFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    case Transaction.TYPE_RESET_ALL:
                        fragment = new ResetFragment();
                        fragmentTransaction.replace(R.id.flTransaction, fragment);
                        fragmentTransaction.commit();
                        break;
                    default:
                        flTransaction.removeAllViews();
                        fragmentTransaction.commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                transactionType = -1;
            }
        });

        lvTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), transactions.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        sTypeTransaction.setAdapter(adapter);
        adapterTransaction = new TransactionAdapter();
        lvTransactions.setAdapter(adapterTransaction);
    }

    class TransactionAdapter extends ArrayAdapter<Transaction>{
        TransactionAdapter(){
            super(getApplicationContext(), android.R.layout.simple_list_item_1, transactions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            TransactionHolder transactionHolder = null;

            if (row == null){
                row = getLayoutInflater().inflate(R.layout.row, parent, false);
                transactionHolder = new TransactionHolder(row);
                row.setTag(transactionHolder);
            }
            else {
                transactionHolder = (TransactionHolder) row.getTag();
            }

            transactionHolder.populateFrom(transactions.get(position));
            return row;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    static class TransactionHolder{
        private TextView tvRowMoney;
        private TextView tvRowDate;
        private TextView tvRowDetails;

        TransactionHolder(View row){
            tvRowDate = (TextView) row.findViewById(R.id.tvRowDate);
            tvRowDetails = (TextView) row.findViewById(R.id.tvRowDetails);
            tvRowMoney = (TextView) row.findViewById(R.id.tvRowMoney);
        }

        void populateFrom(Transaction t){
            tvRowMoney.setText(Transaction.format(t.getMoney()));
            tvRowDetails.setText(t.getDetail());
            if (HomeActivity.flagOrientation == Configuration.ORIENTATION_PORTRAIT){
                tvRowDate.setText(t.getDateString());
            }
            else if (HomeActivity.flagOrientation == Configuration.ORIENTATION_LANDSCAPE){
                tvRowDate.setText(t.getTimeString());
            }
        }
    }

    private long getMoney(){
        String s = txtMoney.getText().toString();
        if (s.length() > 0)
            return Long.valueOf(s);
        return 0;
    }

    public void addTransaction(View v){
        this.money = this.getMoney();
        this.transactionType = 0;
        this.target = 0;
        this.details = null;
        this.wallet = 0;
        this.atm = 0;
        this.visa = 0;
        this.debt = 0;
        this.position = 0;
        this.date = new Date();
        if ((tType != Transaction.TYPE_RESET_ALL) && (this.money <= 0)) {
            Toast.makeText(getApplicationContext(), "Nhập tiền nhiều lên :v", Toast.LENGTH_SHORT).show();
            return;
        }
        int result = -1;
        switch (tType){
            case Transaction.TYPE_MOBILE_PAID:
                result = addMobilePaid();
                break;
            case Transaction.TYPE_WITHDRAW:
                result = addWithdraw();
                break;
            case Transaction.TYPE_RECHARGE:
                result = addRecharge();
                break;
            case Transaction.TYPE_MONTHLY_FEE:
                result = addMonthlyFee();
                break;
            case Transaction.TYPE_ONLINE_PAID:
                result = addOnlinePaid();
                break;
            case Transaction.TYPE_BORROW:
                result = addBorrow();
                break;
            case Transaction.TYPE_PAY_BACK:
                result = addPayBack();
                break;
            case Transaction.TYPE_INCREASE:
                result = addIncrease();
                break;
            case Transaction.TYPE_DECREASE:
                result = addDecrease();
                break;
            case Transaction.TYPE_RESET_ALL:
                reset();
                return;
        }
        if (result != 0)
            return;
        Transaction t = transactions.get(transactions.size() - 1);
        Transaction t1 = new Transaction(this.transactionType, this.money, new Date(), this.details, this.target, this.position, t.getWallet(), t.getAtm(), t.getVisa(), t.getDebt());
        t1.applyTransaction();
        addToDatabase(t1);
        transactions.add(t1);
    }

    private void reset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Reset all?");
        builder.setMessage("Reset all? Really?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResetFragment rf = (ResetFragment) fragment;
                long w = rf.getWallet();
                long a = rf.getAtm();
                long v = rf.getVisa();
                long d = rf.getDebt();
                transactions.clear();
                firstTransaction = new Transaction(Transaction.RESET, 0, new Date(), "Reset", 0, 0, w, a, v, d);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection conn = DriverManager.getConnection(Information.DB_SERVER, Information.DB_USER, Information.DB_PASSWORD);
                            String sql = "DELETE FROM " + Information.TABLE_TRANSACTION + " WHERE 1";
                            Statement st = conn.createStatement();
                            st.executeUpdate(sql);
                            Intent intent = new Intent(Information.KEY_BROADCAST_RESET);
                            sendBroadcast(intent);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int addMobilePaid(){
        MobilePaidFragment f = (MobilePaidFragment) fragment;
        int target = f.getTarget();
        if (target < 0){
            Toast.makeText(getApplicationContext(), "Chọn People", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.target = target;
        this.position = Transaction.MOBILE_PAID_VIA_ATM;
        switch (f.getCheckedRadioButtonId()){
            case R.id.rbMobilePaidViaAtm:
                this.position = Transaction.MOBILE_PAID_VIA_ATM;
                break;
            case R.id.rbMobilePaidViaThangUncle:
                this.position = Transaction.MOBILE_PAID_VIA_THANG_UNCLE;
                break;
            case -1:
                Toast.makeText(getApplicationContext(), "Choose type of Mobile Paid", Toast.LENGTH_SHORT).show();
                return -1 ;
        }
        this.transactionType = Transaction.MOBILE_PAID;
        this.details = "Nạp tiền điện thoại";
        if (this.target > 0)
            this.details += " cho " + Information.PEOPLE[this.target];
        return 0;
    }

    private int addWithdraw(){
        WithdrawFragment wf = (WithdrawFragment) fragment;
        int pos = wf.getPos();
        if (pos == -1){
            Toast.makeText(getApplicationContext(), "Chọn vị trí rút", Toast.LENGTH_SHORT).show();
            return -1;
        }
        int acc = wf.getWithdrawAcc();
        if (acc == -1){
            Toast.makeText(getApplicationContext(), "Chọn tài khoản rút", Toast.LENGTH_SHORT).show();
            return -1;
        }
        Transaction t = transactions.get(transactions.size() - 1);
        this.position = pos;
        this.transactionType = acc;
        this.details = "Rút tiền từ tài khoản " + ((this.transactionType == Transaction.ATM_WITHDRAW) ? "ATM" : "Visa");
        this.target = 0;
        return 0;
    }

    private int addRecharge(){
        RechargeFragment rf = (RechargeFragment) fragment;
        int acc = rf.getRechargeAcc();
        if (acc == -1){
            Toast.makeText(getApplicationContext(), "Chọn tài khoản", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.transactionType = acc;
        this.details = "Chuyển tiền vào tài khoản " + ((this.transactionType == Transaction.ATM_RECHARGE) ? "ATM" : "Visa");
        this.position = 0;
        this.target = 0;
        return 0;
    }

    private int addMonthlyFee(){
        MonthlyFeeFragment mff = (MonthlyFeeFragment) fragment;
        int type = mff.getType();
        if (type == -1){
            Toast.makeText(getApplicationContext(), "Chọn tài khoản", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.transactionType = type;
        this.details = new String("Cước tài khoản " + ((this.transactionType == Transaction.ATM_MONTHLY_FEE) ? "ATM" : "Visa"));
        this.target = 0;
        this.position = 0;
        return 0;
    }

    private int addOnlinePaid(){
        OnlinePaidFragment mff = (OnlinePaidFragment) fragment;
        int type = mff.getType();
        if (type == -1){
            Toast.makeText(getApplicationContext(), "Chọn tài khoản", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.transactionType = type;
        this.details = new String("Thanh toán online bằng tài khoản " + ((this.transactionType == Transaction.ATM_ONLINE_PAID) ? "ATM" : "Visa"));
        this.target = 0;
        this.position = 0;
        return 0;
    }

    private int addBorrow(){
        BorrowFragment bf = (BorrowFragment) fragment;
        int target = bf.getTarget();
        if (target < 1){
            Toast.makeText(getApplicationContext(), "Chọn People", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.transactionType = Transaction.BORROW;
        this.target = target;
        this.details = "Vay " + Information.PEOPLE[this.target];
        this.position = 0;
        return 0;
    }

    private int addPayBack(){
        PayBackFragment bf = (PayBackFragment) fragment;
        int target = bf.getTarget();
        if (target < 1){
            Toast.makeText(getApplicationContext(), "Chọn People", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.target = target;
        this.transactionType = Transaction.PAY_BACK;
        this.details = "Trả nợ " + Information.PEOPLE[this.target];
        this.position = 0;
        return 0;
    }

    private int addIncrease(){
        IncreaseFragment ifr = (IncreaseFragment) fragment;

        int target = ifr.getTarget();
        if (target < 0){
            Toast.makeText(getApplicationContext(), "Chọn People", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.target = target;
        String details = ifr.getDetails();
        if (details.length() == 0){
            Toast.makeText(getApplicationContext(), "Chọn Details", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.details = details;
        this.transactionType = Transaction.INCREASE;
        this.position = 0;
        return 0;
    }

    private int addDecrease(){
        DecreaseFragment df = (DecreaseFragment) fragment;
        int target = df.getTarget();
        if (target < 0){
            Toast.makeText(getApplicationContext(), "Chọn People", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.target = target;
        String details = df.getDetails();
        if (details.length() == 0){
            Toast.makeText(getApplicationContext(), "Nhập Details", Toast.LENGTH_SHORT).show();
            return -1;
        }
        this.details = details;
        this.transactionType = Transaction.DECREASE;
        this.position = 0;
        return 0;
    }

    private void updateBalance(){
        Transaction t = transactions.get(transactions.size() - 1);
//        tvDebt.setText(String.valueOf(t.getDebt()));
//        tvVisa.setText(String.valueOf(t.getVisa()));
//        tvAtm.setText(String.valueOf(t.getAtm()));
//        tvWallet.setText(String.valueOf(t.getWallet()));
//        tvAvailable.setText(String.valueOf(t.countAvailable()));
        tvAvailable.setText(Transaction.format(t.countAvailable()));
        tvWallet.setText(Transaction.format(t.getWallet()));
        tvAtm.setText(Transaction.format(t.getAtm()));
        tvVisa.setText(Transaction.format(t.getVisa()));
        tvDebt.setText(Transaction.format(t.getDebt()));
    }

    public void discardLastTransaction(View v){
        if (transactions.size() == 1)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Disard Transaction");
        builder.setMessage("Discart last transaction? Sure?");
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection conn = DriverManager.getConnection(Information.DB_SERVER, Information.DB_USER, Information.DB_PASSWORD);
                            String sql = "SELECT MAX(id) AS max FROM " + Information.TABLE_TRANSACTION;
                            Statement st = conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            rs.next();
                            int maxid = rs.getInt("max");
                            sql = "DELETE FROM " + Information.TABLE_TRANSACTION + " WHERE id = ?";
                            PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setInt(1, maxid);
                            int result = pst.executeUpdate();
                            Intent intent = new Intent(Information.KEY_BROADCAST_DISCARD_TRANSACTION);
                            intent.putExtra(Information.KEY_DISCARD_RESULT, result);
                            sendBroadcast(intent);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addToDatabase(final Transaction t){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(Information.KEY_BROADCAST_DONE_INSERT_TRANSACTION);
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(Information.DB_SERVER, Information.DB_USER, Information.DB_PASSWORD);
                    String sql = "INSERT INTO " + Information.TABLE_TRANSACTION + " (type, money, date, details, target, position, wallet, atm, visa, debt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, t.getType());
                    pst.setLong(2, t.getMoney());
                    Date date = t.getDate();
                    pst.setTimestamp(3, new Timestamp(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), 0));
                    pst.setString(4, t.getDetail());
                    pst.setInt(5, t.getTarget());
                    pst.setInt(6, t.getPosition());
                    pst.setLong(7, t.getWallet());
                    pst.setLong(8, t.getAtm());
                    pst.setLong(9, t.getVisa());
                    pst.setLong(10, t.getDebt());
                    pst.executeUpdate();
                    intent.putExtra(Information.KEY_INSERT_RESULT, 0);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Class Exception");
                    intent.putExtra(Information.KEY_INSERT_RESULT, -1);
                }
                catch (SQLException e){
                    System.out.println("SQL Exception");
                    intent.putExtra(Information.KEY_INSERT_RESULT, -1);
                }
                sendBroadcast(intent);
            }
        }).start();
    }

    private void list(){
        progress = 0;
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMax(1);
        progressDialog.setMessage("Reading data from server...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setProgress(progress);
                        }
                    });
                    if (progress == 1)
                        break;
                }
                progressDialog.dismiss();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(Information.DB_SERVER, Information.DB_USER, Information.DB_PASSWORD);
                    String sql = "SELECT * FROM " + Information.TABLE_TRANSACTION + " ORDER BY id ASC";
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    Transaction t;
                    Date date;
                    transactions.clear();
                    while (rs.next()){
                        Timestamp ts = rs.getTimestamp("date");
                        date = new Date(ts.getYear(), ts.getMonth(), ts.getDate(), ts.getHours(), ts.getMinutes(), ts.getSeconds());
                        t = new Transaction(rs.getInt("type"), rs.getLong("money"), date, rs.getString("details"), rs.getInt("target"), rs.getInt("position"), rs.getLong("wallet"), rs.getLong("atm"), rs.getLong("visa"), rs.getLong("debt"));
                        transactions.add(t);
                    }
                    System.out.println("done read from server");
                    Intent intent = new Intent(Information.KEY_BROADCAST_DONE_LIST);
                    sendBroadcast(intent);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Class Exception");
                }
                catch (SQLException e){
                    System.out.println("SQL Exception");
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(Information.KEY_BROADCAST_DONE_LIST));
        registerReceiver(receiver, new IntentFilter(Information.KEY_BROADCAST_DISCARD_TRANSACTION));
        registerReceiver(receiver, new IntentFilter(Information.KEY_BROADCAST_DONE_INSERT_TRANSACTION));
        registerReceiver(receiver, new IntentFilter(Information.KEY_BROADCAST_RESET));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Information.KEY_BROADCAST_DONE_LIST)){
                progress = 1;
                updateBalance();
//                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                return;
            }
            if (intent.getAction().equals(Information.KEY_BROADCAST_DISCARD_TRANSACTION)){
                int result = intent.getIntExtra(Information.KEY_DISCARD_RESULT, 0);
                if (result < 1){
                    Toast.makeText(getApplicationContext(), "Failed to discard transaction", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Discard last transaction successfully", Toast.LENGTH_SHORT).show();
                    transactions.remove(transactions.size() - 1);
                    updateBalance();
                }
                return;
            }
            if (intent.getAction().equals(Information.KEY_BROADCAST_DONE_INSERT_TRANSACTION)){
                int result = intent.getIntExtra(Information.KEY_INSERT_RESULT, -1);
                String toast;
                if (result == -1) {
                    toast = new String("Failed to update Database. Try again later.");
                    transactions.remove(transactions.size() - 1);
                }
                else
                    toast = new String("Transaction has been added");
                updateBalance();
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                return;
            }
            if (intent.getAction().equals(Information.KEY_BROADCAST_RESET)){
                Toast.makeText(getApplicationContext(), "Reset Successfully.", Toast.LENGTH_SHORT).show();
                transactions.add(firstTransaction);
                addToDatabase(firstTransaction);
            }
        }
    };

    public void refresh(View v){
        list();
    }

}
