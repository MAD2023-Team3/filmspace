package sg.edu.np.mad.moviespaceapp.Model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

public class FindFriendViewModel {
    String userId;
    Uri user_profile_picture;
    String username;

    String fcmToken;
    public FindFriendViewModel() {

    }
    public FindFriendViewModel(String userId, Uri user_profile_picture, String username) {
        this.userId = userId;
        this.user_profile_picture = user_profile_picture;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Uri getUser_profile_picture() {
        return user_profile_picture;
    }

    public void setUser_profile_picture(Uri user_profile_picture) {
        this.user_profile_picture = user_profile_picture;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
