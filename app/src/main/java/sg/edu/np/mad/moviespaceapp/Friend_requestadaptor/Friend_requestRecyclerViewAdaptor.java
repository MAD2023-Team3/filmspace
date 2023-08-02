package sg.edu.np.mad.moviespaceapp.Friend_requestadaptor;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
                FirebaseFirestore firestoredb = FirebaseFirestore.getInstance();
                String friend_request_Uid = adaptor.Friend_Requests.get(getAbsoluteAdapterPosition()).getUserId();
                DocumentReference user_documentreferece = firestoredb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                // users friend request list
                user_documentreferece.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if(document.contains("Friend_Request")){
                                    // removes the recyclerview item from adaptor
                                    adaptor.Friend_Requests.remove(getAbsoluteAdapterPosition());
                                    adaptor.notifyItemRemoved(getAbsoluteAdapterPosition());

                                    List<String> Friend_request_list = (List<String>)document.get("Friend_Request");
                                    // removes the friend request from the firestore
                                    Friend_request_list.remove(friend_request_Uid);

                                    // update values
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("Friend_Request", Friend_request_list);

                                    firestoredb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .set(data, SetOptions.merge());

                                    // adds the friend uid to a Friends_list in firestore
                                    user_documentreferece.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.contains("Friends_list")) {
                                                    List<String> Friends_list = (List<String>)document.get("Friends_list");
                                                    Friends_list.add(friend_request_Uid);

                                                    // update values
                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put("Friends_list", Friends_list);

                                                    firestoredb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .set(data, SetOptions.merge());
                                                }else {
                                                    // update values
                                                    ArrayList<String> Friends_list = new ArrayList<>();
                                                    Friends_list.add(friend_request_Uid);
                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put("Friends_list", Friends_list);

                                                    firestoredb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .set(data, SetOptions.merge());
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        });
    }
    public Friend_requestViewHolder linkAdapter(Friend_requestRecyclerViewAdaptor adaptor){
        this.adaptor = adaptor;
        return this;
    }
}
