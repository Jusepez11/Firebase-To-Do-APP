package edu.uncc.assignment11.models;

import java.util.HashMap;

public class ToDoListItem {
    String name;
    String priority;
    String user_id;
    String list_id;
    String doc_id;

    public ToDoListItem() {
    }

    public ToDoListItem(String name, String priority, String user_id, String doc_id, String list_id) {
        this.name = name;
        this.priority = priority;
        this.user_id = user_id;
        this.doc_id = doc_id;
        this.list_id = list_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
    public String getList_id() {
        return list_id;
    }
    public void setList_id(String list_id) {
        this.list_id = list_id;
    }
}
