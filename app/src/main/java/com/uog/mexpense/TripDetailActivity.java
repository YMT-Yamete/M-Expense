package com.uog.mexpense;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uog.mexpense.adapter.ExpenseListAdapter;
import com.uog.mexpense.database.DatabaseHelper;
import com.uog.mexpense.database.Expenses;
import com.uog.mexpense.util.Constants;
import com.uog.mexpense.util.ExpenseSearchTask;
import com.uog.mexpense.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripDetailActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtDestination;
    private TextView txtDate;
    private TextView txtTotalDays;
    private TextView txtTravelAgency;
    private TextView txtRiskAssessment;
    private TextView txtDescription;
    private RecyclerView recyclerViewExpense;
    private ExpenseListAdapter adapter;

    private String name;
    private String destination;
    private long date;
    private String totalDays;
    private String travelAgency;
    private String riskAssessment;
    private String description;
    private String value1;
    private String value2;
    private String value3;
    private Double num1;
    private Double num2;
    private Integer id =0;
    private List<Expenses> expensesList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName =findViewById(R.id.txtName);
        txtDestination =findViewById(R.id.txtDestination);
        txtDate =findViewById(R.id.txtDate);
        txtTotalDays = findViewById(R.id.txtTotalDays);
        txtTravelAgency = findViewById(R.id.txtTravelAgency);
        txtRiskAssessment =findViewById(R.id.txtRiskAssessment);
        txtDescription =findViewById(R.id.txtDescription);
        recyclerViewExpense =findViewById(R.id.recyclerViewExpense);
        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(this));

        adapter =new ExpenseListAdapter(expensesList);
        adapter.setOnItemClickListener(new ExpenseListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, long id) {
                removeExpense(position);
            }
        });
        recyclerViewExpense.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        if( bundle !=null ){
            for (String key : bundle.keySet())
            {
                Log.d("Bundle Debug", key + " = \"" + bundle.get(key) + "\"");
            }
            name = bundle.getString(EntryActivity.NAME);
            destination = bundle.getString(EntryActivity.DESTINATION);
            date = bundle.getLong(EntryActivity.DATE);
            totalDays = bundle.getString(EntryActivity.TOTAL_DAYS);
            travelAgency = bundle.getString(EntryActivity.TRAVEL_AGENCY);
            riskAssessment = (bundle.getBoolean(EntryActivity.RISK_ASSESSMENT))?"Yes":"No";
            description = bundle.getString(EntryActivity.DESCRIPTION);

            txtName.setText(name);
            txtDestination.setText(destination);
            txtTotalDays.setText((!totalDays.equals(""))?totalDays:"none");
            txtTravelAgency.setText((!travelAgency.equals(""))?travelAgency:"none");
            txtRiskAssessment.setText(riskAssessment);
            txtDescription.setText((!description.equals(""))?description:"none");
            Date selectedDate =new Date( date );
            String dateStr = new SimpleDateFormat(Constants.DATE_FORMAT).format(selectedDate);
            txtDate.setText(dateStr);

            try {
                id =bundle.getInt(EntryActivity.ID, 0);
            }catch (Exception e){}

        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        expenseSearch( );
    }
    private void expenseSearch(){
        ExpenseSearchTask searchTask =new ExpenseSearchTask(this);
        searchTask.setListener(new ExpenseSearchTask.ExpenseSearchListener() {
            @Override
            public void result(List<Expenses> expensesList) {
                showExpenseListResult( expensesList );
            }
        });
        searchTask.execute( String.valueOf(id) );
    }

    private void showExpenseListResult(List<Expenses> expenses){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expensesList = expenses;
                adapter.setTripList(expensesList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void removeExpense( int position ){
        DatabaseHelper database =new DatabaseHelper(getBaseContext());
        Expenses expenses =expensesList.get(position);
        long result =database.deleteExpense(expenses.getId());
        if( result >0 ){
            expensesList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(), expenses.getExpenseType() +" has been deleted!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(), "Error!, failed to delete the trip "+expenses.getExpenseType(), Toast.LENGTH_SHORT).show();
        }
    }
}