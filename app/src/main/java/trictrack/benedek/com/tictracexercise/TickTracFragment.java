package trictrack.benedek.com.tictracexercise;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import trictrack.benedek.com.tictracexercise.Dao.Dao;
import trictrack.benedek.com.tictracexercise.Dao.User;

/**
 * Created by Benedek on 2016.08.12..
 */
public class TickTracFragment extends Fragment {

    private TicTracAdapter ticTracAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tictrac_fragment, container, false);

        EditText editText = (EditText) rootView.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ticTracAdapter.search(s.toString());
            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.ticTracListView);

        ticTracAdapter = new TicTracAdapter( (TicTracActivity) TickTracFragment.this.getActivity());
        listView.setAdapter(ticTracAdapter);

        return rootView;
    }

    private class TicTracAdapter extends BaseAdapter {

        private Dao dao;

        private TicTracActivity activity;

        private List<User> users;

        public TicTracAdapter(TicTracActivity activity){
            this.activity = activity;
            dao = TicTracApplication.getInstance().getDao();

            dao.open();
            users = dao.getUsers();
            dao.close();
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.row, null);
            }

            TextView email = (TextView) convertView.findViewById(R.id.email);
            email.setText(users.get(position).getEmail());

            final TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(users.get(position).getName());

            final TextView infos = (TextView) convertView.findViewById(R.id.infos);
            infos.setText(users.get(position).getInfos());


            Button setButton = (Button) convertView.findViewById(R.id.setButton);
            setButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String selectedPosition = "-1";
                    if (users.get(position).getId().equals(TicTracApplication.getInstance().getRowId())) {
                        selectedPosition = "-1";
                        name.setTextColor(Color.BLACK);
                        name.setTypeface(null, Typeface.NORMAL);
                        activity.setTitle(activity.getResources().getString(R.string.app_name));
                    } else {
                        selectedPosition = users.get(position).getId();
                    }

                    TicTracApplication.getInstance().saveRowId(selectedPosition);
                    notifyDataSetChanged();
                }
            });

            if (users.get(position).getId().equals(TicTracApplication.getInstance().getRowId())) {
                infos.setVisibility(View.VISIBLE);
                name.setTextColor(Color.BLUE);
                name.setTypeface(null, Typeface.BOLD);
                activity.setTitle(users.get(position).getName());
            } else {
                name.setTextColor(Color.BLACK);
                name.setTypeface(null, Typeface.NORMAL);
                infos.setVisibility(View.GONE);
            }

            return convertView;
        }

        public void search(String search) {

            dao.open();
            users = dao.getUsers();
            dao.close();

            for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
                User user = iterator.next();

                String email = user.getEmail();
                String name = user.getName();

                if (!email.toLowerCase().contains(search.toLowerCase()) || !name.toLowerCase().contains(search.toLowerCase())) {
                    iterator.remove();
                }
                notifyDataSetChanged();
            }
        }
    }
}
