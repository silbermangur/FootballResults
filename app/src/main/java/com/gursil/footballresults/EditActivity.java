package com.gursil.footballresults;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gursil.footballresults.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gursil.footballresults.Models.Match;
import com.gursil.footballresults.Models.MatchContract;
import com.gursil.footballresults.database.DatabaseProvider;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {


    private RecyclerView rvMatches;
    private MatchAdapter matchAdapter;
    private List<Match> matchList;

    private Button backBtn;
    private FloatingActionButton fabBtn;


    @SuppressLint("MissingInflatedId") //TODO cheack it out!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        rvMatches = findViewById(R.id.rvMatches);
        fabBtn = findViewById(R.id.fabBtn);
        rvMatches.setLayoutManager(new LinearLayoutManager(this));
        backBtn = findViewById(R.id.backButton);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditActivity.this, MatchDetail.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @SuppressLint("Range")
    private List<Match> fetchMatchesFromDatabase() {
        List<Match> matchList = new ArrayList<>();
        DatabaseProvider databaseProvider = new DatabaseProvider(this);
        databaseProvider.open();

        Cursor cursor = databaseProvider.getAllMatches();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(MatchContract.MatchEntry._ID));
                String date = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_DATE));
                String city = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_CITY));
                String groupA = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_GROUP_A));
                String groupB = cursor.getString(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_GROUP_B));
                int goalsA = cursor.getInt(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NUM_GOALS_A));
                int goalsB = cursor.getInt(cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NUM_GOALS_B));

                Match match = new Match(id, date, city, groupA, groupB, goalsA, goalsB);
                matchList.add(match);
            } while (cursor.moveToNext());
        }

        cursor.close();
        databaseProvider.close();

        return matchList;
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Fetch the match list from the database
        matchList = fetchMatchesFromDatabase();

        matchAdapter = new MatchAdapter(this, matchList);
        rvMatches.setAdapter(matchAdapter);
    }
}