package com.gursil.footballresults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gursil.footballresults.Models.TeamStats;

import java.util.List;

public class TeamStatsAdapter extends RecyclerView.Adapter<TeamStatsAdapter.TeamStatsViewHolder> {

    private Context context;
    private List<TeamStats> teamStatsList;

    public TeamStatsAdapter(Context context, List<TeamStats> teamStatsList) {
        this.context = context;
        this.teamStatsList = teamStatsList;
    }

    @NonNull
    @Override
    public TeamStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.stats_item, parent, false);
        return new TeamStatsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamStatsViewHolder holder, int position) {
        TeamStats teamStats = teamStatsList.get(position);
        holder.bind(teamStats);
    }

    @Override
    public int getItemCount() {
        return teamStatsList.size();
    }

    public void setTeamStatsList(List<TeamStats> teamStatsList) {
        this.teamStatsList = teamStatsList;
        notifyDataSetChanged();
    }

    public class TeamStatsViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTeamName;
        private TextView tvGames;
        private TextView tvWins;
        private TextView tvDraws;
        private TextView tvLosses;
        private TextView tvGoalsScored;
        private TextView tvGoalsConceded;
        private TextView tvPoints;

        public TeamStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tvTeamName);
            tvGames = itemView.findViewById(R.id.tvGames);
            tvWins = itemView.findViewById(R.id.tvWins);
            tvDraws = itemView.findViewById(R.id.tvDraws);
            tvLosses = itemView.findViewById(R.id.tvLosses);
            tvGoalsScored = itemView.findViewById(R.id.tvGoalsScored);
            tvGoalsConceded = itemView.findViewById(R.id.tvGoalsConceded);
            tvPoints = itemView.findViewById(R.id.tvPoints);
        }

        public void bind(TeamStats teamStats) {
            tvTeamName.setText(teamStats.getTeamName());
            tvGames.setText(String.valueOf(teamStats.getGames()));
            tvWins.setText(String.valueOf(teamStats.getWins()));
            tvDraws.setText(String.valueOf(teamStats.getDraws()));
            tvLosses.setText(String.valueOf(teamStats.getLosses()));
            tvGoalsScored.setText(String.valueOf(teamStats.getGoalsScored()));
            tvGoalsConceded.setText(String.valueOf(teamStats.getGoalsConceded()));
            tvPoints.setText(String.valueOf(teamStats.getPoints()));
        }
    }
}