package trictrack.benedek.com.tictracexercise.services;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import trictrack.benedek.com.tictracexercise.Dao.Dao;
import trictrack.benedek.com.tictracexercise.Dao.User;

/**
 * Created by Benedek on 2016.08.12..
 */
public class DownloadUserDataAsyncTask extends AsyncTask<Void, Void, List<User>> {

    @Override
    protected List<User> doInBackground(Void... params) {
        List<User> users = null;
        try {

            users = downloadData();

        } catch (IOException | JSONException e ) {
            throw new RuntimeException(e);
        }

        return users;
    }

    private List<User> downloadData() throws IOException, JSONException {
        URL url = new URL("http://media.tictrac.com/tmp/users.json");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        int i;

        StringBuffer stringBuffer = new StringBuffer();

        while((i = in.read() )!= -1 ) {
            char c = (char) i;
            stringBuffer.append(c);
        }
        JSONObject jsonObject = new JSONObject(stringBuffer.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("users");

        List<User> users = new ArrayList<>();
        for (int j = 0; j < jsonArray.length(); j++) {

            JSONObject user = jsonArray.getJSONObject(j);
            String email = user.getString("email");
            String name = user.getString("name");
            String infos = user.getString("infos");

            User cUser = new User(email, name, infos);
            users.add(cUser);
        }
        return users;
    }
}
