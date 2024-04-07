package com.gursil.footballresults;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gursil.footballresults.R;
import com.gursil.footballresults.Models.GoalsContract;
import com.gursil.footballresults.Models.MatchContract;
import com.gursil.footballresults.Models.TeamStats;
import com.gursil.footballresults.database.DatabaseProvider;

import java.util.ArrayList;
import java.util.List;

public class MainStats extends AppCompatActivity {

    RecyclerView recyclerView;
    private TeamStatsAdapter teamStatsAdapter;

    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stats);
        backBtn = findViewById(R.id.backToMainBtn);
        recyclerView = findViewById(R.id.statsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        fetchTeamStatsFromDatabase();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void fetchTeamStatsFromDatabase() {
        DatabaseProvider databaseProvider = new DatabaseProvider(this);
        databaseProvider.open();
        List<TeamStats> teamStatsList = databaseProvider.getAllTeamStats();
        databaseProvider.close();

        teamStatsAdapter = new TeamStatsAdapter(this, teamStatsList);
        recyclerView.setAdapter(teamStatsAdapter);

    }



}