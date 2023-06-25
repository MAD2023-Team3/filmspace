package sg.edu.np.mad.moviespaceapp.PopularActorAdaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.Model.PopularActorModelClass;
import sg.edu.np.mad.moviespaceapp.R;

public class PopularActorRecyclerViewAdaptor extends RecyclerView.Adapter<PopularActorViewHolder>{

    private final PopularActorRecyclerViewInterface popularActorRecyclerViewInterface;
    private Context mContext;
    private List<PopularActorModelClass> mData;

    public PopularActorRecyclerViewAdaptor(Context mContext, List<PopularActorModelClass> mdata,PopularActorRecyclerViewInterface popularActorRecyclerViewInterface) {
        this.mContext = mContext;
        this.mData = mdata;
        this.popularActorRecyclerViewInterface = popularActorRecyclerViewInterface;
    }

    @NonNull
    @Override
    public PopularActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.popular_actor_card_item,parent,false);

        PopularActorViewHolder holder = new PopularActorViewHolder(view,popularActorRecyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PopularActorViewHolder holder, int position) {
        PopularActorModelClass obj = mData.get(position);
        Log.d("data",obj.toString());
        holder.actor_name.setText(obj.getActor_name());

        // using glide library to display the image
        // https://image.tmdb.org/t/p/w500/[imagelink]
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + obj.getProfile_path()).into(holder.popular_actor_image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
