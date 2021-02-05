package com.example.myapplication.element;

import java.util.Comparator;

public class Roles implements Comparator<Roles> {

    public String projectId;
    public String projectName;
    public String roles;
    public String username;
    int result;

    public Roles() {

    }

    public Roles(String projectId, String projectName, String roles) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.roles = roles;
    }

    public Roles(String username, String roles) {
        this.username = username;
        this.roles = roles;
    }

    @Override
    public int compare(Roles roles1, Roles roles2) {
        result = 1;
        if (roles1.roles.equals("manager"))
            result = -1;
        else if (roles2.roles.equals("manager"))
            result = 1;
        else if (roles1.roles.equals(roles2.roles))
            result = roles1.username.compareToIgnoreCase(roles2.username);
        else if (roles1.roles.equals("developer") && roles2.roles.equals("client"))
            result = -1;
        else if (roles1.roles.equals("client") && roles2.roles.equals("developer"))
            result = 1;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s : %s", username, roles);
    }
}




