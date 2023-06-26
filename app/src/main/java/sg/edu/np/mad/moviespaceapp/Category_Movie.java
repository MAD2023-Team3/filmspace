package sg.edu.np.mad.moviespaceapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewAdapter;
import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.Model.MovieModelClass;


public class Category_Movie extends Fragment implements HomeRecyclerViewInterface {
    View view;

    RecyclerView movie_category_recyclerview;
    List<MovieModelClass> movie_category_list;
    String JSON_URL;

    Integer grid_column_count;
    public Category_Movie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_movie, container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            JSON_URL = bundle.getString("api");
            Log.d("api",JSON_URL);
        }

        grid_column_count = getResources().getInteger(R.integer.grid_column_layout);
        movie_category_list = new ArrayList<>();
        movie_category_recyclerview = view.findViewById(R.id.movie_category_recyclerview);

        GetData getData = new GetData(JSON_URL);
        getData.execute();

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
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                // scans the jsonArray received from api request turns the request to
                // MovieModelClass and inserts that in movieList
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieModelClass model = new MovieModelClass();
                    model.setId(jsonObject1.getString("id"));
                    model.setMovie_name(jsonObject1.getString("title"));
                    model.setImg(jsonObject1.getString("poster_path"));

                    movie_category_list.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            putDataIntoRecyclerView(movie_category_list,movie_category_recyclerview,"");
        }
    }
    // end: converting json into object

    private void putDataIntoRecyclerView(List<MovieModelClass> movieList, RecyclerView recyclerView,String recyclerviewIdentifier){
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(getContext(),movieList,this,recyclerviewIdentifier);
        recyclerView.setAdapter(adapter);
        GridLayoutManager myLayoutManager = new GridLayoutManager(getContext(),grid_column_count);
        recyclerView.setLayoutManager(myLayoutManager);
    }

    @Override
    public void onItemClick(int position, String recyclerViewIdentifier) {
        Bundle bundle = new Bundle();
        bundle.putString("Movie_Id",movie_category_list.get(position).getId());

        Movie_details_fragment movie_details_fragment = new Movie_details_fragment();
        movie_details_fragment.setArguments(bundle);
        // fragment transaction
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,movie_details_fragment).commit();
    }
}