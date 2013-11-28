/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import java.io.Serializable;

/**
 *
 * @author djinask
 */
public class Groups implements Serializable {
    String GroupName;
    Users admin;
    String Thread;
    int id;

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public Users getAdmin() {
        return admin;
    }

    public void setAdmin(Users admin) {
        this.admin = admin;
    }

    public String getThread() {
        return Thread;
    }

    public void setThread(String Thread) {
        this.Thread = Thread;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
