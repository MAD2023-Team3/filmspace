package sg.edu.np.mad.moviespaceapp.PopularActorAdaptors;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.moviespaceapp.R;

public class PopularActorViewHolder extends RecyclerView.ViewHolder {
    ImageView popular_actor_image;
    TextView actor_name;
    PopularActorRecyclerViewInterface popularActorRecyclerViewInterface;

    public PopularActorViewHolder(@NonNull View itemView,PopularActorRecyclerViewInterface popularActorRecyclerViewInterface) {
        super(itemView);
        popular_actor_image = itemView.findViewById(R.id.popular_actor_img);
        actor_name = itemView.findViewById(R.id.popular_actor_name_placeholder);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popularActorRecyclerViewInterface != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        popularActorRecyclerViewInterface.onItemClick(position);
                    }
                }
            }
        });
    }
}
