package com.uog.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uog.myapplication2.database.DatabaseHelper;
import com.uog.myapplication2.database.Trip;
import com.uog.myapplication2.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtDestination;
    private TextView txtDate;
    private TextView txtTotalDays;
    private TextView txtTravelAgency;
    private TextView txtRiskAssessment;
    private TextView txtDescription;

    String name;
    String destination;
    long date;
    String totalDays;
    String travelAgency;
    String riskAssessment;
    String description;

    private String value1;
    private String value2;
    private String value3;
    private Double num1;
    private Double num2;
    private Integer id =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtName =findViewById(R.id.txtName);
        txtDestination =findViewById(R.id.txtDestination);
        txtDate =findViewById(R.id.txtDate);
        txtTotalDays = findViewById(R.id.txtTotalDays);
        txtTravelAgency = findViewById(R.id.txtTravelAgent);
        txtRiskAssessment =findViewById(R.id.txtRiskAssessment);
        txtDescription =findViewById(R.id.txtDescription);

        Bundle bundle = getIntent().getExtras();
        if( bundle !=null ){
            name = bundle.getString(EntryActivity.NAME);
            destination = bundle.getString(EntryActivity.DESTINATION);
            date = bundle.getLong(EntryActivity.DATE);
            totalDays = bundle.getString(EntryActivity.TOTAL_DAYS);
            travelAgency = bundle.getString(EntryActivity.TRAVEL_AGENCY);
            riskAssessment = bundle.getString(EntryActivity.RISK_ASSESSMENT);
            description = bundle.getString(EntryActivity.DESCRIPTION);

            txtName.setText(name);
            txtDestination.setText(destination);
            txtTotalDays.setText(totalDays);
            txtTravelAgency.setText(travelAgency);
            txtRiskAssessment.setText(riskAssessment);
            txtDescription.setText(description);
            Date selectedDate =new Date( date );
            String dateStr = new SimpleDateFormat(Constants.DATE_FORMAT).format(selectedDate);
            txtDate.setText( "" + dateStr);

            try {
                id =bundle.getInt(EntryActivity.ID, 0);
            }catch (Exception e){}

        }

        Button btnSave =findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper database =new DatabaseHelper(getBaseContext());
                long result =0;
                if( id ==0) {
                    result = database.saveTrip(name, destination, date, totalDays, travelAgency,riskAssessment.equalsIgnoreCase("Yes") ? 1 : 0, description);
                }else{
                    Trip trip =new Trip(
                            id,
                            name,
                            destination,
                            date,
                            totalDays,
                            travelAgency,
                            riskAssessment.equalsIgnoreCase("Yes"),
                            description
                    );
                    result =database.updateTrip(trip);
                }
                database.close();
                if( result >0 ){
                    Toast.makeText(getBaseContext(), "Trip information has been saved!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailActivity.this, TripListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}