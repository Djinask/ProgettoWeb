/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author djinask
 */
 public class Invites {
  String NomeGruppo;
  int id_invito;
  int id_to;
  String from;

    public String getNomeGruppo() {
        return NomeGruppo;
    }

    public void setNomeGruppo(String NomeGruppo) {
        this.NomeGruppo = NomeGruppo;
    }

    public int getId_invito() {
        return id_invito;
    }

    public void setId_invito(int id_invito) {
        this.id_invito = id_invito;
    }

    public int getId_to() {
        return id_to;
    }

    public void setId_to(int id_to) {
        this.id_to = id_to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Timestamp getData_invito() {
        return data_invito;
    }

    public void setData_invito(Timestamp data_invito) {
        this.data_invito = data_invito;
    }

    public Boolean isVisualizzato() {
        return visualizzato;
    }

    public void setVisualizzato(Boolean visualizzato) {
        this.visualizzato = visualizzato;
    }

    public Boolean isAccettato() {
        return accettato;
    }

    public void setAccettato(Boolean accettato) {
        this.accettato = accettato;
    }
  Timestamp data_invito;
  Boolean visualizzato;
    Boolean accettato;


    
    
    
}
