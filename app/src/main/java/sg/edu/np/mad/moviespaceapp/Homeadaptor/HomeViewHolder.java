package sg.edu.np.mad.moviespaceapp.Homeadaptor;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.R;

public class HomeViewHolder extends RecyclerView.ViewHolder{
    ImageView img;

    HomeRecyclerViewInterface homeRecyclerViewInterface;
    String recyclerViewIdentifier;

    public HomeViewHolder(@NonNull View itemView,String recyclerViewIdentifier,HomeRecyclerViewInterface homeRecyclerViewInterface) {
        super(itemView);

        img = itemView.findViewById(R.id.imageview);
        this.recyclerViewIdentifier=recyclerViewIdentifier;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeRecyclerViewInterface != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        homeRecyclerViewInterface.onItemClick(position,recyclerViewIdentifier);
                    }
                }
            }
        });
    }
}
