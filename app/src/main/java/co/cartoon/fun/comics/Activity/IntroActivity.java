package co.cartoon.fun.comics.Activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.google.firebase.iid.FirebaseInstanceId;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import co.cartoon.fun.comics.R;

import static android.os.Build.VERSION.SDK_INT;


public class IntroActivity extends Activity {
	public Handler handler;
	public Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		context = this;
        get_fcm_token();
		adstatus_async = new Adstatus_Async();
		adstatus_async.execute();
	}

	Adstatus_Async adstatus_async = null;
	public class Adstatus_Async extends AsyncTask<String, Integer, String> {
		int menu_id;
		String menu_status;
		HttpURLConnection localHttpURLConnection;
		public Adstatus_Async(){
		}
		@Override
		protected String doInBackground(String... params) {
			String sTag;
			try{
				String str = "http://cion49235.cafe24.com/marketingis/manafan/menu_status.php";
				localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
				localHttpURLConnection.setFollowRedirects(true);
				localHttpURLConnection.setConnectTimeout(15000);
				localHttpURLConnection.setReadTimeout(15000);
				localHttpURLConnection.setRequestMethod("GET");
				localHttpURLConnection.connect();
				InputStream inputStream = new URL(str).openStream();
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(inputStream, "EUC-KR");
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
					}else if (eventType == XmlPullParser.END_DOCUMENT) {
					}else if (eventType == XmlPullParser.START_TAG){
						sTag = xpp.getName();
						if(sTag.equals("Menu")){
							menu_id = Integer.parseInt(xpp.getAttributeValue(null, "menu_id") + "");
						}else if(sTag.equals("menu_status")){
							menu_status = xpp.nextText()+"";
							Log.i("dsu", "menu_status : " + menu_status);
						}
					} else if (eventType == XmlPullParser.END_TAG){
						sTag = xpp.getName();
						if(sTag.equals("Finish")){
						}
					} else if (eventType == XmlPullParser.TEXT) {
					}
					eventType = xpp.next();
				}
			}
			catch (SocketTimeoutException localSocketTimeoutException)
			{
			}
			catch (ClientProtocolException localClientProtocolException)
			{
			}
			catch (IOException localIOException)
			{
			}
			catch (Resources.NotFoundException localNotFoundException)
			{
			}
			catch (NullPointerException NullPointerException)
			{
			}
			catch (Exception e)
			{
			}
			return menu_status;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String menu_status) {
			super.onPostExecute(menu_status);
			if(menu_status.equals("Y")){
				if ( SDK_INT >= Build.VERSION_CODES.M ) {
					grantPermissionsStorage();
				}else{
					handler = new Handler();
					handler.postDelayed(runnable, 1500);
				}
			}else{
				Intent intent = new Intent(context, FakeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}

    private String fcm_token = "";
    private String get_fcm_token(){
        fcm_token = FirebaseInstanceId.getInstance().getToken();
        return  fcm_token;
    }

	public void grantPermissionsStorage() {
		if ( SDK_INT >= Build.VERSION_CODES.M ) {
			if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
					// Explain to the user why we need to write the permission.
					Return_AlertShow(getString(R.string.permission_cancel), this);
				}
				requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
			} else{
				handler = new Handler();
				handler.postDelayed(runnable, 1500);
			}
		}
	}

	public void Return_AlertShow(String msg, Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setNeutralButton(activity.getString(R.string.txt_finish_yes), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				finish();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		myAlertDialog.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case 100:
				try{
					if ( grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						handler = new Handler();
						handler.postDelayed(runnable, 1500);
					} else {
						Return_AlertShow(getString(R.string.permission_cancel), this);
					}
					break;
				}catch (ArrayIndexOutOfBoundsException e){
				}catch (Exception e){
				}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}

		if(adstatus_async != null){
			adstatus_async.cancel(true);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
		    fcm_token = get_fcm_token();
            Log.i("dsu", "fcm_token : " + fcm_token);
			Intent intent = new Intent(context, BrowserActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (handler != null) handler.removeCallbacks(runnable);
		finish();
	}
}