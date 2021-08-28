package application.android.com.watchthewatchers.OSMConnection;

import android.content.Context;
import android.widget.Toast;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;

import application.android.com.watchthewatchers.R;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.FileEntity;

//Diese Klasse beinhaltet die nötigen Schlüssel für den OAuth Prozess und stößt den Datei-upload an.

public class RestClient extends OAuthBaseClient {
	
	 static final BaseApi REST_API_INSTANCE = OsmAPI.instance(OsmAPI.OSM_PERM.WRITE); // Change this
	 static final String  REST_URL = "https://www.openstreetmap.org";
	 static final String  REST_CONSUMER_KEY = "yUUzIg4gygwtrFOTxAwAufasg4Kdq0ojvfqDnvwX";
	 static final String  REST_CONSUMER_SECRET = "zsaePC3SD3ADe1xpzzIaIAp3juiyheIgGT7T1yV6";

	public static final String  TEST_REST_URL = "https://master.apis.dev.openstreetmap.org";
	public static final String  TEST_REST_CONSUMER_KEY = "oeF36aVtTfxSjADz9o6WIFuW6Yv9BosQRK8PNunW";
	public static final String  TEST_REST_CONSUMER_SECRET = "K8DAiyWio9sScsLDF7EBKQyqA15lvjVbkIL8VsvL";

	private static final String TAG = "RestClient";
	private Context context;



	 static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public RestClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), ""));
		this.context = context;
	}

	//Endpoint Methode zum upload von Daten.
	 void getChangesetID(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("api/0.6/changeset/create");
		File file = new File(context.getFilesDir(), "changeset.xml");
		HttpEntity entity = new FileEntity(file);
		//HttpEntity httpEntity = new ByteArrayEntity(body.getBytes());
		client.put(context, apiUrl,entity,"application/xml", handler);
		Toast.makeText(context, "Camera Node hochladen fehlgeschlagen", Toast.LENGTH_SHORT).show();
	}

	void uploadNode(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("api/0.6/node/create");
		File file = new File(context.getFilesDir(), "node.xml");
		HttpEntity entity = new FileEntity(file);
		//HttpEntity httpEntity = new ByteArrayEntity(body.getBytes());
		client.put(context, apiUrl,entity,"application/xml", handler);
	}

	public void deleteNode(AsyncHttpResponseHandler handler, String nodeID){
		//diese Methode wird aus der MainActivity aus aufgerufen um Node zu löschen...
		String apiUrl = getApiUrl("api/0.6/node/#"+nodeID); //vielleicht ohne #?
		File file = new File(context.getFilesDir(), "node.xml"); //Node der gelöscht werden soll
		HttpEntity entity = new FileEntity(file);
		client.put(context, apiUrl,entity,"application/xml", handler);
	}



}
