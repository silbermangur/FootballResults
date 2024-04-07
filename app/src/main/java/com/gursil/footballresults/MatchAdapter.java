package com.gursil.footballresults;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gursil.footballresults.R;
import com.google.android.material.button.MaterialButton;
import com.gursil.footballresults.Models.Match;
import com.gursil.footballresults.Models.MatchContract;
import com.gursil.footballresults.database.DatabaseProvider;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private List<Match> matchList;
    private Context context;
    private DatabaseProvider databaseProvider;

    public MatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
        databaseProvider = new DatabaseProvider(context);
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvCity, tvGroupA, tvGoalsA, tvGoalsB, tvGroupB;
        MaterialButton editBtn, deleteBtn;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvGroupA = itemView.findViewById(R.id.tvGroupA);
            tvGoalsA = itemView.findViewById(R.id.tvGoalsA);
            tvGoalsB = itemView.findViewById(R.id.tvGoalsB);
            tvGroupB = itemView.findViewById(R.id.tvGroupB);
            editBtn = itemView.findViewById(R.id.btnEdit);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Match match) {
            tvDate.setText(match.getDate());
            tvCity.setText(match.getCity());
            tvGroupA.setText(match.getGroupA());
            tvGoalsA.setText(String.valueOf(match.getGoalsA()));
            tvGoalsB.setText(String.valueOf(match.getGoalsB()));
            tvGroupB.setText(match.getGroupB());

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, MatchDetail.class);
                    intent.putExtra(MatchContract.MatchEntry._ID, match.getId());
                    context.startActivity(intent);

                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseProvider.deleteMatch(match.getId());
                    matchList.remove(match);
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
