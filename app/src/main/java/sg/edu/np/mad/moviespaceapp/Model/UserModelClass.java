package sg.edu.np.mad.moviespaceapp.Model;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class UserModelClass {
    String username;

    String userId;

    String email;

    ArrayList<String> Friend_Request;

    ArrayList<String> watchlist_array;

    int fame;

    public UserModelClass(){

    }

    public UserModelClass(String username, String userId, String email, ArrayList<String> friend_Request, ArrayList<String> watchlist_array, int fame) {
        this.username = username;
        this.userId = userId;
        this.email = email;
        Friend_Request = friend_Request;
        this.watchlist_array = watchlist_array;
        this.fame = fame;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFriend_Request() {
        return Friend_Request;
    }

    public void setFriend_Request(ArrayList<String> friend_Request) {
        Friend_Request = friend_Request;
    }

    public ArrayList<String> getWatchlist_array() {
        return watchlist_array;
    }

    public void setWatchlist_array(ArrayList<String> watchlist_array) {
        this.watchlist_array = watchlist_array;
    }

    public int getFame() {
        return fame;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }
}
