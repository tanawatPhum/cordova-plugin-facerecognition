package co.mwater.opencvactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.avalant.ai.avafaceid.views.CameraView;
import com.avalant.ai.avafaceid.views.FaceDetectThread;
import com.avalant.ai.avafaceid.views.FaceIdentityView;


/**
 * Created by avalant on 8/29/17.
 */


public class OpenCVActivity extends Activity{

    private CameraView mCameraView;

    private FaceDetectThread mFaceDetectThread;

    private FaceIdentityView mFaceIndFaceIdentityView;

    private static final String VERIFY_URL = "http://192.168.11.100:8081/face/verify";

    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Display display = getWindowManager().getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        mFaceDetectThread = new FaceDetectThread(this);
        mFaceDetectThread.start();
        mCameraView = new CameraView(this);
        mCameraView.addCallback(mFaceDetectThread);
        mFaceIndFaceIdentityView = new FaceIdentityView(this,width, height, VERIFY_URL);
        mFaceDetectThread.addCallback(mFaceIndFaceIdentityView);
        setContentView(mCameraView);
        addContentView(mFaceIndFaceIdentityView, new LayoutParams(width, height));
    }

    public void sendMessage(String msg) {
        Intent intent=new Intent();
        intent.putExtra("result", msg);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean inAttackMode = false;

    private boolean isReady = false;

    public void openFire() {
        if (!isReady) {
            return;
        }
        if (inAttackMode) {
            return;
        }
        inAttackMode = true;

    }

    public void ceaseFire() {
        if (!isReady) {
            return;
        }

        if (!inAttackMode) {
            return;
        }
        inAttackMode = false;

    }




}
