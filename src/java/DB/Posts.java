/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import java.sql.Date;
import java.sql.Timestamp;


/**
 *
 * @author djinask
 */
public class Posts {
    int id_post;
    int id_gruppo;
    int id_creatore;
    String creatore;
    String testo;
    //Date data_post;
    Timestamp data_post;

    public Timestamp getData() {
        return data_post;
    }

    public void setData(Timestamp data) {
        this.data_post = data;
    }
    

    public String getCreatore() {
        return creatore;
    }

    public void setCreatore(String creatore) {
        this.creatore = creatore;
    }


   

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getId_gruppo() {
        return id_gruppo;
    }

    public void setId_gruppo(int id_gruppo) {
        this.id_gruppo = id_gruppo;
    }

    public int getId_creatore() {
        return id_creatore;
    }

    public void setId_creatore(int id_creatore) {
        this.id_creatore = id_creatore;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

  
    
    
}
