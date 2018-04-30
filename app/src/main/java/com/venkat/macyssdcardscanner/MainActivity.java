package com.venkat.macyssdcardscanner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.venkat.macyssdcardscanner.model.Details;
import com.venkat.macyssdcardscanner.utils.Constatnts;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnScanCompleteListener {

    @BindView(R.id.start_button)
    Button startButton;

    @BindView(R.id.stop_button)
    Button stopButton;

    @BindView(R.id.loading_indicator)
    ProgressBar progressBar;

    ScannerService mService;
    boolean mBound = false;
    Intent startIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        startIntent = new Intent(this, ScannerService.class);
        //Binding the service for communication to register this activity with service.
        bindService(startIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                //Setting the service as foreground(Notification will be shown in status bar)
                //and scan will be started.
                progressBar.setVisibility(View.VISIBLE);
                startIntent.setAction(Constatnts.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                break;
            case R.id.stop_button:
                //Service will be removed from foreground and notification will be removed.
                stopScan();
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        stopScan();
        finish();
    }

    /**
     * This will remove the service from foreground and notiification form status bar is removed.
     */
    private void stopScan() {
        progressBar.setVisibility(View.GONE);
        Intent stopIntent = new Intent(this, ScannerService.class);
        stopIntent.setAction(Constatnts.ACTION.STOPFOREGROUND_ACTION);
        startService(stopIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(startIntent);
        mService.unregisterListener();
        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ScannerService.LocalBinder binder = (ScannerService.LocalBinder) service;
            mService = binder.getService();
            //Registering the activity with service.
            mService.registerListener(MainActivity.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    //This callback is recevied from service after the scan is completed.
    @Override
    public void onScanCompleted(Details fileDetails) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });

        Fragment frag = new FileListFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable(Constatnts.FRAGMENT.EXTENSION_MAP, fileDetails.getExtMap());
        bundle.putSerializable(Constatnts.FRAGMENT.SIZE_MAP, fileDetails.getSizeMap());
        bundle.putFloat(Constatnts.FRAGMENT.AVG_SIZE, fileDetails.getAvgSize());

        frag.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(android.R.id.content, frag).commit();
    }
}
