package sg.edu.np.mad.moviespaceapp;

public class ActorModelClass {
    String Id;
    String Known_for_department;
    String actor_name;
    String actor_profile_path;
    String playing_character;

    String Overview;
    public ActorModelClass() {

    }

    public ActorModelClass(String id, String known_for_department, String actor_name, String actor_profile_path, String playing_character, String overview) {
        Id = id;
        Known_for_department = known_for_department;
        this.actor_name = actor_name;
        this.actor_profile_path = actor_profile_path;
        this.playing_character = playing_character;
        Overview = overview;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getKnown_for_department() {
        return Known_for_department;
    }

    public void setKnown_for_department(String known_for_department) {
        Known_for_department = known_for_department;
    }

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }

    public String getActor_profile_path() {
        return actor_profile_path;
    }

    public void setActor_profile_path(String actor_profile_path) {
        this.actor_profile_path = actor_profile_path;
    }

    public String getPlaying_character() {
        return playing_character;
    }

    public void setPlaying_character(String playing_character) {
        this.playing_character = playing_character;
    }

    @Override
    public String toString() {
        return "ActorModelClass{" +
                "Id='" + Id + '\'' +
                ", Known_for_department='" + Known_for_department + '\'' +
                ", actor_name='" + actor_name + '\'' +
                ", actor_profile_path='" + actor_profile_path + '\'' +
                ", playing_character='" + playing_character + '\'' +
                '}';
    }
}
