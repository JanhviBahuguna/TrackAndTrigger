package com.example.android.organizer;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
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

import java.util.Calendar;


public class Todo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    static String key="";
    public static String task;
    public static String date;
    public static String time;
    public static String mTask,mTime,mDate;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_todo);

        Toolbar toolbar = findViewById(R.id.todo_toolbar);


        recyclerView = findViewById(R.id.recyclerViewTodo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String onlineUserId = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("todo").child(onlineUserId);

        FloatingActionButton fab = findViewById(R.id.fab_todo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }


    private void addTask() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(Todo.this);
        LayoutInflater inflater = LayoutInflater.from(Todo.this);

        View myView = inflater.inflate(R.layout.input_todo, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        myDialog.setCancelable(false);

        EditText task = myView.findViewById(R.id.todo_task);
        EditText date = myView.findViewById(R.id.todo_date);
        EditText time = myView.findViewById(R.id.todo_time);


        Button datePicker = myView.findViewById(R.id.datePicker_todo);
        Button timePicker = myView.findViewById(R.id.timePicker_todo);
        Button save = myView.findViewById(R.id.save_todo);
        Button cancel = myView.findViewById(R.id.cancel_todo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationHelper = new NotificationHelper(this);
        }

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new com.example.android.organizer.TimePicker();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new com.example.android.organizer.DatePicker();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        cancel.setOnClickListener((v) -> {
            dialog.dismiss();
        });

        save.setOnClickListener((v) -> {
            mTask = task.getText().toString();
            String mDate = date.getText().toString();
            mTime = time.getText().toString();
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
                            Toast.makeText(Todo.this, "Task has been inserted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(Todo.this, "task insertion failed" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            dialog.dismiss();

        });

        dialog.show();
    }


    private void setAlarm(Calendar c)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Todo_data> options = new FirebaseRecyclerOptions.Builder<Todo_data>()
                .setQuery(reference, Todo_data.class)
                .build();

        FirebaseRecyclerAdapter<Todo_data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Todo_data, MyViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Todo_data todo_data) {
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
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_retrieved, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        setAlarm(c);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
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
                            Toast.makeText(Todo.this, "Task has been updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(Todo.this, "update failed" + error, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Todo.this, "Task has been deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(Todo.this, "failed to delete task" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
