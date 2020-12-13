package models;

import java.util.ArrayList;
import java.util.HashMap;

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
        for(Enum f : this.field.keySet()){
            if(this.field.get(f) != null){
                returns.add("\""+f.toString().toLowerCase()+"\": \""+this.field.get(f)+"\"");
            }
        }
        return "{\n"+String.join(",", returns)+"\n}";
    }

}
