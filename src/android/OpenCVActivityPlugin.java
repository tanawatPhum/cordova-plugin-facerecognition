package com.opencvactivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import com.avalant.ai.avafaceid.views.ShootActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


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
			// intent.putExtra("processId", args.getString(0));
			// intent.putExtra("title", args.getString(2));
			// JSONArray paramsJSON = args.getJSONArray(1);
			// String[] params = new String[paramsJSON.length()];
			// for (int i=0;i<params.length;i++)
			// 	params[i]=paramsJSON.getString(i);
			// intent.putExtra("processParams", params);
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
					byte[] bytesFace = intent.getByteArrayExtra("faceImg");
					String result = intent.getStringExtra("result");
					// String base64Face = Base64.encodeToString(bytesFace, Base64.DEFAULT);
					File tempFile = File.createTempFile(bytesFace.toString(), ".jpg", null);
					FileOutputStream fos = new FileOutputStream(tempFile);
					fos.write(bytesFace);
					JSONObject res = new JSONObject();
					res.put("result",result);
					res.put("faceImg",tempFile);
					res.put("faceImgOutput",fos);
					Log.d("Detect","callbackToClient");
					currentCallbackContext.success(res);
				}
				else
					currentCallbackContext.error("Request failed");
			} catch (JSONException e) {
				currentCallbackContext.error(e.getLocalizedMessage());
			} catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
	}
}
