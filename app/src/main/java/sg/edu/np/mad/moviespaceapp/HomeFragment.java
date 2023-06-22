package sg.edu.np.mad.moviespaceapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.helper.widget.Carousel;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
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

public class HomeFragment extends Fragment {

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

        // movie recycler view
        movieList= new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        GetData getData = new GetData();
        getData.execute();
        //
        return view;
    }

    // code block for GetData
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

    // recyclerview code block
    private void putDataIntoRecyclerView(List<MovieModelClass> movieList){
        Adapter adapter = new Adapter(getContext(),movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
    }
}