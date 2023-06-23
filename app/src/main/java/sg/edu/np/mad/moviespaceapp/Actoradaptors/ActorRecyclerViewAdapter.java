package sg.edu.np.mad.moviespaceapp.Actoradaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import sg.edu.np.mad.moviespaceapp.ActorModelClass;
import sg.edu.np.mad.moviespaceapp.HomeRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.HomeViewHolder;
import sg.edu.np.mad.moviespaceapp.MovieModelClass;
import sg.edu.np.mad.moviespaceapp.R;

public class ActorRecyclerViewAdapter extends RecyclerView.Adapter<ActorHomeViewHolder> {
    private Context mContext;
    private List<ActorModelClass> mData;

    public ActorRecyclerViewAdapter(Context mContext, List<ActorModelClass> mdata) {
        this.mContext = mContext;
        this.mData = mdata;
    }

    @NonNull
    @Override
    public ActorHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.actor_card_item,parent,false);

        ActorHomeViewHolder holder = new ActorHomeViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ActorHomeViewHolder holder, int position) {
        ActorModelClass obj = mData.get(position);
        if(obj.getKnown_for_department().equals("Acting")){
            holder.actor_name.setText(obj.getActor_name());
            // using glide library to display the image

            // https://image.tmdb.org/t/p/w500/[imagelink]
            Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + obj.getActor_profile_path()).into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
