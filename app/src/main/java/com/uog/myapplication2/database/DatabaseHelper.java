package com.uog.myapplication2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="myExpense.db";
    private static final String TABLE_TRIP ="tblTrip";
    private static final String TABLE_EXPENSE ="tblExpense";

    public static final String TRIP_ID = "id";
    public static final String TRIP_NAME = "name";
    public static final String TRIP_DESTINATION = "destination";
    public static final String TRIP_DATE = "trip_date";
    public static final String TOTAL_DAYS = "total_days";
    public static final String TRAVEL_AGENCY = "travel_agency";
    public static final String TRIP_RISK_ASSESSMENT = "risk_assessment";
    public static final String TRIP_DESCRIPTION = "description";

    private SQLiteDatabase database;

    private static final String CREATE_TRIP_TABLE =String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s BIGINT," +
                    " %s INTEGER," +
                    " %s TEXT," +
                    " %s INTEGER," +
                    " %s TEXT)"
    , TABLE_TRIP, TRIP_ID, TRIP_NAME, TRIP_DESTINATION, TRIP_DATE, TOTAL_DAYS, TRAVEL_AGENCY, TRIP_RISK_ASSESSMENT,TRIP_DESCRIPTION);


    public static final String EXPENSE_ID = "id";
    public static final String TRIP_FOREIGN_ID = "trip_id";
    public static final String EXPENSE_TYPE = "expense_type";
    public static final String EXPENSE_AMOUNT = "amount";
    public static final String EXPENSE_TIME = "expense_time";
    public static final String EXPENSE_COMMENT = "comment";
    public static final String EXPENSE_VALUE1 = "value1";
    public static final String EXPENSE_VALUE2 = "value2";
    public static final String EXPENSE_VALUE3 = "value3";

    private static final String CREATE_EXPENSE_TABLE =String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s INTEGER," +
                    " %s TEXT," +
                    " %s REAL," +
                    " %s BIGINT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " CONSTRAINT fk_trip FOREIGN KEY (%s) REFERENCES %s (%s) ON UPDATE CASCADE ON DELETE cascade)"
            , TABLE_EXPENSE, EXPENSE_ID, TRIP_FOREIGN_ID, EXPENSE_TYPE, EXPENSE_AMOUNT, EXPENSE_TIME, EXPENSE_COMMENT,
            EXPENSE_VALUE1, EXPENSE_VALUE2, EXPENSE_VALUE3,
            EXPENSE_ID, TABLE_TRIP, TRIP_ID );

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        database =getWritableDatabase();
        if(database !=null) {
            database.execSQL( "PRAGMA encoding ='UTF-8'" );
            database.execSQL( "PRAGMA foreign_keys = ON" );
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TRIP_TABLE);
        sqLiteDatabase.execSQL(CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long saveTrip(String name, String destination, long date, String totalDays, String travelAgency, int riskAssessment, String description){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(TRIP_NAME, name);
        rowValues.put(TRIP_DESTINATION, destination);
        rowValues.put(TRIP_DATE, date);
        rowValues.put(TOTAL_DAYS, totalDays);
        rowValues.put(TRAVEL_AGENCY, travelAgency);
        rowValues.put(TRIP_RISK_ASSESSMENT, riskAssessment);
        rowValues.put(TRIP_DESCRIPTION, description);
        result =database.insertOrThrow(TABLE_TRIP, null, rowValues);
        return result;
    }

    public long updateTrip(Trip trip){
        long result =0;

        ContentValues rowValues =new ContentValues();
        rowValues.put(TRIP_NAME, trip.getName());
        rowValues.put(TRIP_DESTINATION, trip.getDestination());
        rowValues.put(TRIP_DATE, trip.getDate());
        rowValues.put(TOTAL_DAYS,trip.getTotalDays());
        rowValues.put(TRAVEL_AGENCY, trip.getTravelAgency());
        rowValues.put(TRIP_RISK_ASSESSMENT, trip.isRiskAssessment()? 1 : 0);
        rowValues.put(TRIP_DESCRIPTION, trip.getDescription());
        String where = "id=?";
        String values[] = {trip.getId() +""};
        result =database.update(TABLE_TRIP, rowValues, where, values);
        return result;
    }

    public long deleteTrip(int id){
        long result =0;
        String where = "id=?";
        String values[] = { String.valueOf(id) };
        result =database.delete(TABLE_TRIP, where, values);
        return result;
    }

    public long saveExpense(int tripId, String type, double amount, long time, String comment,
                            String val1, String val2, String val3){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(TRIP_FOREIGN_ID, tripId);
        rowValues.put(EXPENSE_TYPE, type);
        rowValues.put(EXPENSE_AMOUNT, amount);
        rowValues.put(EXPENSE_TIME, time);
        rowValues.put(EXPENSE_COMMENT, comment);
        rowValues.put(EXPENSE_VALUE1, val1);
        rowValues.put(EXPENSE_VALUE2, val2);
        rowValues.put(EXPENSE_VALUE3, val3);
        result =database.insertOrThrow(TABLE_EXPENSE, null, rowValues);
        return result;
    }

    public List<Trip> searchTrip( String keyword ) throws Exception{
        Cursor cursor = null;
        String query ="SELECT * FROM " + TABLE_TRIP
                +" WHERE " + TRIP_NAME +" LIKE '%" + keyword +"%'";// "SELECT * FROM tblTrip WHERE name LIKE %%"

        return searchTrip( query, cursor );
    }

    public List<Trip> searchTrip( String name, String destination, Date date ) throws Exception{
        Cursor cursor = null;
        Date startDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        Date endDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 23, 59,59);
        String query ="SELECT * FROM " + TABLE_TRIP
                +" WHERE "
                + TRIP_NAME +"='" + name + "'"
                + " AND " + TRIP_DESTINATION +"='" + destination + "'"
                + " AND " + TRIP_DATE +" BETWEEN " + startDate.getTime() + " AND " + endDate.getTime();
        return searchTrip( query, cursor );
    }

    public List<Trip> searchTrip( String query, Cursor cursor ) throws Exception{

        List<Trip> results =new ArrayList<>();
        cursor = database.rawQuery( query, null );
        cursor.moveToFirst( );
        while( !cursor.isAfterLast() ){
            Trip trip =new Trip(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6)==1,
                    cursor.getString(7)
            );
            results.add(trip);
            cursor.moveToNext( );
        }
        cursor.close();
        return results;
    }
}
