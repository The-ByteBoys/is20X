package models;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class AbstractModel {
    
    public String toString(){
        ArrayList<String> returns = new ArrayList<>();
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
        return "{\n"+String.join(",", returns)+"\n}";
    }

}
