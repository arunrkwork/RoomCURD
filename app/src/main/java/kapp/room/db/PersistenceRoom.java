package kapp.room.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import kapp.room.db.persistence.User;
import kapp.room.db.persistence.UserDatabase;

/**
 * Created by Arunraj on 2/28/2018.
 */

public class PersistenceRoom {

    Context mContext;
    UserDatabase mDatabase;
    PersistenceListener mListener;
    private static final String TAG = PersistenceRoom.class.getSimpleName();

    public interface PersistenceListener {
        void setOnReadAllUsers(List<User> userList);
        void setOnAddUserResponse(Long response, String name);
        void setOnUpdateUserResponse(Integer response, int position, User user);
        void setOnDeleteUserResponse(Integer response, int position);
    }

    public PersistenceRoom(Context mContext) {
        this.mContext = mContext;
        mListener = (PersistenceListener) mContext;
        mDatabase = UserDatabase
                .getInstance(mContext);
    }

    public void getAllUsers() {
        new AsyncTask<Void, Void, List<User>>() {

            @Override
            protected List<User> doInBackground(Void... voids) {
                Log.d(TAG, "doInBackground: getAllUsers");
                return mDatabase.getUserDao().getAllUsers();
            }

            @Override
            protected void onPostExecute(List<User> userList) {
                super.onPostExecute(userList);
                Log.d(TAG, "onPostExecute: getAllUsers");
                mListener.setOnReadAllUsers(userList);
            }
        }.execute();
    }

    public void addUser(final String name) {
        final User user = new User(name);
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.getUserDao().insert(user);
            }

            @Override
            protected void onPostExecute(Long response) {
                super.onPostExecute(response);
                mListener.setOnAddUserResponse(response, name);
            }
        }.execute();
    }

    public void updateUser(final int position, final User user) {

        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... voids) {
                return mDatabase.getUserDao().update(user);
            }

            @Override
            protected void onPostExecute(Integer response) {
                super.onPostExecute(response);
                mListener.setOnUpdateUserResponse(response, position, user);
            }
        }.execute();
    }

    public void deleteUser(final int position, final User user) {

        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... voids) {
                return mDatabase.getUserDao().delete(user);
            }

            @Override
            protected void onPostExecute(Integer response) {
                super.onPostExecute(response);
                mListener.setOnDeleteUserResponse(response, position);
            }
        }.execute();
    }




}
