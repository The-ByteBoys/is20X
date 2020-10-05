package models;

import enums.*;
import java.util.HashMap;

public class ClubModel extends AbstractModel {
    protected HashMap<Club, Object> field;

    public ClubModel(Integer id, String name){
        super();

        field.put(Club.ID, id);
        field.put(Club.NAME, name);
    }

    public void setOwner(String newOwner){
        field.put(Club.OWNER, newOwner);
    }
}
