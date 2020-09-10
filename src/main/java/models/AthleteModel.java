package models;

public class AthleteModel {
    private Integer id;
    private String name;
    private Integer birth;
    private Integer club;

    public AthleteModel(Integer id, String name, Integer birth, Integer club) {
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

    @Override
    public String toString(){
        return "{\n  \"id\": \""+this.id+"\",\n  \"name\": \""+this.name+"\",\n  \"birth\": \""+this.birth+"\",\n  \"club\": \""+this.club+"\"\n}";
    }

}
