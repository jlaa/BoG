package br.edu.ifpe.tads.pdm.bog;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Games> {

    private AppCompatActivity activity;
    private List<Games> gameList;

    public ListViewAdapter(AppCompatActivity context, int resource, List<Games> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.gameList = objects;
    }

    @Override
    public Games getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_rating_games, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));
        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getRatingBar());
        holder.gameName.setText(getItem(position).getNome());

        return convertView;
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Games item = getItem(position);
                item.setRatingBar(v);
                Log.i("Adapter", "star: " + v);
            }
        };
    }

    static class ViewHolder {
        public RatingBar ratingBar;
        public TextView gameName;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            gameName = (TextView) view.findViewById(R.id.nomeJogo);
        }

    }
}