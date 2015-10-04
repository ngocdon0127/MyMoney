package com.example.ngocdon.mymoney;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtPassword;
    TextView tvConnection;
    Button btnLogIn;
    ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        tvConnection = (TextView) findViewById(R.id.tvConnection);
        btnLogIn = (Button) findViewById(R.id.btnLogin);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        if (!isConnected) {
            txtPassword.setEnabled(false);
            btnLogIn.setEnabled(false);
        }
        else
            tvConnection.setText("");
    }

    public void logIn(View v){
        String password = txtPassword.getText().toString();
        if (password.equals(Information.MASTER_PASSWORD)){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Sai cmm pass rồi." , Toast.LENGTH_SHORT).show();
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnected());
            if (isConnected){
                txtPassword.setEnabled(true);
                btnLogIn.setEnabled(true);
                tvConnection.setText("");
            }
            else {
                txtPassword.setEnabled(false);
                btnLogIn.setEnabled(false);
                tvConnection.setText(getString(R.string.tvConnection));
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
