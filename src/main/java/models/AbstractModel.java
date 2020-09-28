package models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import enums.*;

public abstract class AbstractModel {

    protected HashMap<Athlete, Object> field;

    public AbstractModel(){
        field = new HashMap<>();
    }

    public Object get(Athlete getField){
        return this.field.get(getField);
    }

    public String toString(){
        ArrayList<String> returns = new ArrayList<>();
        /** OLD METHOD * /
        for (Field f : this.getClass().getDeclaredFields()) {
            String val = "";

            try {
                val = f.get(this).toString();
            } catch (Exception e) {
                System.out.println("Error doing toString on an object: "+e);
                continue;
            }

            returns.add("\""+f.getName()+"\": \""+val+"\"");
        }
        */
        for(Athlete f : this.field.keySet()){
            if(!this.field.get(f).equals("")){
                returns.add("\""+f.toString().toLowerCase()+"\": \""+this.field.get(f)+"\"");
            }
        }
        return "{\n"+String.join(",", returns)+"\n}";
    }

}
