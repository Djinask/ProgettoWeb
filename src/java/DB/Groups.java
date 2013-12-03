/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author djinask
 */
public class Groups implements Serializable {
    String GroupName;
    String owner;
    String Thread;
    int id;
    Date data;
    int admin;

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }
    

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String admin) {
        this.owner = admin;
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
