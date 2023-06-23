package sg.edu.np.mad.moviespaceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.MyViewHolder> {

    private final HomeRecyclerViewInterface homeRecyclerViewInterface;
    private Context mContext;
    private List<MovieModelClass> mData;

    public HomeRecyclerViewAdapter(Context mContext, List<MovieModelClass> mdata,HomeRecyclerViewInterface homeRecyclerViewInterface) {
        this.mContext = mContext;
        this.mData = mdata;
        this.homeRecyclerViewInterface = homeRecyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.home_movies_list,parent,false);

        return new MyViewHolder(view,homeRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getMovie_name());

        // using glide library to display the image

        // https://image.tmdb.org/t/p/w500/[imagelink]
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + mData.get(position).getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // viewholder
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView img;

        public MyViewHolder(@NonNull View itemView,HomeRecyclerViewInterface homeRecyclerViewInterface) {
            super(itemView);

            name=itemView.findViewById(R.id.name_txt);
            img = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(homeRecyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            homeRecyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}