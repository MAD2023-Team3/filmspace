package sg.edu.np.mad.moviespaceapp.Model;

public class LeaderboardModelClass {
    String actor;

    String profile_path;
    int fame;
    public LeaderboardModelClass() {

    }

    public LeaderboardModelClass(String actor,String profile_path, int fame) {
        this.actor = actor;
        this.profile_path = profile_path;
        this.fame = fame;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return "LeaderboardModelClass{" +
                "actor='" + actor + '\'' +
                ", profile_path='" + profile_path + '\'' +
                ", fame=" + fame +
                '}';
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    public String getActor() {
        return actor;
    }

    public int getFame() {
        return fame;
    }

}
