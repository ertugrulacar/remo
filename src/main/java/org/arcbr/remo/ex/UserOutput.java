package org.arcbr.remo.ex;

import org.arcbr.remo.model.RemoModel;

import java.util.List;

public class UserOutput extends RemoModel {


    private String user_name;
    private String user_last_name;
    private List<Integer> user_list;

    public String getUser_name() {
        return user_name;
    }

    public UserOutput setUser_name(String user_name) {
        this.user_name = user_name;
        return this;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public UserOutput setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
        return this;
    }

    public List<Integer> getUser_list() {
        return user_list;
    }

    public UserOutput setUser_list(List<Integer> user_list) {
        this.user_list = user_list;
        return this;
    }
}
