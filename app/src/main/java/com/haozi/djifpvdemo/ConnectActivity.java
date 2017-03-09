package com.haozi.djifpvdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.haozi.djifpvdemo.Utils.FPVDemoAppliaction;

import dji.sdk.Products.DJIAircraft;
import dji.sdk.base.DJIBaseProduct;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener,DJIBaseProduct.DJIVersionCallback{
    private Button mOpenBtn;
    private TextView mTextConnectionStatus;
    private TextView mTextProduct;
    private final static String Tag = ConnectActivity.class.getSimpleName();
    private BroadcastReceiver mReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshSDKRelativeUI();
        }
    };
    private void initUI() {

        mTextConnectionStatus = (TextView) findViewById(R.id.text_connection_status);
        mTextProduct = (TextView) findViewById(R.id.text_product_info);
        mOpenBtn = (Button) findViewById(R.id.btn_open_connect);
        mOpenBtn.setOnClickListener(this);
//        mOpenBtn.setEnabled(false);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        initUI();
        IntentFilter filter = new IntentFilter();
        filter.addAction(FPVDemoAppliaction.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(Tag, "onDestroy");
        unregisterReceiver(mReceiver);
    }
    public void refreshSDKRelativeUI(){
        DJIBaseProduct product=FPVDemoAppliaction.getProductInstance();
        Toast.makeText(this, "连接状态:"+product.isConnected()+"", Toast.LENGTH_SHORT).show();
        if (product!=null&&product.isConnected()){
            Log.v(Tag, "refreshSDK: True");
            mOpenBtn.setEnabled(true);
            String string = product instanceof DJIAircraft ? "DJIAircraft" : "DJIHandHeld";
            mTextConnectionStatus.setText("Status: " + string + " connected");
            product.setDJIVersionCallback(this);
            if (null!=product.getModel()){
                mTextProduct.setText("" + product.getModel().getDisplayName());
            } else {
                mTextProduct.setText(R.string.product_information);
            }
        }else{
            Log.v(Tag, "refreshSDK: False");
            mOpenBtn.setEnabled(false);
            mTextProduct.setText(R.string.product_information);
            mTextConnectionStatus.setText(R.string.connection_loose);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_connect: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onProductVersionChange(String s, String s1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
