package sg.edu.np.mad.moviespaceapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Update_profile_fragment extends Fragment {

    View view;

    ImageView default_profile_pic;
    FloatingActionButton upload_profile_pic_btn;
    Button upload_profile_btn;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    FirebaseFirestore firestoredb;
    //

    public Update_profile_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            Glide.with(getContext()).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(default_profile_pic);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.update_profile_fragment, container, false);

        // firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        StorageReference firebasestorage_reference = FirebaseStorage.getInstance().getReference().child("profile_pic").child(userUid);

        default_profile_pic = view.findViewById(R.id.default_profile_picture);
        upload_profile_pic_btn = view.findViewById(R.id.upload_profilepic_btn);
        upload_profile_btn = view.findViewById(R.id.upload_profile_btn);

        // set current photo in the default profile pic
        firebasestorage_reference.getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        Glide.with(getContext()).load(uri).apply(RequestOptions.circleCropTransform()).into(default_profile_pic);
                    }
                });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        // firestore database(update values)
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);
        StorageReference firebasestorage_reference = FirebaseStorage.getInstance().getReference().child("profile_pic").child(userUid);

        upload_profile_pic_btn.setOnClickListener((v)->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        upload_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImageUri!=null){
                    firebasestorage_reference.putFile(selectedImageUri)
                            .addOnCompleteListener(task -> {
                                // fragment transaction
                                getFragmentManager().beginTransaction().replace(R.id.frameLayout,new Profilefragment()).commit();
                            });
                }else{
                    // fragment transaction
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout,new Profilefragment()).commit();
                }
            }
        });

    }
}