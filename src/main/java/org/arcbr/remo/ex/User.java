package org.arcbr.remo.ex;

import org.arcbr.remo.model.RemoModel;

import java.io.Serializable;
import java.util.List;

public class User extends RemoModel {

    private String name;
    private String lastName;
    private List<Integer> list;
    private User user;
    private List<User> users;

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public List<Integer> getList() {
        return list;
    }

    public User setList(List<Integer> list) {
        this.list = list;
        return this;
    }

    public User getUser() {
        return user;
    }

    public User setUser(User user) {
        this.user = user;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public User setUsers(List<User> users) {
        this.users = users;
        return this;
    }
}
