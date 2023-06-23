package sg.edu.np.mad.moviespaceapp.Actoradaptors;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.moviespaceapp.R;

public class ActorHomeViewHolder extends RecyclerView.ViewHolder {
    TextView actor_name;
    ImageView img;

    public ActorHomeViewHolder(View itemView){
        super(itemView);
        actor_name= itemView.findViewById(R.id.actor_name_placeholder);
        img = itemView.findViewById(R.id.actor_img);
    }
}
