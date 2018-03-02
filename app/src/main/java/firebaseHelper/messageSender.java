package firebaseHelper;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by lelouch on 2/23/18.
 */

public class messageSender extends AsyncTask<Void, Void, Void> {
    private String message, token;

    public messageSender(String message, String token) {
        this.message = message;
        this.token = token;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        JSONObject jsonBody = new JSONObject();
        JSONObject dataJson = new JSONObject();

        try {
            dataJson.put("message", message);
            jsonBody.put("data", dataJson);
            jsonBody.put("to", token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
