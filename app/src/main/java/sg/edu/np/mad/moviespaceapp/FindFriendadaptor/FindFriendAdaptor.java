package sg.edu.np.mad.moviespaceapp.FindFriendadaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sg.edu.np.mad.moviespaceapp.Login;
import sg.edu.np.mad.moviespaceapp.Model.FindFriendViewModel;
import sg.edu.np.mad.moviespaceapp.Model.UserModelClass;
import sg.edu.np.mad.moviespaceapp.R;

public class FindFriendAdaptor extends FirestoreRecyclerAdapter<UserModelClass, FindFriendAdaptor.UserModelViewHolder> {
    Context context;

    public FindFriendAdaptor(@androidx.annotation.NonNull FirestoreRecyclerOptions<UserModelClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@androidx.annotation.NonNull UserModelViewHolder holder, int position, @androidx.annotation.NonNull UserModelClass model) {
        String currentuser_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference_user = FirebaseFirestore.getInstance().collection("users").document(model.getUserId());
        // set text for username
        if (!model.getUserId().equals(currentuser_id)) {
            holder.usernameText.setText(model.getUsername());
        } else {
            holder.usernameText.setText(model.getUsername() + "(Me)");
            holder.send_friend_request_btn.setText("Me");
        }

        if (model.getFriend_Request() != null && model.getFriend_Request().contains(currentuser_id)) {
            // when a friend request is already sent
            holder.send_friend_request_btn.setText("Request Sent");
        } else if (model.getFriends_list() != null && model.getFriends_list().contains(currentuser_id)) {
            // when the user is already a friend of ours
            holder.send_friend_request_btn.setText("Added");
        }
        // retrieve their profile_picture
        StorageReference firebasestorage_reference = FirebaseStorage.getInstance().getReference().child("profile_pic").child(model.getUserId());
        firebasestorage_reference.getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(holder.profile_picture);
                    }
                });

        holder.send_friend_request_btn.setOnClickListener(v -> {
            if (model.getUserId().equals(currentuser_id)) {
                // validate if the we are clicking is our own profile
                Toast.makeText(context, "Cannot click on your own profile", Toast.LENGTH_SHORT).show();
            } else if (model.getFriend_Request() != null && model.getFriend_Request().contains(currentuser_id)) {
                // when a friend request is already sent
                Toast.makeText(context, "Friend Request Already Sent", Toast.LENGTH_SHORT).show();
            } else if (model.getFriend_Request() != null && model.getFriends_list().contains(currentuser_id)) {
                // when the user is already a friend of ours
                Toast.makeText(context, "Friend Request Already Sent", Toast.LENGTH_SHORT).show();
            } else {
                documentReference_user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.contains("Friend_Request")) {
                            // The document contains a friend request list
                            List<String> Friend_Request_Array = (List<String>) documentSnapshot.get("Friend_Request");
                            // if array containes currentuser_id then dont do anything
                            // else add currenuserid to array
                            if (!Friend_Request_Array.contains(currentuser_id)) {
                                Friend_Request_Array.add(currentuser_id);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Friend_Request", Friend_Request_Array);
                                documentReference_user.set(user, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                holder.send_friend_request_btn.setText("Request Sent");
                                                Toast.makeText(context, "Friend request sent", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // if inside request
                                Toast.makeText(context, "Request already sent", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // The document does not contain the specified field
                            // add the friend request list
                            List<String> watchlater_list = new ArrayList<String>();
                            watchlater_list.add(currentuser_id);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Friend_Request", watchlater_list);
                            documentReference_user.set(user, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // when its already in your watch later
                                            Toast.makeText(context, "Friend request sent", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }

    @androidx.annotation.NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.findfriends_recyclerview_item, parent, false);
        return new UserModelViewHolder(view);
    }
    class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        ImageView profile_picture;
        Button send_friend_request_btn;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            profile_picture = itemView.findViewById(R.id.default_profile_picture);
            send_friend_request_btn = itemView.findViewById(R.id.add_friend_btn);

        }
    }
}