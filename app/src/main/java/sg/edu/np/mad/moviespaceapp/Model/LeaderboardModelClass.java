package sg.edu.np.mad.moviespaceapp.Model;

public class LeaderboardModelClass {
    String actor_name;
    Integer actor_placement;
    String actor_id;
    String profile_path;
    int fame;
    public LeaderboardModelClass() {

    }

    // constructor
    public LeaderboardModelClass(String actor_id,String profile_path, int fame,Integer actor_placement,String actor_name) {
        this.actor_id = actor_id;
        this.profile_path = profile_path;
        this.fame = fame;
        this.actor_placement = actor_placement;
        this.actor_name = actor_name;
    }


    @Override
    public String toString() {
        return "LeaderboardModelClass{" +
                "actor_name='" + actor_name + '\'' +
                ", actor_placement=" + actor_placement +
                ", actor_id='" + actor_id + '\'' +
                ", profile_path='" + profile_path + '\'' +
                ", fame=" + fame +
                '}';
    }

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }

    public Integer getActor_placement() {
        return actor_placement;
    }

    public void setActor_placement(Integer actor_placement) {
        this.actor_placement = actor_placement;
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

    public String getActor_id() {
        return actor_id;
    }

    public void setActor_id(String actor_id) {
        this.actor_id = actor_id;
    }

    public int getFame() {
        return fame;
    }

}
