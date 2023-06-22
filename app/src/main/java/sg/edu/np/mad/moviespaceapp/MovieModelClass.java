package sg.edu.np.mad.moviespaceapp;

public class MovieModelClass {
    String id;
    String movie_name;
    String img;

    public MovieModelClass(String id, String movie_name, String img) {
        this.id = id;
        this.movie_name = movie_name;
        this.img = img;
    }

    public MovieModelClass() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
