package application.android.com.watchthewatchers.OSMConnection;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import application.android.com.watchthewatchers.AddNewCameraActivity;
import application.android.com.watchthewatchers.OSMConnection.models.SampleModel;
import application.android.com.watchthewatchers.OSMConnection.models.SampleModelDao;
import application.android.com.watchthewatchers.R;
import application.android.com.watchthewatchers.Core.SurveillanceCam;
import application.android.com.watchthewatchers.IOFormat.CreateOsmNode;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpResponseException;

public class LoginActivity extends OAuthLoginActionBarActivity<RestClient> {

	private static final String TAG = "LoginActivity";
	MyDatabase myDatabase;
	private AsyncHttpResponseHandler changesetHandler;
	private AsyncHttpResponseHandler createNodeHandler;
	private SurveillanceCam scam;
	private TextView OauthLabel;
	private Button uploadNodeButton;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		uploadNodeButton = findViewById(R.id.uploadNodeButton);
		OauthLabel = findViewById(R.id.OauthLabel);
		//Code from RestApplication
		myDatabase = Room.databaseBuilder(this, MyDatabase.class,
				MyDatabase.NAME).fallbackToDestructiveMigration().build();

		scam = AddNewCameraActivity.getCam();
		final SampleModel sampleModel = new SampleModel();
		sampleModel.setName("CodePath");

		final SampleModelDao sampleModelDao = getMyDatabase().sampleModelDao();

		initializeChangesetHandler();
		initializeCreateNodeHandler();


		AsyncTask<SampleModel, Void, Void> task = new AsyncTask<SampleModel, Void, Void>() {
			@Override
			protected Void doInBackground(SampleModel... sampleModels) {
				sampleModelDao.insertModel(sampleModels);
				return null;
			};
		};
		task.execute(sampleModel);


		if(!getRestClient(getApplicationContext()).isAuthenticated())
		{
			RestClient client = getRestClient(this);
			client.setRequestIntentFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			client.connect();
		}else{
			Toast.makeText(this, "Bereits Authenticated", Toast.LENGTH_SHORT).show();
		}

		/*if(!Intent.ACTION_VIEW.equals(getIntent().getAction()))
		{
			RestClient client = getRestClient(this);
			client.setRequestIntentFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			client.connect();
		}*/
	}

	private void initializeCreateNodeHandler() {

		this.createNodeHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					try {
						String s = new String(responseBody, "UTF-8");
						Log.d(TAG, "Node upload successfully " + s);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				try {
					Log.d(TAG, new String(responseBody, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				Toast.makeText(LoginActivity.this, "Upload der Kamera fehlgeschlagen", Toast.LENGTH_SHORT).show();
			}
		};

	}

	private void initializeChangesetHandler() {

		this.changesetHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

				try {
					String changesetID = new String(responseBody, "UTF-8");
					Log.d(TAG, "changeset successfully provided " + changesetID);
					CreateOsmNode.createOsmFile(changesetID, scam, getApplicationContext());
					getClient().uploadNode(createNodeHandler);
				} catch (UnsupportedEncodingException | HttpResponseException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
				try {
					Log.d(TAG, new String(responseBody, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				Toast.makeText(LoginActivity.this, "Upload der Kamera fehlgeschlagen", Toast.LENGTH_SHORT).show();
			}
		};
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Toast.makeText(getApplicationContext(), "Erfolgreich mit Open Street Maps authentifiziert.", Toast.LENGTH_SHORT).show();
		Log.d("Login", "OnLoginSuccess");
		OauthLabel.setText(R.string.OauthSuccess);

		if(getRestClient(getApplicationContext()).isAuthenticated())
		{
			//Dialog um Upload nochmals zu bestätigen
		}
	}

	@Override
	public void onLoginFailure(Exception e) {
		Log.d("Login", "Login Failed");
		OauthLabel.setText(R.string.OauthFailure);
		uploadNodeButton.setClickable(false);
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login


	public void uploadNodes(View view) {
		final RestClient client = getRestClient(this);
		client.getChangesetID(changesetHandler);
	}

	public static RestClient getRestClient(Context context) {
		return (RestClient) RestClient.getInstance(RestClient.class, context);
	}

	public MyDatabase getMyDatabase() {
		return myDatabase;
	}






}

