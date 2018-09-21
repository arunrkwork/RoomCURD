package kapp.room.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kapp.room.R;
import kapp.room.db.persistence.User;

/**
 * Created by Arunraj on 2/28/2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    List<User> list;

    public UserAdapter(List<User> list) {
        this.list = list;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, final int position) {
        holder.txtUser.setText("ID : " + list.get(position).getId() + " Name : " + list.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class UserHolder extends RecyclerView.ViewHolder {

        TextView txtUser;

        public UserHolder(View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txtUser);
        }
    }

}
