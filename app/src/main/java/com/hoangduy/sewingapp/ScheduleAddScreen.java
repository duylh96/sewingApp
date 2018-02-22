package com.hoangduy.sewingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hoangduy.sewingapp.dto.Customer;
import com.hoangduy.sewingapp.dto.History;
import com.hoangduy.sewingapp.utils.Constants;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleAddScreen extends AppCompatActivity {

    private DatePicker schedulePicker;
    private EditText edt_shortDescription;
    private Button btn_makeSchedule;
    private Constants.MODE mode;
    private Customer customer;
    private DatabaseReference historyRf;
    private DatabaseReference scheduleRf;
    private History history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        schedulePicker = findViewById(R.id.schedule_picker);
        edt_shortDescription = findViewById(R.id.edt_shortDescription);
        btn_makeSchedule = findViewById(R.id.btn_makeSchedule);

        mode = (Constants.MODE) getIntent().getSerializableExtra("MODE");
        customer = (Customer) getIntent().getSerializableExtra("CUSTOMER");
        switch (mode) {
            case CREATE:
                getSupportActionBar().setTitle(R.string.add_new_schedule_title);
                btn_makeSchedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeNewSchedule();
                    }
                });
                break;
            case EDIT:
                getSupportActionBar().setTitle(R.string.add_new_schedule_title);
                btn_makeSchedule.setText(R.string.update_schedule_title);
                history = (History) getIntent().getSerializableExtra("HISTORY");
                edt_shortDescription.setText(history.getDescription());
                schedulePicker.updateDate(Integer.parseInt(history.getDate().substring(0, 4)),
                        Integer.parseInt(history.getDate().substring(4, 6)) - 1,
                        Integer.parseInt(history.getDate().substring(6)));
                btn_makeSchedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateSchedule();
                    }
                });
                break;
        }
    }

    private void updateSchedule() {
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String date =
                Constants.normalizeNumber(schedulePicker.getYear()) +
                        Constants.normalizeNumber(schedulePicker.getMonth() + 1) +
                        Constants.normalizeNumber(schedulePicker.getDayOfMonth());
        if (StringUtils.isNotEmpty(edt_shortDescription.getText().toString().trim())) {
            scheduleRf = FirebaseDatabase.getInstance().getReference("schedule");
            historyRf = FirebaseDatabase.getInstance().getReference("history");

            //delete old value
            scheduleRf.child(history.getDate() + history.getName()).removeValue();
            historyRf.child(history.getName()).child(history.getDate()).removeValue();

            //assign new value
            history.setDate(date);
            history.setName(customer.getName());
            history.setDescription(edt_shortDescription.getText().toString().trim());


            scheduleRf.child(date + history.getName()).setValue(history);

            historyRf.child(history.getName()).child(date).setValue(history);

            Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();

            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "error!", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeNewSchedule() {
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String date =
                Constants.normalizeNumber(schedulePicker.getYear()) +
                        Constants.normalizeNumber(schedulePicker.getMonth() + 1) +
                        Constants.normalizeNumber(schedulePicker.getDayOfMonth());
        if (StringUtils.isNotEmpty(edt_shortDescription.getText().toString().trim()) &&
                date.compareTo(currentDate) > 0) {
            scheduleRf = FirebaseDatabase.getInstance().getReference("schedule");
            historyRf = FirebaseDatabase.getInstance().getReference("history");


            history = new History();
            history.setDate(date);
            history.setName(customer.getName());
            history.setDescription(edt_shortDescription.getText().toString().trim());


            scheduleRf.child(date + history.getName()).setValue(history);

            historyRf.child(history.getName()).child(date).setValue(history);

            Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();

            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
