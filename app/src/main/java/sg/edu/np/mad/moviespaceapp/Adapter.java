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

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private Context mContext;
    private List<MovieModelClass> mData;

    public Adapter(Context mContext, List<MovieModelClass> mdata) {
        this.mContext = mContext;
        this.mData = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.home_movies_list,parent,false);

        return new MyViewHolder(view);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_txt);
            img = itemView.findViewById(R.id.imageView);
        }
    }
}