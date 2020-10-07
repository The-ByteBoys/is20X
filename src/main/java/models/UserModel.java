package models;

import enums.*;

public class UserModel extends AbstractModel {
    public UserModel(String fname, String lname, String email, String password){
        super();

        // field.put(User.ID, id);
        field.put(User.FNAME, fname);
        field.put(User.LNAME, lname);
        // field.put(User.ATHLETEID, );
        field.put(User.EMAIL, email);
        field.put(User.PASSWORD, password);
    }

    public void setUserAthlete(Integer id){
        field.put(User.ATHLETEID, id);
    }
}

/*public class UserModel {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    public UserModel(String firstName, String lastName, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }

    public UserModel(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "{\n  \"firstname\": \""+this.firstName+"\",\n  \"lastname\": \""+this.lastName+"\",\n  \"username\": \""+this.userName+"\"\n}";
    }

}
*/