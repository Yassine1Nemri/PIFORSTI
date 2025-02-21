package com.melocode.videoismaael.services;

import com.melocode.videoismaael.entities.User;
import org.apache.poi.ss.usermodel.*;
import com.melocode.videoismaael.tools.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class UserService implements ICrud<User> {
    private Connection cnx2;

    public UserService() {
        this.cnx2 = DatabaseConnection.getConnection();
    }

    @Override
    public void ajouterEntite(User user) {
        String query = "INSERT INTO `user`( `email`, `roles`, `password`, `name`, `prenom`, `tel`, `is_banned`) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement st = cnx2.prepareStatement(query)) {
            st.setString(1, user.getEmail());
            st.setString(2, user.getRoles());
            st.setString(3, /*hashPassword*/(user.getPassword())); // Hash the password before storing
            st.setString(4, user.getName());
            st.setString(5, user.getPrenom());
            st.setInt(6, user.getTel());
            st.setInt(7, user.getIs_banned());
            executeUpdate(st, "Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            handleSQLException(e, "ajout de l'utilisateur");
        }
    }

    @Override
    public List<User> afficherEntite() {
        return List.of();
    }


    public ResultSet log(String email, String pw) {
        ResultSet rs = null;
        try {
            String req = "SELECT * FROM user WHERE email = '" + email + "' AND password = '" + pw + "'";
            PreparedStatement st = cnx2.prepareStatement(req);
            rs = st.executeQuery(req);
            return rs;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }

    public ResultSet Getall() {
        ResultSet rs = null;
        try {
            String req = "SELECT * FROM `user`";
            PreparedStatement st = cnx2.prepareStatement(req);
            rs = st.executeQuery(req);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }


    @Override
    public void modifierEntite(User user) {
        String query = "UPDATE user SET email=?, password=?, name=?, prenom=?, tel=? WHERE id =?";
        try (PreparedStatement st = cnx2.prepareStatement(query)) {
            setUserParams(st, user);
            executeUpdate(st, "Utilisateur modifié avec succès !");
        } catch (SQLException e) {
            handleSQLException(e, "modification de l'utilisateur");
        }
    }

    public void modifierMdp(String email, String password) {
        String query = "UPDATE user SET password=? WHERE email=?";
        try (PreparedStatement st = cnx2.prepareStatement(query)) {
            st.setString(1, (password)); // Hash the new password
            st.setString(2, email);
            executeUpdate(st, "Mot de passe modifié avec succès !");
        } catch (SQLException e) {
            handleSQLException(e, "modification du mot de passe");
        }
    }


    public void supprimerEntite(User user) {
        String query = "DELETE FROM user WHERE id=?";
        try (PreparedStatement st = cnx2.prepareStatement(query)) {
            st.setInt(1, user.getId());
            executeUpdate(st, "Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            handleSQLException(e, "suppression de l'utilisateur");
        }
    }



    // 🔹 Additional methods (search, ban, export)

    public ResultSet searchUsers(String searchText) {
        String query = "SELECT * FROM user WHERE name LIKE ? OR email LIKE ?";
        return executeQueryWithParams(query, "%" + searchText + "%", "%" + searchText + "%");
    }

    public ResultSet getUsersInRange(int startIndex, int endIndex) {
        String query = "SELECT * FROM user LIMIT ? OFFSET ?";
        return executeQueryWithParams(query, endIndex - startIndex, startIndex);
    }


    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private void setUserParams(PreparedStatement st, User user) throws SQLException {
        st.setString(1, user.getEmail());
        st.setString(2, hashPassword(user.getPassword())); // Hash the password before storing
        st.setString(3, user.getName());
        st.setString(4, user.getPrenom());
        st.setInt(5, user.getTel());
        st.setInt(6, user.getId());
    }

    private void executeUpdate(PreparedStatement st, String successMessage) throws SQLException {
        int rowsAffected = st.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println(successMessage);
        } else {
            System.out.println("Aucune modification effectuée.");
        }
    }

    private ResultSet executeQueryWithParams(String query, Object... params) {
        try (PreparedStatement st = cnx2.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                st.setObject(i + 1, params[i]);
            }
            return st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleSQLException(SQLException e, String action) {
        System.out.println("Erreur SQL lors de l'" + action + " : " + e.getMessage());
        e.printStackTrace();
    }

    private void createHeaderRow(Row row, String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
        }
    }

    private void populateSheetWithData(ResultSet rs, Sheet sheet) throws SQLException {
        int rowIndex = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(rs.getInt("id"));
            row.createCell(1).setCellValue(rs.getString("email"));
            row.createCell(2).setCellValue(rs.getString("roles"));
            row.createCell(3).setCellValue(rs.getString("password"));
            row.createCell(4).setCellValue(rs.getString("name"));
            row.createCell(5).setCellValue(rs.getString("prenom"));
            row.createCell(6).setCellValue(rs.getInt("tel"));
            row.createCell(7).setCellValue(rs.getInt("is_banned"));
        }
    }





}
