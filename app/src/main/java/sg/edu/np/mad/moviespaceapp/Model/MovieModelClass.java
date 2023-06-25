package sg.edu.np.mad.moviespaceapp.Model;

public class MovieModelClass {
    String id;
    String movie_name;
    String img;
    String overview;
    

    public MovieModelClass(String id, String movie_name, String img, String overview) {
        this.id = id;
        this.movie_name = movie_name;
        this.img = img;
        this.overview = overview;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public String toString() {
        return "MovieModelClass{" +
                "id='" + id + '\'' +
                ", movie_name='" + movie_name + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
