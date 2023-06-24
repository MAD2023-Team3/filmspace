package sg.edu.np.mad.moviespaceapp.Homeadaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import sg.edu.np.mad.moviespaceapp.MovieModelClass;
import sg.edu.np.mad.moviespaceapp.R;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private final HomeRecyclerViewInterface homeRecyclerViewInterface;
    private Context mContext;
    private List<MovieModelClass> mData;

    private String recyclerViewIdentifier;

    public HomeRecyclerViewAdapter(Context mContext, List<MovieModelClass> mdata,HomeRecyclerViewInterface homeRecyclerViewInterface, String recyclerViewIdentifier) {
        this.mContext = mContext;
        this.mData = mdata;
        this.homeRecyclerViewInterface = homeRecyclerViewInterface;
        this.recyclerViewIdentifier = recyclerViewIdentifier;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.home_movies_list,parent,false);

        HomeViewHolder holder = new HomeViewHolder(view,recyclerViewIdentifier,homeRecyclerViewInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        MovieModelClass obj = mData.get(position);
        // using glide library to display the image

        // https://image.tmdb.org/t/p/w500/[imagelink]
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + obj.getImg()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}