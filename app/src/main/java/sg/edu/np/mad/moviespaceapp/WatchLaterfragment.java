package sg.edu.np.mad.moviespaceapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewAdapter;
import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewInterface;

public class WatchLaterfragment extends Fragment implements HomeRecyclerViewInterface{

    // recycler view
    List<MovieModelClass> movieList;
    RecyclerView watch_later_recyclerview;
    //

    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    FirebaseFirestore firestoredb;
    List<String> watchlater_list;
    String JSON_URL;
    //

    // api json link
    View view;
    public WatchLaterfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_watch_laterfragment, container, false);

        movieList= new ArrayList<>();

        // firestore database
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);
        //

        // retrieving watchlater array from user info
        documentReference_user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Retrieve field field
                List<String> array = (List<String>) documentSnapshot.get("watchlist_array");
                // add the retrieved array to watchlater_list
                watchlater_list=new ArrayList<>();
                if (array != null) {
                    for (Object item : array) {
                        watchlater_list.add((String) item);
                    }
                }
                Log.d("Firestore_sada", "Field value: " + watchlater_list);

                watch_later_recyclerview = view.findViewById(R.id.watch_later_recyclerview);

                for(int i =0;i< watchlater_list.size();i++){
                    Log.d("sds",watchlater_list.get(i));
                    JSON_URL = String.format("https://api.themoviedb.org/3/movie/%s?api_key=d51877fbcef44b5e6c0254522b9c1a35",watchlater_list.get(i));
                    GetData getData = new GetData(JSON_URL);
                    getData.execute();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // movie recycler view
        watch_later_recyclerview = view.findViewById(R.id.watch_later_recyclerview);

        return view;
    }

    // start: code block for GetData
    public class GetData extends AsyncTask<String,String,String> {
        private String jsonUrl;

        public GetData(String jsonUrl){
            this.jsonUrl = jsonUrl;
        }
        @Override
        protected String doInBackground(String... strings){
            String current = "";

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    Log.d("JSON",jsonUrl);
                    url = new URL(jsonUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while(data != -1){
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    if(urlConnection!= null){
                        urlConnection.disconnect();;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return current;
        }

        // end: code block for GetData

        // start: converting json into object
        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                // scans the jsonArray received from api request turns the request to
                // MovieModelClass and inserts that in movieList

                MovieModelClass model = new MovieModelClass();
                model.setId(jsonObject.getString("id"));
                model.setMovie_name(jsonObject.getString("title"));
                model.setImg(jsonObject.getString("poster_path"));
                movieList.add(model);
            }catch (JSONException e){
                e.printStackTrace();
            }

            putDataIntoRecyclerView(movieList,watch_later_recyclerview,"");

        }
    }

    // start: recyclerview code block sd
    private void putDataIntoRecyclerView(List<MovieModelClass> movieList,RecyclerView recyclerView,String recyclerviewIdentifier){
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(getContext(),movieList,this,recyclerviewIdentifier);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    public void onItemClick(int position,String recyclerViewIdentifier) {
        Bundle bundle = new Bundle();
        bundle.putString("Movie_Id",movieList.get(position).getId());

        Movie_details_fragment movie_details_fragment = new Movie_details_fragment();
        movie_details_fragment.setArguments(bundle);
        // fragment transaction
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,movie_details_fragment).commit();
    }
    // end: when clicking on a recyclerview item

}