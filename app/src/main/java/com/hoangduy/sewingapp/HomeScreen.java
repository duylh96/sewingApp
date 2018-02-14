package com.hoangduy.sewingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoangduy.sewingapp.adapters.AllCustomerAdapter;
import com.hoangduy.sewingapp.adapters.LatestCustomerAdapter;
import com.hoangduy.sewingapp.dto.Customer;
import com.hoangduy.sewingapp.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    static boolean calledAlready = false;
    private DatabaseReference db;
    private List<Customer> listAllCustomer, listLatestCustomer;
    private RecyclerView recyclerViewAllCustomer, recyclerViewLatestCustomer;
    private AllCustomerAdapter allCustomerAdapter;
    private LatestCustomerAdapter latestCustomerAdapter;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //config firebase to enable local storage
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //bind view
        recyclerViewAllCustomer = findViewById(R.id.list_all_customer);
        recyclerViewLatestCustomer = findViewById(R.id.list_latest_customer);

        //init list all customer
        this.connectDB();
        this.initAllCustomer();

        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToDetail();
            }
        });
    }

    private void initAllCustomer() {
        allCustomerAdapter = new AllCustomerAdapter(this.listAllCustomer, new AllCustomerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Customer customer) {
                Intent i = new Intent(HomeScreen.this, CustomerDetailScreen.class);
                i.putExtra("CUSTOMER", customer);
                startActivity(i);
            }

            @Override
            public void OnItemLongClick() {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAllCustomer.setLayoutManager(layoutManager);
        recyclerViewAllCustomer.setAdapter(allCustomerAdapter);
    }

    private void connectDB() {
        db = FirebaseDatabase.getInstance().getReference("customer");
        listAllCustomer = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAllCustomer.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    listAllCustomer.add(postSnapShot.getValue(Customer.class));
                }
                sortOrderListAllCustomer();
                allCustomerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToDetail() {
        Intent intent = new Intent(HomeScreen.this, CustomerAddScreen.class);
        intent.putExtra("MODE", Constants.MODE.CREATE);
        startActivity(intent);
    }

    private void sortOrderListAllCustomer() {
        Collections.sort(listAllCustomer, new Comparator<Customer>() {
            @Override
            public int compare(Customer c1, Customer c2) {
                String s1 = Constants.getLastWordOfName(c1);
                String s2 = Constants.getLastWordOfName(c2);
                return s1.compareToIgnoreCase(s2);
            }
        });
    }
}
