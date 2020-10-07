package models;

import enums.*;

public class ClubModel extends AbstractModel {

    /**
     * 
     * @param id
     * @param name
     */
    public ClubModel(int id, String name){
        super();

        field.put(Club.ID, id);
        field.put(Club.NAME, name);
    }

    /**
     * Empty constructor to manually add all fields
     */
    // public ClubModel(){ }

    public void setOwner(String newOwner){
        field.put(Club.OWNER, newOwner);
    }
}
