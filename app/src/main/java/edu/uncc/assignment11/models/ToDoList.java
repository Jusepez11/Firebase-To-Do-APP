package edu.uncc.assignment11.models;

import java.io.Serializable;

public class ToDoList implements Serializable {
    String name;
    String user_id;
    String doc_id;

    public ToDoList() {
    }

    public ToDoList(String name, String user_id, String doc_id) {
        this.name = name;
        this.user_id = user_id;
        this.doc_id = doc_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

}
