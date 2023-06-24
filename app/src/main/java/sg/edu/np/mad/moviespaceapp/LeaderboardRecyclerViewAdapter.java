package sg.edu.np.mad.moviespaceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    List<LeaderboardModelClass> leaderboardList;

    public LeaderboardAdapter(List<LeaderboardModelClass> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_leaderboardfragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardModelClass item = leaderboardList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textActor;
        private TextView textFame;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textActor = itemView.findViewById(R.id.text_actor);
            textFame = itemView.findViewById(R.id.text_fame);
        }

        public void bind(LeaderboardModelClass item) {
            textActor.setText(item.getActor());
            textFame.setText(String.valueOf(item.getFame()));
        }
    }
}
