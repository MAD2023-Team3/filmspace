package sg.edu.np.mad.moviespaceapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewHolder extends RecyclerView.ViewHolder{
    ImageView img;

    HomeRecyclerViewInterface homeRecyclerViewInterface;

    public HomeViewHolder(@NonNull View itemView,HomeRecyclerViewInterface homeRecyclerViewInterface) {
        super(itemView);

        img = itemView.findViewById(R.id.imageview);

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
