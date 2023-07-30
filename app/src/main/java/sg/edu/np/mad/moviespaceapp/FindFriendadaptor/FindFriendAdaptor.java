package sg.edu.np.mad.moviespaceapp.FindFriendadaptor;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.util.List;

import sg.edu.np.mad.moviespaceapp.Model.FindFriendViewModel;
import sg.edu.np.mad.moviespaceapp.R;

public class FindFriendAdaptor extends FirestoreRecyclerAdapter<FindFriendViewModel, FindFriendAdaptor.UserModelViewHolder> {
    Context context;

    public FindFriendAdaptor(@androidx.annotation.NonNull FirestoreRecyclerOptions<FindFriendViewModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@androidx.annotation.NonNull UserModelViewHolder holder, int position, @androidx.annotation.NonNull FindFriendViewModel model) {
        String currentuserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(!model.getUserId().equals(currentuserId)){
            holder.usernameText.setText(model.getUsername());
        }else {
            holder.usernameText.setText(model.getUsername() + "(Me)");
        }

        // retrieve their profile_picture
        StorageReference firebasestorage_reference = FirebaseStorage.getInstance().getReference().child("profile_pic").child(model.getUserId());
        firebasestorage_reference.getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(holder.profile_picture);
                    }
                });
    }

    @androidx.annotation.NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.findfriends_recyclerview_item,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        ImageView profile_picture;

        public UserModelViewHolder(@NonNull View itemView){
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            profile_picture = itemView.findViewById(R.id.default_profile_picture);
        }
    }
}
