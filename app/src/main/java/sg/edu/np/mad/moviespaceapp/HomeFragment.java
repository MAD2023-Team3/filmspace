package sg.edu.np.mad.moviespaceapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class HomeFragment extends Fragment implements HomeRecyclerViewInterface {

    View view;

    // recycler view
    List<MovieModelClass> movieList;
    RecyclerView recyclerView;
    //

    // api url
    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    //

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_homefragment, container, false);

        // start: movie recycler view
        movieList= new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        GetData getData = new GetData();
        getData.execute();
        // end: movie recycler view

        Bundle bundle = new Bundle();
        bundle.putString("key","value");

        return view;
    }

    // start: code block for GetData
    public class GetData extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings){
            String current = "";

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
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
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                // scans the jsonArray received from api request turns the request to
                // MovieModelClass and inserts that in movieList
                for(int i = 0; i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieModelClass model = new MovieModelClass();
                    model.setId(jsonObject1.getString("id"));
                    model.setMovie_name(jsonObject1.getString("title"));
                    model.setImg(jsonObject1.getString("poster_path"));

                    movieList.add(model);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            putDataIntoRecyclerView(movieList);
        }
    }
    // end: converting json into object

    // start: recyclerview code block
    private void putDataIntoRecyclerView(List<MovieModelClass> movieList){
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(getContext(),movieList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    // start: when clicking on a recyclerview item
    @Override
    public void onItemClick(int position) {
        Log.d("sd","ASDsdasd");
        Bundle bundle = new Bundle();
        bundle.putString("Movie_Id",movieList.get(position).getId());
        bundle.putString("movie_name",movieList.get(position).getMovie_name());
        bundle.putString("poster_path",movieList.get(position).getImg());

        Movie_details_fragment movie_details_fragment = new Movie_details_fragment();
        movie_details_fragment.setArguments(bundle);
        // fragment transaction
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,movie_details_fragment).commit();
    }
    // end: when clicking on a recyclerview item

    /// end: recyclerview code block

    // start: search MovieModelClass object by their id
    public MovieModelClass SearchObjMovie(String Id){
        for (int i = 0;i < movieList.size();i++){
            if(movieList.get(i).getId().toString() == Id){
                return movieList.get(i);
            }else{
                Log.d("meg","cant find ass");
            }
        }
        return null;
    }
    // end: search object by their id
}