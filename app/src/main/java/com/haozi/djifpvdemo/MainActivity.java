package com.haozi.djifpvdemo;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.haozi.djifpvdemo.Utils.FPVDemoAppliaction;

import java.io.IOException;

import dji.sdk.Camera.DJICamera;
import dji.sdk.Codec.DJICodecManager;
import dji.sdk.base.DJIBaseProduct;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private TextureView mTextureView;
    private Button mCapture;
    protected TextureView mVideoSurface = null;
    private Button mCaptureBtn, mShootPhotoModeBtn, mRecordVideoModeBtn;
    private ToggleButton mRecordBtn;
    private TextView recordingTime;
    private DJICamera.CameraReceivedVideoDataCallback mCameraReceivedVideoDataCallback = null;
    private DJICodecManager mCodecManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        mCameraReceivedVideoDataCallback = new DJICamera.CameraReceivedVideoDataCallback() {
            //receive callback for the raw h264 video data for camera live view;
//            接收无人机回传的实时视频
            @Override
            public void onResult(byte[] videoBuffer, int size) {
                if (mCodecManager != null) {
                    mCodecManager.sendDataToDecoder(videoBuffer, size);
                } else {
                    Log.e(TAG, "mCodecManager is null");
                }

            }
        };
    }

    //手机显示准备
    private void initPreviewer() {
        DJIBaseProduct product = FPVDemoAppliaction.getProductInstance();
        if (product == null || !product.isConnected()) {
            Toast.makeText(this, getString(R.string.disconnected), Toast.LENGTH_SHORT).show();
        } else {
            if (mTextureView != null) {
                mTextureView.setSurfaceTextureListener(this);
            }
            if (!product.getModel().equals(DJIBaseProduct.Model.UnknownAircraft)) {
                DJICamera camera = FPVDemoAppliaction.getCameraInstance();
                if (camera != null) {
                    camera.setDJICameraReceivedVideoDataCallback(mCameraReceivedVideoDataCallback);
                }
            }
        }

    }

    private void uninitPreviewer() {
        DJICamera camera = FPVDemoAppliaction.getCameraInstance();
        if (camera != null) {
            // Reset the callback
            FPVDemoAppliaction.getCameraInstance().setDJICameraReceivedVideoDataCallback(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "on resume");
        initPreviewer();
        if (mTextureView == null) {
            Log.e(TAG, "mVideoSurface is null");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onReturn() {

    }

    protected void onProductChange() {
        initPreviewer();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //    对所有的UI组件进行初始化
    private void initUI() {
        // init mVideoSurface
        mVideoSurface = (TextureView) findViewById(R.id.video_previewer_surface);
        recordingTime = (TextView) findViewById(R.id.timer);
        mCaptureBtn = (Button) findViewById(R.id.btn_capture);
        mRecordBtn = (ToggleButton) findViewById(R.id.btn_record);
        mShootPhotoModeBtn = (Button) findViewById(R.id.btn_shoot_photo_mode);
        mRecordVideoModeBtn = (Button) findViewById(R.id.btn_record_video_mode);
        if (null != mVideoSurface) {
            mVideoSurface.setSurfaceTextureListener(this);
        }

        mCaptureBtn.setOnClickListener(this);
        mRecordBtn.setOnClickListener(this);
        mShootPhotoModeBtn.setOnClickListener(this);
        mRecordVideoModeBtn.setOnClickListener(this);
        recordingTime.setVisibility(View.INVISIBLE);
        mRecordBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }


    //手机本地相机
    private void nativeCameraStartMed(SurfaceTexture surface) {
        Camera mCamera;
        mCamera = Camera.open();
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        mCamera.startFaceDetection();
        mTextureView.setAlpha(0.5f);
        mTextureView.setRotation(90f);
    }

    private void nativeCameraStopMed(Camera mCamera, SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
    }

    //surfaceTureListener方法
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureAvailable");
        if (mCodecManager==null){
            mCodecManager=new DJICodecManager(this,surface,width,height);
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e(TAG,"onSurfaceTextureDestroyed");
        if (mCodecManager != null) {
            mCodecManager.cleanSurface();
            mCodecManager = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_capture: {
                break;
            }
            case R.id.btn_shoot_photo_mode: {
                break;
            }
            case R.id.btn_record_video_mode: {
                break;
            }
            default:
                break;
        }
    }

}
