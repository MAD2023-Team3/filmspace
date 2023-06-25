package sg.edu.np.mad.moviespaceapp.Leaderboardadaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import sg.edu.np.mad.moviespaceapp.Model.LeaderboardModelClass;
import sg.edu.np.mad.moviespaceapp.R;

public class LeaderboardRecyclerAdaptor extends RecyclerView.Adapter<LeaderboardViewHolder>{
    private Context mContext;
    private List<LeaderboardModelClass> mData;
    public LeaderboardRecyclerAdaptor(Context mContext, List<LeaderboardModelClass> leaderboardList) {
        this.mData = leaderboardList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.leaderboard_recyclerview_item,parent,false);

        LeaderboardViewHolder holder = new LeaderboardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardModelClass obj = mData.get(position);

        holder.textActor.setText(obj.getActor());
        holder.textFame.setText(obj.getFame());

        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + obj.getProfile_path()).into(holder.actor_profile_image);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
