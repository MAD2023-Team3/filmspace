package sg.edu.np.mad.moviespaceapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class HomeFragment extends Fragment implements HomeRecyclerViewInterface {

    View view;

    // recycler view
    List<MovieModelClass> all_movieslist,popular_movieList,upcoming_movielist,now_playing_api_movielist,top_rated_movielist;
    RecyclerView popular_recyclerView,upcoming_recyclerview,now_playing_recyclerview,top_rated_recyclerview;
    //

    // api url
    private static String popular_JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    String popular_api_tag = "popular_api_tag";

    private static String Upcoming_JSON_URL ="https://api.themoviedb.org/3/movie/upcoming?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    String upcoming_api_tag = "upcoming_api_tag";

    private static String Now_Playing_JSON_URL ="https://api.themoviedb.org/3/movie/now_playing?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    String now_playing_api_tag = "now_playing_api_tag";

    private static String Top_Rated_JSON_URL ="https://api.themoviedb.org/3/movie/top_rated?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    String top_rated_api_tag = "top_rated_api_tag";



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
        popular_movieList= new ArrayList<>();
        upcoming_movielist = new ArrayList<>();
        now_playing_api_movielist = new ArrayList<>();
        top_rated_movielist = new ArrayList<>();
        all_movieslist = new ArrayList<>();

        // popular recycler view
        popular_recyclerView = view.findViewById(R.id.recyclerview);

        // upcoming recycler view
        upcoming_recyclerview = view.findViewById(R.id.upcoming_recyclerView);

        // now playing recycler view
        now_playing_recyclerview = view.findViewById(R.id.now_playing_recyclerview);

        // top rated recycler view
        top_rated_recyclerview = view.findViewById(R.id.top_rated_recyclerView);

        // api request
        GetData getData_popular = new GetData(popular_JSON_URL,popular_api_tag,popular_recyclerView);
        getData_popular.execute();

        GetData getData_now_playing = new GetData(Now_Playing_JSON_URL,now_playing_api_tag,now_playing_recyclerview);
        getData_now_playing.execute();

        GetData getData_top_rated = new GetData(Top_Rated_JSON_URL,top_rated_api_tag,top_rated_recyclerview);
        getData_top_rated.execute();

        GetData getData_upcoming = new GetData(Upcoming_JSON_URL,upcoming_api_tag,upcoming_recyclerview);
        getData_upcoming.execute();


        // end: movie recycler view

        return view;
    }

    // start: code block for GetData
    public class GetData extends AsyncTask<String,String,String> {
        private String jsonUrl;
        private String api_tag;

        private RecyclerView recyclerView;

        public GetData(String jsonUrl,String api_tag,RecyclerView recyclerView){
            this.recyclerView = recyclerView;
            this.jsonUrl = jsonUrl;
            this.api_tag = api_tag;
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
            if(api_tag.equals("popular_api_tag")) {
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

                        popular_movieList.add(model);
                        all_movieslist.add(model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                putDataIntoRecyclerView(popular_movieList,recyclerView,api_tag);
            } else if (api_tag.equals("upcoming_api_tag")) {
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

                        upcoming_movielist.add(model);
                        all_movieslist.add(model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                putDataIntoRecyclerView(upcoming_movielist,recyclerView,api_tag);

            } else if (api_tag.equals("now_playing_api_tag")) {
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

                        now_playing_api_movielist.add(model);
                        all_movieslist.add(model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                putDataIntoRecyclerView(now_playing_api_movielist,recyclerView,api_tag);

            } else if (api_tag.equals("top_rated_api_tag")) {
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

                        top_rated_movielist.add(model);
                        all_movieslist.add(model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                putDataIntoRecyclerView(top_rated_movielist,recyclerView,api_tag);

            }

        }
    }
    // end: converting json into object

    // start: recyclerview code block
    private void putDataIntoRecyclerView(List<MovieModelClass> movieList, RecyclerView recyclerView,String recyclerviewIdentifier){
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(getContext(),movieList,this,recyclerviewIdentifier);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(myLayoutManager);
    }

    // start: when clicking on a recyclerview item
    @Override
    public void onItemClick(int position,String recyclerViewIdentifier) {
        Bundle bundle = new Bundle();
        if(recyclerViewIdentifier.equals("popular_api_tag")){
            bundle.putString("Movie_Id",popular_movieList.get(position).getId());
        } else if (recyclerViewIdentifier.equals("upcoming_api_tag")) {
            bundle.putString("Movie_Id",upcoming_movielist.get(position).getId());
        } else if (recyclerViewIdentifier.equals("now_playing_api_tag")) {
            bundle.putString("Movie_Id",now_playing_api_movielist.get(position).getId());
        } else if (recyclerViewIdentifier.equals("top_rated_api_tag")) {
            bundle.putString("Movie_Id",top_rated_movielist.get(position).getId());
        }

        Movie_details_fragment movie_details_fragment = new Movie_details_fragment();
        movie_details_fragment.setArguments(bundle);
        // fragment transaction
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,movie_details_fragment).commit();
    }
    // end: when clicking on a recyclerview item

    // end: recyclerview code block

}