package me.dm7.barcodescanner.core;


import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

// This code is mostly based on the top answer here: http://stackoverflow.com/questions/18149964/best-use-of-handlerthread-over-other-similar-classes
public class CameraHandlerThread extends HandlerThread {
    private static final String LOG_TAG = "CameraHandlerThread";

    private BarcodeScannerView mScannerView;

    public CameraHandlerThread(BarcodeScannerView scannerView) {
        super("CameraHandlerThread");
        mScannerView = scannerView;
        start();
    }

    public void startCamera(final int cameraId) throws CameraOpenException {
        Handler localHandler = new Handler(Looper.getMainLooper());
        localHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    final Camera camera = CameraUtils.getCameraInstance(cameraId);
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mScannerView.setupCameraPreview(CameraWrapper.getWrapper(camera, cameraId));
                        }
                    });
                } catch (CameraOpenException coe) {
                    coe.printStackTrace();
                }
            }
        });
    }
}
