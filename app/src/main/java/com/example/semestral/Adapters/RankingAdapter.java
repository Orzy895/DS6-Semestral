package com.example.semestral.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.semestral.Entidades.Ranking;
import com.example.semestral.R;

import java.util.List;

public class RankingAdapter extends ArrayAdapter<Ranking> {

    public RankingAdapter(Context context, List<Ranking> rankings){
        super(context, 0, rankings);
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewranking, parent, false);
        }
        Ranking ranking = getItem(pos);
        TextView username = convertView.findViewById(R.id.resultName);
        TextView score = convertView.findViewById(R.id.resultScore);
        if(ranking != null){
            username.setText(ranking.getUsername());
            score.setText(ranking.getScores());
        }
        return convertView;
    }

}
