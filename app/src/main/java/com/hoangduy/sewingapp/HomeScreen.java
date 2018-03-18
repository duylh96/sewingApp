package com.hoangduy.sewingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoangduy.sewingapp.adapters.AllCustomerAdapter;
import com.hoangduy.sewingapp.adapters.LatestCustomerAdapter;
import com.hoangduy.sewingapp.dto.Customer;
import com.hoangduy.sewingapp.dto.CustomerSearchItem;
import com.hoangduy.sewingapp.dto.History;
import com.hoangduy.sewingapp.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    static boolean calledAlready = false;
    private DatabaseReference customerDB;
    private DatabaseReference scheduleDB;
    private List<Customer> listAllCustomer;
    private List<History> listSchedule;
    private RecyclerView recyclerViewAllCustomer, recyclerViewLatestSchedule;
    private AllCustomerAdapter allCustomerAdapter;
    private LatestCustomerAdapter latestScheduleAdapter;
    private FloatingActionButton fabAdd;

    private FloatingSearchView mSearchView;
    private String mLastQuery = "";

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
        mSearchView = findViewById(R.id.floating_search_view);
        recyclerViewAllCustomer = findViewById(R.id.list_all_customer);
        recyclerViewLatestSchedule = findViewById(R.id.list_latest_customer);

        //init list all customer
        this.connectDB();
        this.initAllCustomer();
        this.initSchedule();

        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToDetail();
            }
        });

        setupSearchBar();
    }

    private void setupSearchBar() {

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();

                    List<CustomerSearchItem> listSuggestionCustomer = new ArrayList<>();
                    for (Customer customer : listAllCustomer) {
                        if (customer.getName().toLowerCase().contains(newQuery.toLowerCase())) {
                            CustomerSearchItem searchItem = new CustomerSearchItem();
                            searchItem.setName(customer.getName());
                            listSuggestionCustomer.add(searchItem);
                        }
                    }

                    //show list
                    mSearchView.swapSuggestions(listSuggestionCustomer);
                    mSearchView.hideProgress();
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                for (Customer customer : listAllCustomer) {
                    if (customer.getName().equals(searchSuggestion.getBody())) {
                        Intent i = new Intent(HomeScreen.this, CustomerDetailScreen.class);
                        i.putExtra("CUSTOMER", customer);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });
    }

    private void initSchedule() {
        latestScheduleAdapter = new LatestCustomerAdapter(this.listSchedule, new LatestCustomerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(History history) {
                for (Customer customer : listAllCustomer) {
                    if (customer.getName().equals(history.getName())) {
                        Intent i = new Intent(HomeScreen.this, CustomerDetailScreen.class);
                        i.putExtra("CUSTOMER", customer);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void OnItemLongClick() {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewLatestSchedule.setLayoutManager(layoutManager);
        recyclerViewLatestSchedule.setAdapter(latestScheduleAdapter);
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
            public void OnItemLongClick(final Customer customer) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(HomeScreen.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(HomeScreen.this);
                }
                builder.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                customerDB.child(customer.getName()).removeValue();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAllCustomer.setLayoutManager(layoutManager);
        recyclerViewAllCustomer.setAdapter(allCustomerAdapter);
    }

    private void connectDB() {
        //all customer
        customerDB = FirebaseDatabase.getInstance().getReference("customer");
        listAllCustomer = new ArrayList<>();
        customerDB.addValueEventListener(new ValueEventListener() {
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

        //schedule
        scheduleDB = FirebaseDatabase.getInstance().getReference("schedule");
        listSchedule = new ArrayList<>();
        scheduleDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listSchedule.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    listSchedule.add(postSnapShot.getValue(History.class));
                }
                updateTimeSchedule();
                latestScheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTimeSchedule() {
        for (History history : listSchedule) {
            String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
            if (history.getDate().compareTo(currentDate) < 0) {
                scheduleDB.child(history.getDate() + history.getName()).removeValue();
            }
        }
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
