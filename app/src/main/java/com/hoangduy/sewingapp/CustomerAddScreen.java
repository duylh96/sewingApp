package com.hoangduy.sewingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hoangduy.sewingapp.dto.Customer;
import com.hoangduy.sewingapp.utils.Constants;

import org.apache.commons.lang3.StringUtils;

public class CustomerAddScreen extends AppCompatActivity {

    private EditText edt_customerName, edt_customerPhone, edt_customerDetails;
    private Button btn_createNewCustomer;
    private DatabaseReference db;
    private Constants.MODE mode;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseDatabase.getInstance().getReference("customer");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init components
        edt_customerName = findViewById(R.id.edt_customerName);
        edt_customerPhone = findViewById(R.id.edt_phoneNumber);
        edt_customerDetails = findViewById(R.id.edt_details);
        btn_createNewCustomer = findViewById(R.id.btn_new);

        mode = (Constants.MODE) getIntent().getSerializableExtra("MODE");
        switch (mode) {
            case CREATE:
                //change default title to match screen description
                getSupportActionBar().setTitle(R.string.add_new_customer_screen_title);
                //init template data
                edt_customerDetails.setText(Constants.customerDetailsTemplate);
                btn_createNewCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createNewCustomer();
                    }
                });

                break;
            case EDIT:
                customer = (Customer) getIntent().getSerializableExtra("CUSTOMER");
                edt_customerName.setText(customer.getName());
                edt_customerPhone.setText(customer.getPhone());
                edt_customerDetails.setText(customer.getDescription());
                btn_createNewCustomer.setText(R.string.updateCustomer);
                btn_createNewCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateCustomer();
                    }
                });

                break;
        }
    }

    private void updateCustomer() {
        if (checkValid()) {

            Customer editedCustomer = new Customer();
            editedCustomer.setName(edt_customerName.getText().toString().trim());
            editedCustomer.setPhone(edt_customerPhone.getText().toString().trim());
            editedCustomer.setDescription(edt_customerDetails.getText().toString());

            if (customer.getName() == editedCustomer.getName())
                db.child(customer.getName()).setValue(editedCustomer);
            else {
                db.child(customer.getName()).removeValue();
                db.child(editedCustomer.getName()).setValue(editedCustomer);
            }

            Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();

            customer = editedCustomer;
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewCustomer() {
        if (checkValid()) {
            Customer customer = new Customer();
            customer.setName(edt_customerName.getText().toString().trim());
            customer.setPhone(edt_customerPhone.getText().toString().trim());
            customer.setDescription(edt_customerDetails.getText().toString());

            db.child(customer.getName()).setValue(customer);

            Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, CustomerDetailScreen.class);
            i.putExtra("CUSTOMER", customer);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValid() {
        return StringUtils.isNotEmpty(this.edt_customerName.getText().toString())
                && StringUtils.isNumeric(this.edt_customerPhone.getText().toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("flag", 1);
        returnIntent.putExtra("CUSTOMER", customer);
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }
}
