package kapp.room.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kapp.room.R;
import kapp.room.db.PersistenceRoom;
import kapp.room.db.persistence.User;
import kapp.room.adapter.UserAdapter;
import kapp.room.listener.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PersistenceRoom.PersistenceListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    List<User> list;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    UserAdapter adapter;
    FloatingActionButton fabAdd;
    PersistenceRoom mRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        mRoom = new PersistenceRoom(this);
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new UserAdapter(list);
        mRecyclerView.setAdapter(adapter);

        mRoom.getAllUsers();
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = list.get(position).getId();
                String name = list.get(position).getName();
                updateDialog(position, id, name);
                Log.d(TAG, "setOnUserItemClick: id : " + id + " name : " + name + " position : " + position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                showDeleteAlert(position);
            }
        }));


    }


    private void showDeleteAlert(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete(position);
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void delete(int position) {
      mRoom.deleteUser(position, list.get(position));
    }

    @Override
    public void onClick(View v) {
        if (v == fabAdd) {
            addDialog();
        }
    }

    private void addDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);

        final EditText edUser = dialog.findViewById(R.id.edUser);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edUser.getText().toString();
                if (name != null && name.length() != 0) {
                    mRoom.addUser(name);
                } else
                    Toast.makeText(MainActivity.this, "Please Enter the Name", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateDialog(final int position, final int id, final String name) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);

        final EditText edUser = dialog.findViewById(R.id.edUser);
        edUser.setText(name);
        edUser.setSelection(name.length());
        Button btnUpdate = dialog.findViewById(R.id.btnAdd);
        btnUpdate.setText("Update");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edUser.getText().toString();
                if (name != null && name.length() != 0) {

                    User user = new User();
                    user.setId(id);
                    user.setName(name);
                    mRoom.updateUser(position, user);

                    dialog.dismiss();

                } else
                    Toast.makeText(MainActivity.this, "Please Enter the Name", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void setOnReadAllUsers(List<User> userList) {
        list.addAll(userList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setOnAddUserResponse(Long response, String name) {
        long res = response.longValue();

        if (res != -1) {
            User user = new User();
            user.setId((int) res);
            user.setName(name);
            list.add(user);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "One User Inserted", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();

        //Toast.makeText(this, "insert: "+response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOnUpdateUserResponse(Integer response, int position, User user) {
        if (response > 0) {
            list.remove(position);
            adapter.notifyItemChanged(position);
            list.add(position, user);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "One User Updated", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, "update : " + response +" position " + position + " id : " + user.getId() + " name : " + user.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOnDeleteUserResponse(Integer response, int position) {
        if (response > 0) {
            list.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, list.size());
            Toast.makeText(this, "One User Deleted", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, " delete: " + response, Toast.LENGTH_SHORT).show();
    }
}
