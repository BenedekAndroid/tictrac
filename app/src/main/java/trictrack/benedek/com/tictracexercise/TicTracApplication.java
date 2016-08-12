package trictrack.benedek.com.tictracexercise;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import trictrack.benedek.com.tictracexercise.Dao.Dao;
import trictrack.benedek.com.tictracexercise.services.DownloadUserDataAsyncTask;

/**
 * Created by Benedek on 2016.08.12..
 */
public class TicTracApplication extends Application {

    public static final String SELECTED_USER = "Selected user";

    private SharedPreferences sharedpreferences;

    private static TicTracApplication instance;

    public TicTracApplication() {
        super();
        instance = this;
    }

    private Dao dao;
    private DownloadUserDataAsyncTask downloadUserDataAsyncTask;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedpreferences = getSharedPreferences(SELECTED_USER, Context.MODE_PRIVATE);
        dao = new Dao(this);
        downloadUserDataAsyncTask = new DownloadUserDataAsyncTask();
    }

    public static TicTracApplication getInstance() {
        if (instance == null) {
            Log.e("", "Application object is null.");
        }
        return instance;
    }
    public Dao getDao(){
        return dao;
    }

    public DownloadUserDataAsyncTask getDownloadUserDataAsyncTask(){
        return downloadUserDataAsyncTask;
    }

    public void saveRowId(String rowId) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("position", rowId);
        editor.commit();
    }

    public String getRowId() {
        return sharedpreferences.getString("position", "-1");
    }
}
