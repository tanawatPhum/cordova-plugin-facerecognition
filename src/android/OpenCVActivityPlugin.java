package com.opencvactivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import com.avalant.ai.avafaceid.views.ShootActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


/*
 * Cordova/Phonegap plugin.
 *
 * Call using action "process" with parameters processId, processParams, title.
 * Call using action "processList" to get a list of processIds available
 */
public class OpenCVActivityPlugin extends CordovaPlugin {
	private static final String TAG = OpenCVActivityPlugin.class.getSimpleName();

	private static int REQUEST_OPENCV_ACTIVITY = 1234;
	CallbackContext currentCallbackContext;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if ("process".equals(action)) {
			// Launch OpenCV
			currentCallbackContext = callbackContext;
			Intent intent = new Intent(cordova.getActivity(), ShootActivity.class);
			intent.putExtra("url", args.getString(0));
			cordova.startActivityForResult(this, intent, REQUEST_OPENCV_ACTIVITY);
			return true;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_OPENCV_ACTIVITY) {
			// Call Javascript with results
			try {
				if (resultCode == Activity.RESULT_OK) {
//					byte[] faceUser = intent.getByteArrayExtra("faceUser");
          String faceUser = intent.getStringExtra("faceUser");
					String nameUser = intent.getStringExtra("nameUser");
					String idUser = intent.getStringExtra("idUser");
          Log.d("faceUser:",faceUser);

//					File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".jpg", null);
//
//					FileOutputStream fos = new FileOutputStream(tempFile);
//					fos.write(faceUser);
//          fos.flush();
//          fos.close();
//          String encodedImage = Base64.encodeToString(faceUser, Base64.DEFAULT);
					JSONObject res = new JSONObject();
          			res.put("idUser",idUser);
					res.put("nameUser",nameUser);
					res.put("faceUser",faceUser);
//					res.put("faceUserOutput",fos);
					Log.d("Detect","callbackToClient");
					currentCallbackContext.success(res);
				}
				else
					currentCallbackContext.error("Request failed");
			} catch (JSONException e) {
				currentCallbackContext.error(e.getLocalizedMessage());
			}
    }
	}
}
