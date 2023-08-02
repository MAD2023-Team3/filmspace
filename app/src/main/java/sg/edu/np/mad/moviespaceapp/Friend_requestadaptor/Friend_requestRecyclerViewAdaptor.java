package sg.edu.np.mad.moviespaceapp.Friend_requestadaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.moviespaceapp.Friend_request_fragment;
import sg.edu.np.mad.moviespaceapp.Model.LeaderboardModelClass;
import sg.edu.np.mad.moviespaceapp.Model.UserModelClass;
import sg.edu.np.mad.moviespaceapp.R;

public class Friend_requestRecyclerViewAdaptor extends RecyclerView.Adapter<Friend_requestViewHolder>{

    ArrayList<UserModelClass> Friend_Requests;

    public Friend_requestRecyclerViewAdaptor(ArrayList<UserModelClass> Friend_Requests) {
        this.Friend_Requests = Friend_Requests;
    }

    @NonNull
    @Override
    public Friend_requestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.findfriends_recyclerview_item,parent,false);

        return new Friend_requestViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull Friend_requestViewHolder holder, int position) {
        String username = Friend_Requests.get(position).getUsername();
        holder.user_name_text.setText(username);

    }

    @Override
    public int getItemCount() {
        return Friend_Requests.size();
    }
}

class Friend_requestViewHolder extends RecyclerView.ViewHolder{
    TextView user_name_text;
    Button add_friend_btn;
    private Friend_requestRecyclerViewAdaptor adaptor;

    public Friend_requestViewHolder(@NonNull View itemView){
        super(itemView);

        user_name_text = itemView.findViewById(R.id.user_name_text);
        add_friend_btn = itemView.findViewById(R.id.add_friend_btn);
        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adaptor.Friend_Requests.remove(getAbsoluteAdapterPosition());
            }
        });
    }
    public Friend_requestViewHolder linkAdapter(Friend_requestRecyclerViewAdaptor adaptor){
        this.adaptor = adaptor;
        return this;
    }
}
