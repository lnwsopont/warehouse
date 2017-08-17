package com.muicsenior.warehouse.libraries;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by ta on 31/3/2015.
 */
public class JSON {

    public static final JSON EMPTY = new JSON("{}");
    protected Object o;
    protected Gson gson = new Gson();
    protected final static String TMP_KEY_NAME = "key";
    public static JSONRegister register = JSONRegister.getInstance();

    public JSON(){
        set("{}");
    }

    public JSON(String src){
        set(src != null ? src : "{}");
    }

    public JSON(Object src){
        //if(true || o instanceof JSONObject || o instanceof JSONArray) {
        if( src == null ){
            set("{}");
        }
        else {
            o = src;
        }
    }

    public JSON set(String src){
        try {
            o = new JSONObject(src);
        } catch(JSONException e) {
            try {
                o = new JSONArray(src);
            } catch(JSONException e2) {
                o = src;
            }
            //o = src;

                /*try {
                    o = new JSONObject("{\"" + TMP_KEY_NAME + "\":" + src + "}");
                } catch(JSONException e1) {
                    o = new JSONObject();
                }*/
        }
        return this;
    }

    public JSON set(String key, Object val){
        try {
            ((JSONObject) o).put(key, val);
        } catch(JSONException e) {
            //e.printStackTrace();
        }
        return this;
    }

    public JSON remove(String key){
        ((JSONObject) o).remove(key);
        return this;
    }

    public JSON merge(JSON other){
        //TODO
        if((o instanceof JSONObject && other.o instanceof JSONObject) || (o instanceof JSONArray && other.o instanceof JSONArray)) {

        }
        return this;
    }

    public JSON regis(String key){
        register.put(key, o.toString());
        return this;
    }

    public JSON get(String name){
        try {
            try {
                return new JSON(((JSONObject) o).get(name));
            } catch(JSONException e) {
                //e.printStackTrace();
                return new JSON(((JSONObject) o).getJSONArray(name));
            }
        } catch(Exception e) {
            //e.printStackTrace();
            return new JSON(o);
        }
    }

    public boolean checkIsNull(String name){
        try{
            Object j = ((JSONObject) o).get(name);
            return JSONObject.NULL.equals(j);
        }
        catch(Exception e){
            return true;
        }
    }

    public <E> E get(String name, Class<E> type, E defaultValue){
        try {
            E e = defaultValue;
            try {
                Object j = ((JSONObject) o).get(name);
                //Log.i("aaa", "JSON j--->" + j.toString() + "-" + JSONObject.NULL.equals(j));

                if(JSONObject.NULL.equals(j)){
                    return e;
                }

                e = gson.fromJson(j.toString(), type);

                if(e == null){
                    throw new JsonSyntaxException("dummy Exception");
                }
                //Log.i("bbb", "--> " + name + "[" + e + "]");
            } catch(JsonSyntaxException j) {
                e = (E) ((JSONObject) o).get(name);
                //Log.i("bbb", "--> " + name + "[" + e + "]*");
            }
            return e;

        } catch(Exception e) {
            //e.printStackTrace();
            return defaultValue;
        }
    }

    public <E> E get(String name, Class<E> type){
        return get(name, type, null);
    }

    public <E> E getArr(String name, Class<E> type){
        try {
            return gson.fromJson(((JSONObject) o).get(name).toString(), type);
        } catch(Exception e) {
            return null;
        }
    }

    public JSON first(){
        return get(0);
    }

    public JSON last(){
        return length() == 0 ? null : get(length()-1);
    }

    public boolean has(String key){
        try {
            return ((JSONObject) o).has(key);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Iterator<String> keys(){
        try {
            return ((JSONObject) o).keys();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isEmpty() {
        try {
            if (o instanceof JSONObject) {
                return ((JSONObject) o).names().length() == 0;
            }
            else return length() == 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }


    public JSON get(int index){
        try {
            if(!(o instanceof JSONArray)) {
                //o = new JSONObject("{\"theKey\":" + o.toString() + "}").getJSONArray("theKey");
                o = ((JSONObject) o).getJSONArray(TMP_KEY_NAME);
            }
            return new JSON(((JSONArray) o).get(index).toString());
        } catch(Exception e) {
            return new JSON(o);
        }
    }

    public <E> E get(int index, Class<E> type){
        try {

            if(!(o instanceof JSONArray)) {
                o = ((JSONObject) o).getJSONArray(TMP_KEY_NAME);
            }

            if(false && gson == null) {
                return (E) ((JSONArray) o).get(index);
            }
            else {
                return gson.fromJson(((JSONArray) o).get(0).toString(), type);
            }

        } catch(Exception e) {
            return null;
        }
    }

    public int length(){
        if(o instanceof JSONArray) {
            return ((JSONArray) o).length();
        }
        return 0;
    }

    @Override
    public String toString(){
        try {
            return o.toString();
        }
        catch(Exception e){
            //e.printStackTrace();
           return "";
        }
    }
}

