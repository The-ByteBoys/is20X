package models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

// import enums.*;

public abstract class AbstractModel {

    protected HashMap<Enum, Object> field;

    public AbstractModel(){
        field = new HashMap<>();
    }

    public Object get(Enum getField){
        return this.field.get(getField);
    }
    
    public void set(Enum newEnum, Object newVal){
        this.field.put(newEnum, newVal);
    }

    @Override
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
        for(Enum f : this.field.keySet()){
            if(this.field.get(f) != null){
                returns.add("\""+f.toString().toLowerCase()+"\": \""+this.field.get(f)+"\"");
            }
        }
        return "{\n"+String.join(",", returns)+"\n}";
    }

}
