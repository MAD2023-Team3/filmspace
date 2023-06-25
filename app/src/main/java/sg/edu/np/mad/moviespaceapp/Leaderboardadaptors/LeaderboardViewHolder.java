package sg.edu.np.mad.moviespaceapp.Leaderboardadaptors;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.moviespaceapp.R;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
    TextView textActor;
    TextView textFame;
    ImageView actor_profile_image;

    public LeaderboardViewHolder(@NonNull View itemView) {
        super(itemView);
        textActor = itemView.findViewById(R.id.leaderboard_name);
        textFame = itemView.findViewById(R.id.leaderboard_actor_fame);
        actor_profile_image = itemView.findViewById(R.id.leaderboard_actor_img);
    }

}
