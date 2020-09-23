package models;

public class AthleteModel extends AbstractModel {
    protected Integer id;
    protected String name;
    protected Integer birth;
    protected String club;

    public AthleteModel(Integer id, String name, Integer birth, String club) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.club = club;
    }

    public AthleteModel(){
        // Allow creation of empty model.
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }
}
