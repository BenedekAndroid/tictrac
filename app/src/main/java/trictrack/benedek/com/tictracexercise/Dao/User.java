package trictrack.benedek.com.tictracexercise.Dao;

/**
 * Created by Benedek on 2016.08.12..
 */
public class User {

    private String id;
    private String email;
    private String name;
    private String infos;

    public User(String email, String name, String infos) {
        this.email = email;
        this.name = name;
        this.infos = infos;
    }

    public User(String id, String email, String name, String infos) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.infos = infos;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getInfos() {
        return infos;
    }

    public String getId() {
        return id;
    }
}
