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


public class Bills extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private String key="";
    private String Bills;
    private String date;
    private String Amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bills);

        Toolbar toolbar = findViewById(R.id.todo_toolbar);


        recyclerView = findViewById(R.id.recyclerViewBills);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String onlineUserId = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Bills").child(onlineUserId);

        FloatingActionButton fab = findViewById(R.id.fab_bills);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }

    private void addTask() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(Bills.this);
        LayoutInflater inflater = LayoutInflater.from(Bills.this);

        View myView = inflater.inflate(R.layout.bills_input, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        myDialog.setCancelable(false);

        EditText Bills = myView.findViewById(R.id.bills_bill);
        EditText date = myView.findViewById(R.id.bills_date);
        EditText Amount = myView.findViewById(R.id.bills_amount);

        Button save = myView.findViewById(R.id.save_bills);
        Button cancel = myView.findViewById(R.id.cancel_bills);


        cancel.setOnClickListener((v) -> {
            dialog.dismiss();
        });

        save.setOnClickListener((v) -> {
            String mBills = Bills.getText().toString();
            String mDate = date.getText().toString();
            String mAmount = Amount.getText().toString();
            String id = reference.getKey();

            if (TextUtils.isEmpty(mBills)) {
                Bills.setError("Bills Required");
                return;
            }

            if (TextUtils.isEmpty(mDate)) {
                date.setError("Date Required");
                return;
            }

            if (TextUtils.isEmpty(mAmount)) {
                Amount.setError("Amount required");
                return;
            } else {
                Bills_data Bills_data = new Bills_data(mBills, mAmount, id, mDate);
                reference.push().setValue(Bills_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Bills.this, "Task has been inserted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(Bills.this, "task insertion failed" + error, Toast.LENGTH_SHORT).show();
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

        FirebaseRecyclerOptions<Bills_data> options = new FirebaseRecyclerOptions.Builder<Bills_data>()
                .setQuery(reference, Bills_data.class)
                .build();

        FirebaseRecyclerAdapter<Bills_data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Bills_data, MyViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Bills_data bills_data) {
                holder.setTask(bills_data.getBills());
                holder.setDate(bills_data.getDate());
                holder.setAmount(bills_data.getAmount());

                holder.mView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        Bills = bills_data.getBills();
                        date = bills_data.getDate();
                        Amount = bills_data.getAmount();

                        updateTask();
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bills_retrieved, parent, false);
                return new MyViewHolder(view);
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
            TextView taskView = mView.findViewById(R.id.Bills_retrieve);
            taskView.setText(task);
        }

        public void setDate(String date) {
            TextView dateView = mView.findViewById(R.id.date_retrieve);
            dateView.setText(date);
        }

        public void setAmount(String Amount) {
            TextView AmountView = mView.findViewById(R.id.amount_retrieve);
            AmountView.setText(Amount);
        }

    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.bills_update,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mTask = view.findViewById(R.id.bills_update_bills);
        EditText mDate = view.findViewById(R.id.bills_update_date);
        EditText mAmount = view.findViewById(R.id.bills_update_amount);

        mTask.setText(Bills);
        mTask.setSelection(Bills.length());

        mDate.setText(date);
        mDate.setSelection(date.length());

        mAmount.setText(Amount);
        mAmount.setSelection(Amount.length());

        Button delButton = view.findViewById(R.id.delete_todo);
        Button updateButton = view.findViewById(R.id.update_todo);
        Button shareButton = view.findViewById(R.id.share);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bills = mTask.getText().toString();
                date = mDate.getText().toString();
                Amount = mAmount.getText().toString();

                Bills_data Bills_data = new Bills_data(Bills,Amount,key,date);
                reference.child(key).setValue(Bills_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(Bills.this, "Task has been updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(Bills.this, "update failed" + error, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Bills.this, "Task has been deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(Bills.this, "failed to delete task" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        shareButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Meeting: " +  mTask.getText().toString()+"\n"+"Date: " +  mDate.getText().toString()+"\n" + "Time: " + mAmount.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
        dialog.show();
    }
}
