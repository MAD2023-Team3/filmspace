package sg.edu.np.mad.moviespaceapp.Friend_requestadaptor;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    Context context;

    public Friend_requestRecyclerViewAdaptor(ArrayList<UserModelClass> Friend_Requests, Context context) {
        this.Friend_Requests = Friend_Requests;
        this.context = context;
    }

    @NonNull
    @Override
    public Friend_requestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_reyclerview_item,parent,false);

        return new Friend_requestViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull Friend_requestViewHolder holder, int position) {
        String username = Friend_Requests.get(position).getUsername();
        holder.user_name_text.setText(username);

        // retrieve their profile_picture
        StorageReference firebasestorage_reference = FirebaseStorage.getInstance().getReference().child("profile_pic").child(Friend_Requests.get(position).getUserId());
        firebasestorage_reference.getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(holder.profile_picture);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return Friend_Requests.size();
    }
}

class Friend_requestViewHolder extends RecyclerView.ViewHolder{
    TextView user_name_text;
    ImageView profile_picture;
    Button friend_request_btn;
    private Friend_requestRecyclerViewAdaptor adaptor;

    public Friend_requestViewHolder(@NonNull View itemView){
        super(itemView);

        user_name_text = itemView.findViewById(R.id.user_name_text);
        friend_request_btn = itemView.findViewById(R.id.friend_request_btn);
        profile_picture = itemView.findViewById(R.id.default_profile_picture);
        friend_request_btn.setOnClickListener(new View.OnClickListener() {
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

                                    // adds the friend uid to a Friends_list in firestore for the current user
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

                                    // for the user who sent the friend request
                                    firestoredb.collection("users").document(friend_request_Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.contains("Friends_list")) {
                                                    List<String> Friends_list = (List<String>)document.get("Friends_list");
                                                    Friends_list.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                    // update values
                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put("Friends_list", Friends_list);

                                                    firestoredb.collection("users").document(friend_request_Uid)
                                                            .set(data, SetOptions.merge());
                                                }else {
                                                    // update values
                                                    ArrayList<String> Friends_list = new ArrayList<>();
                                                    Friends_list.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put("Friends_list", Friends_list);

                                                    firestoredb.collection("users").document(friend_request_Uid)
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
