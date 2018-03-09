package com.mark.foodorderapp.Model;

/**
 * Created by Mark on 23/02/2018.
 */

public class User {
    private String name;
    private String password;
    private String phone;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String setname(String Pname) {
        name = Pname;
        return name;
    }

    public String getName() {

        return name;
    }

    public String getPassword() {

        return password;
    }
}