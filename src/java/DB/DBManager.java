/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

public class DBManager implements Serializable {

    private transient Connection conn;

    public DBManager(String dburl) throws SQLException {

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.conn = DriverManager.getConnection(dburl);
    }

    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).info(ex.getMessage());
        }
    }

    /**
     *
     * Autentica un utente in base a un nome utente e a una password
     *
     *
     *
     * @param username il nome utente
     *
     * @param password la password
     *
     * @return null se l'utente non è autenticato, un oggetto User se * l'utente
     * esiste ed è autenticato
     *
     */
    public Users authenticate(String mail, String password) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT * FROM USERS WHERE MAIL = ? AND PASSWORD = ?");
        try {
            stm.setString(1, mail);
            stm.setString(2, password);

            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    Users user = new Users();
                    user.setmail(mail);
                    user.setpassword(rs.getString("PASSWORD"));
                    user.setName(rs.getString("NAME"));
                    user.setId(rs.getInt("ID"));

                    return user;
                } else {
                    return null;
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
    }

    public Boolean AddGroup(Groups group) throws SQLException, ServletException {

        PreparedStatement stm = conn.prepareStatement("INSERT INTO GRUPPI(NAME,ADMIN) VALUES(?,?)");

        try {

            stm.setString(1, group.GroupName);
            stm.setInt(2, group.getAdmin().getId());

            try {
                stm.executeUpdate();

                return true;
            } catch (SQLException ex) {
                throw new ServletException(ex);

            }
        } finally {
            stm.close();
        }

    } //To change body of generated methods, choose Tools | Templates.

    public List<Groups> getGroups(Users utente) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT * FROM GRUPPI WHERE ADMIN = ?");
        try {
            stm.setInt(1, utente.getId());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                List<Groups> gruppi = new ArrayList();
                do {
                    Groups gruppo = new Groups();
                    gruppo.setId(rs.getInt("ID_GRUPPO"));
                    gruppo.setData_creazione(rs.getDate("DATA_CREAZIONE"));
                    gruppo.setGroupName(rs.getString("NAME"));
                    gruppo.setAdmin(utente);
                    gruppi.add(gruppo);

                } while (rs.next());
                return gruppi;

            } else {
                return null;
            }

        } finally {
            stm.close();
        }

    }

    public Groups getGroup(Integer id_gruppo, Users user) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT * FROM GRUPPI WHERE ID_GRUPPO = ?");
        try {
            stm.setInt(1, id_gruppo);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {

                Groups gruppo = new Groups();

                gruppo.setId(rs.getInt("ID_GRUPPO"));
                gruppo.setData_creazione(rs.getDate("DATA_CREAZIONE"));
                gruppo.setGroupName(rs.getString("NAME"));
                gruppo.setAdmin(user);

                return gruppo;

            } else {
                return null;
            }

        } finally {
            stm.close();
        }

    }

    public Boolean AddPost(String testo, Users user, Groups gruppo) throws SQLException, ServletException {

        PreparedStatement stm = conn.prepareStatement("INSERT INTO POSTS(ID_CREATORE,TESTO,ID_GRUPPO) VALUES(?,?,?)");

        try {

            stm.setInt(1, user.getId());
            stm.setString(2, testo);
            stm.setInt(3, gruppo.getId());

            try {
                stm.executeUpdate();

                return true;
            } catch (SQLException ex) {
                throw new ServletException(ex);

            }
        } finally {
            stm.close();
        }

    }

    public List<Posts> getPosts(Groups gruppo) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT * FROM POSTS JOIN USERS ON POSTS.ID_CREATORE=USERS.ID WHERE ID_GRUPPO=?");
        try {
            stm.setInt(1, gruppo.getId());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                List<Posts> posts = new ArrayList();
                do {
                    Posts post = new Posts();
                    post.setId_post(rs.getInt("ID_POST"));
                    post.setData(rs.getTimestamp("DATA_POST"));
                    post.setId_creatore(rs.getInt("ID_CREATORE"));
                    post.setId_gruppo(rs.getInt("ID_GRUPPO"));
                    post.setTesto(rs.getString("TESTO"));
                    post.setCreatore(rs.getString("NAME"));

                    posts.add(post);

                } while (rs.next());
                return posts;

            } else {
                return null;
            }

        } finally {
            stm.close();
        }

    }

    public List<Users> getUsers(Users richiedente) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT * FROM USERS EXCEPT SELECT * FROM USERS WHERE ID = ? ");
        try {
            stm.setInt(1, richiedente.getId());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                List<Users> utenti = new ArrayList();
                do {
                    Users user = new Users();
                    user.setId(rs.getInt("ID"));
                    user.setName(rs.getString("NAME"));
                    user.setmail(rs.getString("MAIL"));

                    utenti.add(user);

                } while (rs.next());
                return utenti;

            } else {
                return null;
            }

        } finally {
            stm.close();
        }

    }

    public Boolean AddInviti(int id_gruppo, Users user, String[] inviti) throws SQLException, ServletException {
        int i;
        PreparedStatement stm = conn.prepareStatement("INSERT INTO INVITI(ID_GRUPPO,ID_USER) VALUES(?,?)");

        try {
            for (i = 0; i < inviti.length; i++) {

                stm.setInt(1, id_gruppo);
                stm.setInt(2, Integer.parseInt(inviti[i]));

                try {

                    stm.executeUpdate();

                } catch (SQLException ex) {
                    throw new ServletException(ex);

                }
            }
        } finally {
            stm.close();
        }
        return true;

    }

    public List<Invites> getInviti(Users utente) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT GRUPPI.NAME AS \"NOME_GRUPPO\",\n" +
"                       ID_INVITO, " +
"                       ID_USER AS \"TO\"," +
"                       USERS.NAME AS \"FROM\", " +
"                       DATA_INVITO, " +
"                       VISUALIZZATO, " +
"                       ACCETTATO  " +
"                FROM INVITI  JOIN GRUPPI  ON INVITI.ID_GRUPPO= GRUPPI.ID_GRUPPO " +
"                                JOIN USERS ON USERS.ID=GRUPPI.ADMIN WHERE ID_USER =? ");
        try {
            stm.setInt(1, utente.getId());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                List<Invites> inviti = new ArrayList();
                do {
                    Invites invito = new Invites();
                    invito.setNomeGruppo(rs.getString("NOME_GRUPPO"));
                    invito.setAccettato(rs.getBoolean("ACCETTATO"));
                   invito.setData_invito(rs.getTimestamp("DATA_INVITO"));
                   invito.setFrom(rs.getString("FROM"));
                   invito.setId_invito(rs.getInt("ID_INVITO"));
                   invito.setId_to(rs.getInt("TO"));
                   invito.setVisualizzato(rs.getBoolean("VISUALIZZATO"));

                    inviti.add(invito);

                } while (rs.next());
                return inviti;

            } else {
                return null;
            }

        } finally {
            stm.close();
        }
    }
}
