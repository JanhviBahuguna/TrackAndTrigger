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


public class Grocery extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private String key="";
    private String Item;
    private String Quantity;
//  String text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_grocery);

        Toolbar toolbar = findViewById(R.id.Grocery_toolbar);
        recyclerView = findViewById(R.id.recyclerViewGrocery);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String onlineUserId = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Grocery").child(onlineUserId);

        FloatingActionButton fab = findViewById(R.id.fab_Grocery);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });


    }


    private void addItem() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(Grocery.this);
        LayoutInflater inflater = LayoutInflater.from(Grocery.this);

        View myView = inflater.inflate(R.layout.input, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        myDialog.setCancelable(false);

        EditText Item = myView.findViewById(R.id.item);
        EditText Quantity = myView.findViewById(R.id.quanity);

        Button save = myView.findViewById(R.id.save);
        Button cancel = myView.findViewById(R.id.cancel);


        cancel.setOnClickListener((v) -> {
            dialog.dismiss();
        });

        save.setOnClickListener((v) -> {
            String mItem = Item.getText().toString();
            String mQuantity = Quantity.getText().toString();
            String id = reference.getKey();

            if (TextUtils.isEmpty(mItem)) {
                Item.setError("Item Required");
                return;
            }

            if (TextUtils.isEmpty(mQuantity)) {
                Quantity.setError("Quantity required");
                return;
            } else {
                Data data = new Data(mItem, mQuantity,id);
                reference.push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> Item) {
                        if (Item.isSuccessful()) {
                            Toast.makeText(Grocery.this, "Item has been inserted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = Item.getException().toString();
                            Toast.makeText(Grocery.this, "Item insertion failed" + error, Toast.LENGTH_SHORT).show();
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

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(reference, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data data) {
                holder.setItem(data.getItem());
                holder.setQuantity(data.getQuantity());

                holder.mView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        Item = data.getItem();
                        Quantity = data.getQuantity();

                        updateTask();
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_retrieved, parent, false);
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

        public void setItem(String Item) {
            TextView ItemView = mView.findViewById(R.id.Item_retrieve);
            ItemView.setText(Item);
        }


        public void setQuantity(String Quantity) {
            TextView QuantityView = mView.findViewById(R.id.Quantity_retrieve);
            QuantityView.setText(Quantity);
        }

    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mItem = view.findViewById(R.id.update_item);
        EditText mQuantity = view.findViewById(R.id.update_quantity);

        mItem.setText(Item);
        mItem.setSelection(Item.length());

        mQuantity.setText(Quantity);
        mQuantity.setSelection(Quantity.length());

        Button delButton = view.findViewById(R.id.delete);
        Button updateButton = view.findViewById(R.id.update);
        Button shareButton = view.findViewById(R.id.share);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item = mItem.getText().toString();
                Quantity = mQuantity.getText().toString();

                Data data = new Data(Item,Quantity,key);
                reference.child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> Item) {

                        if(Item.isSuccessful())
                        {
                            Toast.makeText(Grocery.this, "Item has been updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = Item.getException().toString();
                            Toast.makeText(Grocery.this, "update failed" + error, Toast.LENGTH_SHORT).show();
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
                    public void onComplete(@NonNull Task<Void> item) {
                        if(item.isSuccessful())
                        {
                            Toast.makeText(Grocery.this, "Item has been deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = item.getException().toString();
                            Toast.makeText(Grocery.this, "failed to delete Item" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });
        shareButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Item: " +  mItem.getText().toString()+"\n" + "Quantity: " + mQuantity.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        dialog.show();
    }

}
