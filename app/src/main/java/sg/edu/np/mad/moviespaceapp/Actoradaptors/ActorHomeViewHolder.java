package sg.edu.np.mad.moviespaceapp.Actoradaptors;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.R;

public class ActorHomeViewHolder extends RecyclerView.ViewHolder {
    TextView actor_name;
    ImageView img;
    TextView character_name;

    ActorRecyclerViewInterface actorRecyclerViewInterface;

    public ActorHomeViewHolder(View itemView,ActorRecyclerViewInterface actorRecyclerViewInterface){
        super(itemView);
        actor_name= itemView.findViewById(R.id.actor_name_placeholder);
        img = itemView.findViewById(R.id.actor_img);
        character_name = itemView.findViewById(R.id.actor_character_name_placeholder);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actorRecyclerViewInterface != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        actorRecyclerViewInterface.onItemClick(position);
                    }
                }
            }
        });
    }
}
