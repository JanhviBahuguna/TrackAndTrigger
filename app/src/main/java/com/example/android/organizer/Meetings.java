package com.example.android.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Meetings extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private String key="";
    private String task;
    private String date;
    private String time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meetings);

        Toolbar toolbar = findViewById(R.id.meetings_toolbar);


        recyclerView = findViewById(R.id.recyclerViewmeetings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String onlineUserId = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("meetings").child(onlineUserId);

        FloatingActionButton fab = findViewById(R.id.fab_meetings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }

    private void addTask() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(Meetings.this);
        LayoutInflater inflater = LayoutInflater.from(Meetings.this);

        View myView = inflater.inflate(R.layout.input_todo, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        myDialog.setCancelable(false);

        EditText task = myView.findViewById(R.id.todo_task);
        EditText date = myView.findViewById(R.id.todo_date);
        EditText time = myView.findViewById(R.id.todo_time);

        Button save = myView.findViewById(R.id.save_todo);
        Button cancel = myView.findViewById(R.id.cancel_todo);


        cancel.setOnClickListener((v) -> {
            dialog.dismiss();
        });

        save.setOnClickListener((v) -> {
            String mTask = task.getText().toString();
            String mDate = date.getText().toString();
            String mTime = time.getText().toString();
            String id = reference.getKey();

            if (TextUtils.isEmpty(mTask)) {
                task.setError("Task Required");
                return;
            }

            if (TextUtils.isEmpty(mDate)) {
                date.setError("Date Required");
                return;
            }

            if (TextUtils.isEmpty(mTime)) {
                time.setError("Time required");
                return;
            } else {
                Todo_data todo_data = new Todo_data(mTask, mTime, id, mDate);
                reference.push().setValue(todo_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Meetings.this, "Task has been inserted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(Meetings.this, "task insertion failed" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            dialog.dismiss();

        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Todo_data> options = new FirebaseRecyclerOptions.Builder<Todo_data>()
                .setQuery(reference, Todo_data.class)
                .build();

        FirebaseRecyclerAdapter<Todo_data, Meetings.MyViewHolder> adapter = new FirebaseRecyclerAdapter<Todo_data, Meetings.MyViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull Meetings.MyViewHolder holder, int position, @NonNull Todo_data todo_data) {
                holder.setTask(todo_data.getTask());
                holder.setDate(todo_data.getDate());
                holder.setTime(todo_data.getTime());

                holder.mView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        task = todo_data.getTask();
                        date = todo_data.getDate();
                        time = todo_data.getTime();

                        updateTask();
                    }
                });

            }

            @NonNull
            @Override
            public Meetings.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meetings_retrieved, parent, false);
                return new Meetings.MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTask(String task) {
            TextView taskView = mView.findViewById(R.id.task_retrieve);
            taskView.setText(task);
        }

        public void setDate(String date) {
            TextView dateView = mView.findViewById(R.id.date_retrieve);
            dateView.setText(date);
        }

        public void setTime(String time) {
            TextView timeView = mView.findViewById(R.id.time_retrieve);
            timeView.setText(time);
        }

    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.todo_update,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mTask = view.findViewById(R.id.todo_update_task);
        EditText mDate = view.findViewById(R.id.todo_update_date);
        EditText mTime = view.findViewById(R.id.todo_update_time);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDate.setText(date);
        mDate.setSelection(date.length());

        mTime.setText(time);
        mTime.setSelection(time.length());

        Button delButton = view.findViewById(R.id.delete_todo);
        Button updateButton = view.findViewById(R.id.update_todo);
        Button shareButton = view.findViewById(R.id.share);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = mTask.getText().toString();
                date = mDate.getText().toString();
                time = mTime.getText().toString();

                Todo_data todo_data = new Todo_data(task,time,key,date);
                reference.child(key).setValue(todo_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(Meetings.this, "Task has been updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(Meetings.this, "update failed" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Meetings.this, "Task has been deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(Meetings.this, "failed to delete task" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        shareButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Meeting: " +  mTask.getText().toString()+"\n"+"Date: " +  mDate.getText().toString()+"\n" + "Time: " + mTime.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
        dialog.show();
    }
}

