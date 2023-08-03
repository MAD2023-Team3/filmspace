package sg.edu.np.mad.moviespaceapp.Friend_adaptor;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.moviespaceapp.Actoradaptors.ActorRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.Friend_request_fragment;
import sg.edu.np.mad.moviespaceapp.Model.LeaderboardModelClass;
import sg.edu.np.mad.moviespaceapp.Model.UserModelClass;
import sg.edu.np.mad.moviespaceapp.R;

public class Friend_RecyclerView_Adaptor extends RecyclerView.Adapter<Friend_ViewHolder>{

    List<UserModelClass> Friend_list;
    private final Friend_RecyclerView_Interface friend_RecyclerView_Interface;
    public Friend_RecyclerView_Adaptor(List<UserModelClass> friend_list,Friend_RecyclerView_Interface friend_RecyclerView_Interface) {
        this.Friend_list = friend_list;
        this.friend_RecyclerView_Interface = friend_RecyclerView_Interface;
    }

    @NonNull
    @Override
    public Friend_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_reyclerview_item,parent,false);

        return new Friend_ViewHolder(view,friend_RecyclerView_Interface).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull Friend_ViewHolder holder, int position) {
        String username = Friend_list.get(position).getUsername();
        holder.user_name_text.setText(username);

    }

    @Override
    public int getItemCount() {
        return Friend_list.size();
    }
}

class Friend_ViewHolder extends RecyclerView.ViewHolder{
    TextView user_name_text;
    ImageView friend_profile_pic;

    Friend_RecyclerView_Interface friend_RecyclerView_Interface;
    private Friend_RecyclerView_Adaptor adaptor;

    public Friend_ViewHolder(@NonNull View itemView,Friend_RecyclerView_Interface friend_RecyclerView_Interface){
        super(itemView);

        user_name_text = itemView.findViewById(R.id.friend_username);
        friend_profile_pic = itemView.findViewById(R.id.friend_profile_picture);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friend_RecyclerView_Interface != null){
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        friend_RecyclerView_Interface.onItemClick(position);
                    }
                }
            }
        });
    }
    public Friend_ViewHolder linkAdapter(Friend_RecyclerView_Adaptor adaptor){
        this.adaptor = adaptor;
        return this;
    }
}
