/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
            DriverManager.getConnection("jdbc:derby;shutdown=true");
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
                    user.setAvatar_path(rs.getString("AVATAR_PATH"));

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

    public Users getUser(int user_id) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT * FROM USERS WHERE ID = ?");

        try {
            stm.setInt(1, user_id);

            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    Users user = new Users();
                    user.setmail(rs.getString("MAIL"));
                    user.setpassword(rs.getString("PASSWORD"));
                    user.setName(rs.getString("NAME"));
                    user.setId(rs.getInt("ID"));

                    user.setAvatar_path(rs.getString("AVATAR_PATH"));

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

    public Boolean AddGroup(Groups group, Users utente) throws SQLException, ServletException {

        PreparedStatement stm = conn.prepareStatement("INSERT INTO GRUPPI(NAME,ADMIN, THREAD ) VALUES(?,?,?)");

        try {

            stm.setString(1, group.GroupName);
            stm.setInt(2, utente.getId());
            stm.setString(3, group.getThread());

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

        PreparedStatement stm = conn.prepareStatement("SELECT "
                + "GRUPPI.ID_GRUPPO, "
                + "PARTEC.\"DATA\" AS \"DATA\", "
                + "GRUPPI.NAME AS \"NOME_GRUPPO\","
                + "GRUPPI.THREAD ,"
                + "USERS.NAME AS \"OWNER\"  "
                + "FROM GRUPPI JOIN PARTEC \n"
                + "ON GRUPPI.ID_GRUPPO=PARTEC.ID_GRUPPO \n"
                + "JOIN USERS \n"
                + "ON GRUPPI.ADMIN=USERS.ID\n"
                + "WHERE PARTEC.ID_USER=? AND PARTEC.ACTIVE=true \n"
                + "UNION\n"
                + "SELECT "
                + "GRUPPI.ID_GRUPPO, "
                + "GRUPPI.DATA_CREAZIONE AS \"DATA\", "
                + "GRUPPI.NAME AS \"NOME_GRUPPO\", "
                + "GRUPPI.THREAD ,"
                + "USERS.NAME AS \"OWNER\" "
                + "FROM GRUPPI\n"
                + "JOIN USERS \n"
                + "ON GRUPPI.ADMIN=USERS.ID\n"
                + "WHERE ADMIN=?");
        try {
            stm.setInt(1, utente.getId());
            stm.setInt(2, utente.getId());

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                List<Groups> gruppi = new ArrayList();
                do {
                    Groups gruppo = new Groups();
                    gruppo.setId(rs.getInt("ID_GRUPPO"));
                    gruppo.setData(rs.getTimestamp("DATA"));
                    gruppo.setGroupName(rs.getString("NOME_GRUPPO"));
                    gruppo.setOwner(rs.getString("OWNER"));
                    gruppo.setThread(rs.getString("THREAD"));
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

        PreparedStatement stm = conn.prepareStatement("SELECT ID_GRUPPO, ADMIN, THREAD,  GRUPPI.DATA_CREAZIONE, GRUPPI.NAME AS \"NOME_GRUPPO\", USERS.NAME AS \"NOME_OWNER\" FROM GRUPPI JOIN USERS \n"
                + "ON GRUPPI.ADMIN=USERS.ID WHERE ID_GRUPPO = ?");
        try {
            stm.setInt(1, id_gruppo);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {

                Groups gruppo = new Groups();

                gruppo.setId(rs.getInt("ID_GRUPPO"));
                gruppo.setData(rs.getTimestamp("DATA_CREAZIONE"));
                gruppo.setGroupName(rs.getString("NOME_GRUPPO"));
                gruppo.setOwner(rs.getString("NOME_OWNER"));
                gruppo.setAdmin(rs.getInt("ADMIN"));
                gruppo.setThread(rs.getString("THREAD"));
                rs.close();

                return gruppo;

            } else {
                return null;
            }

        } finally {
            stm.close();
        }

    }

    public Boolean AddPost(Posts post, List<MyFiles> files) throws SQLException, ServletException {
        String sql_post = "INSERT INTO POSTS(ID_CREATORE,TESTO,ID_GRUPPO, ALLEGATO) VALUES(?,?,?,?)";
        String sql_file = "INSERT INTO FILES(ORIGINAL_FILE_NAME,NEW_FILE_NAME,TYPE,PATH,POST_REFERENCE,LENGTH) VALUES (?,?,?,?,?,?)";

        PreparedStatement stm1 = conn.prepareStatement(sql_post, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement stm2 = conn.prepareStatement(sql_file);
        int id_post = 0;

        try {

            stm1.setInt(1, post.getId_creatore());
            stm1.setString(2, post.getTesto());
            stm1.setInt(3, post.getId_gruppo());
            stm1.setBoolean(4, post.isAllegato());

            try {
                stm1.executeUpdate();
                ResultSet genKey = stm1.getGeneratedKeys();
                if (genKey.next()) {
                    id_post = genKey.getInt(1);
                }
                if (post.isAllegato()) {
                    for (int i = 0; i < files.size(); i++) {
                        stm2.setString(1, files.get(i).getOrigin_name());

                        stm2.setString(2, files.get(i).getName());
                        stm2.setString(3, files.get(i).getType());
                        stm2.setString(4, files.get(i).getPath());
                        stm2.setInt(5, id_post);
                        stm2.setLong(6, files.get(i).getLength());
                        try {
                            stm2.executeUpdate();
                        } catch (SQLException ex) {
                            throw new ServletException(ex);
                        }
                    }

                }
            } catch (SQLException ex) {
                throw new ServletException(ex);

            }
            return true;
            //inserisco file
        } catch (SQLException ex) {
            throw new ServletException(ex);

        } finally {
            stm1.close();
            stm2.close();
        }

    }

    public List<Posts> getPosts(Groups gruppo) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT * FROM POSTS JOIN USERS ON POSTS.ID_CREATORE=USERS.ID WHERE ID_GRUPPO=? ORDER BY DATA_POST DESC");
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
                    post.setAllegato(rs.getBoolean("ALLEGATO"));
                    post.setUser_avatar_path(rs.getString("AVATAR_PATH"));

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

    public List<Users> getUsers(Users richiedente, int id_gruppo) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("SELECT ID, MAIL, NAME FROM USERS\n"
                + "EXCEPT\n"
                + "(SELECT ID, MAIL, NAME FROM USERS WHERE ID = ? \n"
                + "UNION \n"
                + "SELECT ID, MAIL, NAME FROM USERS JOIN INVITI \n"
                + "ON USERS.ID=INVITI.ID_USER \n"
                + "WHERE ID_GRUPPO=? AND INVITI.NASCOSTO=false)");
        try {
            stm.setInt(1, richiedente.getId());
            stm.setInt(2, id_gruppo);

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

    public List<Users> getUsersGroup(Users richiedente, int id_gruppo, Boolean mode) throws SQLException {

        //mode= true shows users available
        //mode= false shows users deletable
        String sql1 = "SELECT USERS.ID as ID_USER, MAIL,AVATAR_PATH, NAME FROM USERS \n"
                + "JOIN PARTEC\n"
                + "ON USERS.ID=PARTEC.ID_USER\n"
                + "WHERE ID_GRUPPO=? AND PARTEC.ACTIVE=true";

        String sql2 = "SELECT USERS.ID as ID_USER, MAIL,AVATAR_PATH, NAME FROM USERS \n"
                + "EXCEPT\n"
                + "(SELECT USERS.ID as ID_USER, MAIL, AVATAR_PATH,NAME FROM USERS WHERE ID = ?)\n"
                + "UNION\n"
                + "SELECT USERS.ID as ID_USER, MAIL, AVATAR_PATH,NAME FROM USERS \n"
                + "JOIN PARTEC \n"
                + "ON USERS.ID=PARTEC.ID_USER \n"
                + "WHERE ID_GRUPPO=? AND PARTEC.ACTIVE=false";

        PreparedStatement stm1 = conn.prepareStatement(sql1);
        PreparedStatement stm2 = conn.prepareStatement(sql2);
        try {
            ResultSet rs;
            if (!mode) {
                stm1.setInt(1, id_gruppo);

                rs = stm1.executeQuery();
            } else {

                stm2.setInt(1, richiedente.getId());
                stm2.setInt(2, id_gruppo);

                rs = stm2.executeQuery();
            }

            if (rs.next()) {
                List<Users> utenti = new ArrayList();
                do {
                    Users user = new Users();
                    user.setId(rs.getInt("ID_USER"));
                    user.setName(rs.getString("NAME"));
                    user.setmail(rs.getString("MAIL"));
                     user.setAvatar_path(rs.getString("AVATAR_PATH"));
                    utenti.add(user);

                } while (rs.next());
                return utenti;

            } else {
                return null;
            }

        } finally {
            stm1.close();
            stm2.close();
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

    public List<Invites> getInviti(Users utente) throws SQLException, ServletException {

        List<Invites> inviti = new ArrayList();
        PreparedStatement stm = conn.prepareStatement("SELECT "
                + "GRUPPI.NAME AS \"NOME_GRUPPO\","
                + "ID_INVITO,"
                + " ID_USER AS \"TO\","
                + "USERS.NAME AS \"FROM\","
                + "DATA_INVITO,"
                + "VISUALIZZATO,"
                + "ACCETTATO "
                + "FROM INVITI  JOIN GRUPPI  "
                + "ON INVITI.ID_GRUPPO= GRUPPI.ID_GRUPPO "
                + "JOIN USERS "
                + "ON USERS.ID=GRUPPI.ADMIN "
                + "WHERE ID_USER = ? AND NASCOSTO=false AND ACCETTATO =false");

        try {
            stm.setInt(1, utente.getId());

            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {

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
                    return inviti;
                }
            } finally {
                rs.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        } finally {
            stm.close();
        }
        return inviti;
    }

    public int getInvitiNumber(Users utente) throws SQLException, ServletException {

        int notifiche = 0;
        PreparedStatement stm = conn.prepareStatement("SELECT COUNT(*) AS \"CONTA\""
                + "FROM INVITI  JOIN GRUPPI  "
                + "ON INVITI.ID_GRUPPO= GRUPPI.ID_GRUPPO "
                + "JOIN USERS "
                + "ON USERS.ID=GRUPPI.ADMIN "
                + "WHERE ID_USER = ? AND NASCOSTO=false AND ACCETTATO =false");

        try {
            stm.setInt(1, utente.getId());

            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    notifiche = rs.getInt("CONTA");

                } else {
                    return 0;
                }
            } finally {
                rs.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        } finally {
            stm.close();
        }
        return notifiche;
    }

    public Boolean RemoveInvites(int id_invito) throws SQLException, ServletException {
        PreparedStatement stm = conn.prepareStatement("UPDATE INVITI SET NASCOSTO=TRUE WHERE ID_INVITO=?");
        Boolean fatto = false;

        try {

            stm.setInt(1, id_invito);

            try {
                stm.executeUpdate();

                fatto = true;
            } catch (SQLException ex) {
                throw new ServletException(ex);

            }
        } finally {
            stm.close();
        }
        return fatto;

//To change body of generated methods, choose Tools | Templates.
    }

    public Boolean AddPartecipation(Users utente, int id_invito) throws SQLException, ServletException {
        PreparedStatement stm = conn.prepareStatement(""
                + "INSERT INTO PARTEC(ID_GRUPPO, ID_USER, ID_INVITO) "
                + "VALUES "
                + "((SELECT ID_GRUPPO FROM INVITI WHERE ID_INVITO = ?), ?, ? )"
                + "");
        PreparedStatement stm2 = conn.prepareStatement("UPDATE INVITI SET ACCETTATO=TRUE WHERE ID_INVITO=?");

        Boolean fatto = null;

        try {

            stm.setInt(1, id_invito);
            stm2.setInt(1, id_invito);
            stm.setInt(2, utente.getId());
            stm.setInt(3, id_invito);

            try {
                stm.executeUpdate();
                try {
                    stm2.executeUpdate();

                    fatto = true;
                } catch (SQLException ex) {
                    throw new ServletException(ex);

                }

                fatto = true;
            } catch (SQLException ex) {
                throw new ServletException(ex);

            }
        } finally {
            stm.close();
        }

        return fatto;
    }

    public MyFiles getAnnex(int id_post) throws SQLException, ServletException {

        String sql = "SELECT * FROM FILES WHERE POST_REFERENCE = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        try {
            stm.setInt(1, id_post);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {

                MyFiles mio_file = new MyFiles();
                mio_file.setLength(rs.getLong("LENGTH"));
                mio_file.setPath(rs.getString("PATH"));
                mio_file.setName(rs.getString("NEW_FILE_NAME"));
                mio_file.setOrigin_name(rs.getString("ORIGINAL_FILE_NAME"));
                mio_file.setLength(rs.getLong("LENGTH"));

                rs.close();

                return mio_file;

            } else {
                return null;
            }

        } finally {
            stm.close();
        }

    }

    public Boolean updateAvatar(int user_id, String path) throws SQLException, ServletException {
        PreparedStatement stm = conn.prepareStatement("UPDATE USERS SET AVATAR_PATH=? WHERE ID=?");
        Boolean fatto = false;

        try {

            stm.setString(1, path);
            stm.setInt(2, user_id);

            try {
                stm.executeUpdate();

                fatto = true;
            } catch (SQLException ex) {
                throw new ServletException(ex);

            }
        } finally {
            stm.close();
        }
        return fatto;

    }

    public Boolean DeleteUsers(int id_gruppo, Users user, String[] rimossi) throws SQLException, ServletException {
        int i;
        String sql_delete_users = "UPDATE PARTEC SET ACTIVE=FALSE WHERE ID_GRUPPO=? AND ID_USER=?";
         String hide_invites = "UPDATE INVITI SET NASCOSTO=TRUE WHERE ID_INVITO=?";

        String sql_get_id_invite = "SELECT * FROM PARTEC WHERE ID_GRUPPO=? AND ID_USER=? AND ACTIVE=TRUE";

        PreparedStatement stm1 = conn.prepareStatement(sql_delete_users);
        int id_invito = 0;
        int rimosso;
        ResultSet res;
        

        PreparedStatement invites = conn.prepareStatement(sql_get_id_invite);
                PreparedStatement hide = conn.prepareStatement(hide_invites);
        try {
            for (i = 0; i < rimossi.length; i++) {

                rimosso = Integer.parseInt(rimossi[i]);

                invites.setInt(1, (int) id_gruppo);
                invites.setInt(2, (int) rimosso);
              
                res = invites.executeQuery();
                id_invito = 0;
                if (res.next()) {
                    id_invito = res.getInt("ID_INVITO");

                   
                }
                hide.setInt(1, (int) id_invito);
                hide.executeUpdate();
                res.close();
            }
        } finally {
            invites.close();
            hide.close();
        }
        
        
        try {
            for (i = 0; i < rimossi.length; i++) {

                rimosso = Integer.parseInt(rimossi[i]);

                stm1.setInt(1, id_gruppo);

                stm1.setInt(2, rimosso);

                System.out.println("id_gr =" + id_gruppo);
                System.out.println("id_user =" + rimosso);

                try {

                    stm1.executeUpdate();

                } catch (SQLException ex) {
                    throw new ServletException(ex);

                }
            }
        } finally {
            stm1.close();

        }

        return true;
//To change body of generated methods, choose Tools | Templates.
    }

    public Boolean editGroup(int id, String new_name, String new_thread, Users user) throws SQLException, ServletException {
                String edit_group = "UPDATE GRUPPI SET NAME=?,THREAD=? WHERE ID_GRUPPO=?";
                 PreparedStatement stm =conn.prepareStatement(edit_group);
                    Groups group= new Groups();
                    group = (Groups)this.getGroup(id, user);
                    System.out.println(new_name);
                    System.out.println(new_thread);
                     System.out.println(id);
                      System.out.println(user.getName());
                    System.out.println(group.getId());
                    if(new_thread.equals("")){
                        new_thread=group.getThread();
                        
                    }
                    if(new_name.equals("")){
                        new_name=group.getGroupName();
                    }
                        
                    
                    stm.setString(1, new_name);
                    stm.setString(2, new_thread);
                    stm.setInt(3, group.getId());
                    
                    try {

                    stm.executeUpdate();

                } catch (SQLException ex) {
                    throw new ServletException(ex);

                }finally{
                        stm.close();
                    }
        
        
        return true;
    }

}
