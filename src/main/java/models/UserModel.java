package models;

import enums.*;

public class UserModel extends AbstractModel {

    /** New UserModel with starting values:
     *
     * @param id       user_id
     * @param email    varchar(10)
     * @param type     enum('ADMIN', 'ATHLETE', 'COACH')
     * @param password varchar(250)
     */
    public UserModel(int id, String email, String type, String password){
        super();

        field.put(User.ID, id);
        field.put(User.EMAIL, email);
        field.put(User.TYPE, type);
        field.put(User.PASSWORD, password);
    }

    public UserModel() { }

    public void setUserAthlete(Integer id){
        field.put(User.ATHLETEID, id);
    }
}
