package sg.edu.np.mad.moviespaceapp.Leaderboardadaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.Model.LeaderboardModelClass;
import sg.edu.np.mad.moviespaceapp.R;

public class LeaderboardRecyclerAdaptor extends RecyclerView.Adapter<LeaderboardViewHolder>{
    private Context mContext;
    private List<LeaderboardModelClass> mData;

    private final LeaderboardRecyclerViewInterface leaderboardRecyclerViewInterface;
    public LeaderboardRecyclerAdaptor(Context mContext, List<LeaderboardModelClass> leaderboardList,LeaderboardRecyclerViewInterface leaderboardRecyclerViewInterface) {
        this.mData = leaderboardList;
        this.mContext = mContext;
        this.leaderboardRecyclerViewInterface=leaderboardRecyclerViewInterface;
    }


    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.leaderboard_recyclerview_item,parent,false);

        LeaderboardViewHolder holder = new LeaderboardViewHolder(view,leaderboardRecyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardModelClass obj = mData.get(position);

        holder.actor_placement.setText(String.valueOf(obj.getActor_placement()));
        holder.textActor.setText(obj.getActor_name());
        holder.textFame.setText(String.valueOf(obj.getFame()));
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + obj.getProfile_path()).into(holder.actor_profile_image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
