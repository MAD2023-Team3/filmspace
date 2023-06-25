package sg.edu.np.mad.moviespaceapp.Model;

public class PopularActorModelClass {
    String Profile_path;
    String Actor_name;
    String Actor_id;

    @Override
    public String toString() {
        return "PopularActorModelClass{" +
                "Profile_path='" + Profile_path + '\'' +
                ", Actor_name='" + Actor_name + '\'' +
                ", Actor_id='" + Actor_id + '\'' +
                ", Known_for_department='" + Known_for_department + '\'' +
                '}';
    }

    String Known_for_department;

    public PopularActorModelClass() {

    }

    public String getProfile_path() {
        return Profile_path;
    }

    public void setProfile_path(String profile_path) {
        Profile_path = profile_path;
    }

    public String getActor_name() {
        return Actor_name;
    }

    public void setActor_name(String actor_name) {
        Actor_name = actor_name;
    }

    public String getActor_id() {
        return Actor_id;
    }

    public void setActor_id(String actor_id) {
        Actor_id = actor_id;
    }

    public String getKnown_for_department() {
        return Known_for_department;
    }

    public void setKnown_for_department(String known_for_department) {
        Known_for_department = known_for_department;
    }

    public PopularActorModelClass(String profile_path, String actor_name, String actor_id, String known_for_department) {
        Profile_path = profile_path;
        Actor_name = actor_name;
        Actor_id = actor_id;
        Known_for_department = known_for_department;
    }
}
