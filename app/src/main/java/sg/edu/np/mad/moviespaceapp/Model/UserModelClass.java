package sg.edu.np.mad.moviespaceapp.Model;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class UserModelClass {
    String username;

    String userId;

    String email;

    List<String> Friend_Request;

    List<String> watchlist_array;


    List<String> Friends_list;


    int fame;

    public UserModelClass(){

    }

    public UserModelClass(String username, String userId, String email, List<String> friend_Request, List<String> watchlist_array, List<String> friends_list, int fame) {
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.Friend_Request = friend_Request;
        this.watchlist_array = watchlist_array;
        this.Friends_list = friends_list;
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

    public List<String> getFriend_Request() {
        return Friend_Request;
    }

    public void setFriend_Request(List<String> friend_Request) {
        Friend_Request = friend_Request;
    }

    public List<String> getWatchlist_array() {
        return watchlist_array;
    }

    public void setWatchlist_array(List<String> watchlist_array) {
        this.watchlist_array = watchlist_array;
    }

    public List<String> getFriends_list() {
        return Friends_list;
    }

    public void setFriends_list(List<String> friends_list) {
        Friends_list = friends_list;
    }

    public int getFame() {
        return fame;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }
}
