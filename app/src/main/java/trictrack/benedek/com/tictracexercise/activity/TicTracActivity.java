package trictrack.benedek.com.tictracexercise.activity;


import android.app.ActionBar;
import android.app.Activity;

import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.ExecutionException;

import trictrack.benedek.com.tictracexercise.Dao.Dao;
import trictrack.benedek.com.tictracexercise.Dao.User;
import trictrack.benedek.com.tictracexercise.R;
import trictrack.benedek.com.tictracexercise.application.TicTracApplication;
import trictrack.benedek.com.tictracexercise.fragment.TickTracFragment;
import trictrack.benedek.com.tictracexercise.services.DownloadUserDataAsyncTask;

public class TicTracActivity extends Activity {

    private Dao dao;

    private DownloadUserDataAsyncTask downloadUserDataAsyncTask;

    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictrack);

        actionBar = getActionBar();

        dao = TicTracApplication.getInstance().getDao();

        downloadUserDataAsyncTask = TicTracApplication.getInstance().getDownloadUserDataAsyncTask();

        saveUsersInMemory();

        initFragment();

    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new TickTracFragment(), null).commit();
    }

    private void saveUsersInMemory() {
        List<User> users = null;
        try {
            users = downloadUserDataAsyncTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        save(users);

    }

    private void save(List<User> users) {
        dao.open();
        for (User user : users) {
            if (!dao.isItSaved(user)) {
                dao.save(user);
            }
        }
        dao.close();
    }

    public void setTitle(String title) {
        actionBar.setTitle(title);
    }
}
