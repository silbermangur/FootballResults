package com.gursil.footballresults;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gursil.footballresults.R;
import com.gursil.footballresults.Models.MatchContract;
import com.gursil.footballresults.database.DatabaseProvider;

import java.util.Calendar;

public class MatchDetail extends AppCompatActivity {

    private EditText etDate, etCity, etGroupA, etGroupB, etGoalsA, etGoalsB;
    private Button btnSave;
    private DatabaseProvider databaseProvider;
    private DatePickerDialog datePickerDialog;
    private long matchId = -1; // -1 indicates a new match

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        etDate = findViewById(R.id.etDate);
        etCity = findViewById(R.id.etCity);
        etGroupA = findViewById(R.id.etGroupA);
        etGroupB = findViewById(R.id.etGroupB);
        etGoalsA = findViewById(R.id.etGoalsA);
        etGoalsB = findViewById(R.id.etGoalsB);
        btnSave = findViewById(R.id.btnSave);

        databaseProvider = new DatabaseProvider(this);

        // Check if an ID was passed through the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MatchContract.MatchEntry._ID)) {
            matchId = intent.getLongExtra(MatchContract.MatchEntry._ID, -1);
            fetchMatchDetails(matchId);
        }


        // Set click listener on the EditText
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create DatePickerDialog
                datePickerDialog = new DatePickerDialog(MatchDetail
                        .this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Handle the selected date
                                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                etDate.setText(selectedDate);
                            }
                        }, year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();

                // Consume the click event
                v.clearFocus();
            }
        });

        // Set focus change listener on the EditText
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Show the DatePickerDialog when the EditText gains focus
                    etDate.callOnClick();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matchId == -1) {
                    saveMatchToDatabase();
                    finish();
                } else {
                    updateMatchInDatabase();
                }
            }
        });
    }

    @SuppressLint("Range")
    private void fetchMatchDetails(long id) {
        databaseProvider.open();
        Cursor cursor = databaseProvider.getMatchDetails(id);
        if (cursor != null && cursor.moveToFirst()) {
            etDate.setText(cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_DATE)));
            etCity.setText(cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_CITY)));
            etGroupA.setText(cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_GROUP_A)));
            etGroupB.setText(cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_GROUP_B)));
            etGoalsA.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NUM_GOALS_A))));
            etGoalsB.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NUM_GOALS_B))));
        }
        databaseProvider.close();
    }

    private void saveMatchToDatabase() {
        String date = etDate.getText().toString();
        String city = etCity.getText().toString();
        String groupA = etGroupA.getText().toString();
        String groupB = etGroupB.getText().toString();
        int goalsA = Integer.parseInt(etGoalsA.getText().toString());
        int goalsB = Integer.parseInt(etGoalsB.getText().toString());
        String outcome = "";
        if(goalsA > goalsB){
            outcome = "Win";
        }else if(goalsA < goalsB){
            outcome = "Loss";
        }else{
            outcome = "Draw";
        }

        databaseProvider.open();

        // Insert or update the match in the database
        if (matchId == -1) {
            // Insert a new match
            long newRowId = databaseProvider.insertMatchResult(date, city, groupA, groupB, goalsA, goalsB, outcome);
            if (newRowId != -1) {
                Toast.makeText(this, "Match added successfully", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                Toast.makeText(this, "Failed to add match", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update an existing match
            boolean isUpdated = databaseProvider.updateMatchResult(matchId, date, city, groupA, groupB, goalsA, goalsB, outcome);
            if (isUpdated) {
                Toast.makeText(this, "Match updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update match", Toast.LENGTH_SHORT).show();
            }
        }

        databaseProvider.close();
    }

    private void clearInputFields() {
        etDate.setText("");
        etCity.setText("");
        etGroupA.setText("");
        etGroupB.setText("");
        etGoalsA.setText("");
        etGoalsB.setText("");
    }


    @SuppressLint("Range")
    private void updateMatchInDatabase() {
        String date = etDate.getText().toString();
        String city = etCity.getText().toString();
        String groupA = etGroupA.getText().toString();
        String groupB = etGroupB.getText().toString();
        int goalsA = Integer.parseInt(etGoalsA.getText().toString());
        int goalsB = Integer.parseInt(etGoalsB.getText().toString());
        String outcome = "";
        if(goalsA > goalsB){
            outcome = "Win";
        }else if(goalsA < goalsB){
            outcome = "Loss";
        }else{
            outcome = "Draw";
        }

        databaseProvider.open();

        // Retrieve the existing match details from the database
        Cursor cursor = databaseProvider.getMatchDetails(matchId);
        if (cursor != null && cursor.moveToFirst()) {
            String oldGroupA = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_GROUP_A));
            String oldGroupB = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_GROUP_B));
            int oldGoalsA = cursor.getInt(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NUM_GOALS_A));
            int oldGoalsB = cursor.getInt(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NUM_GOALS_B));
            String oldOutcome = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_OUTCOME));
            cursor.close();

            // Update the match in the database
            boolean isUpdated = databaseProvider.updateMatchResult(matchId, date, city, groupA, groupB, goalsA, goalsB, outcome);

            if (isUpdated) {
                // Update the team stats based on the changes
                databaseProvider.updateTeamStatsOnMatchUpdate(oldGroupA, oldGoalsA, oldGoalsB, oldOutcome,
                        groupA, goalsA, goalsB, outcome);
                databaseProvider.updateTeamStatsOnMatchUpdate(oldGroupB, oldGoalsB, oldGoalsA, invertOutcome(oldOutcome),
                        groupB, goalsB, goalsA, invertOutcome(outcome));

                Toast.makeText(this, "Match updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update match", Toast.LENGTH_SHORT).show();
            }
        }

        databaseProvider.close();
    }

    private String invertOutcome(String outcome) {
        switch (outcome) {
            case "Win":
                return "Loss";
            case "Loss":
                return "Win";
            case "Draw":
                return "Draw";
            default:
                return "";
        }
    }

}