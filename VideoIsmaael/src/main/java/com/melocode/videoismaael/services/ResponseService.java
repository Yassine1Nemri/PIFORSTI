package com.melocode.videoismaael.services;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.melocode.videoismaael.entities.Response;
import com.melocode.videoismaael.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponseService implements ICrud<Response> {
    private Connection cnx;

    public ResponseService() {
        this.cnx = DatabaseConnection.getConnection();
    }

    @Override
    public void ajouterEntite(Response response) {
        String query = "INSERT INTO response (id_rec, id_admin, content,date_reponse) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setInt(1, response.getId_rec());
            st.setInt(2, response.getId_admin());
            st.setString(3, response.getContent());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Response added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Response> afficherEntite() {
        List<Response> responses = new ArrayList<>();
        String query = "SELECT * FROM response";

        try  {
              PreparedStatement st = cnx.prepareStatement(query);
  ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Response response = new Response(
                        rs.getInt("id_response"),
                        rs.getInt("id_rec"),
                        rs.getInt("id_admin"),
                        rs.getString("content"),
                        rs.getString("date_reponse")
                );
                responses.add(response);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving responses: " + e.getMessage());
            e.printStackTrace();
        }
        return responses;
    }

    @Override
    public void modifierEntite(Response response) {
        String query = "UPDATE response SET content=? WHERE id_response=?";
        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setString(1, response.getContent());
            st.setInt(2, response.getId_response());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Response updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error updating response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerEntite(Response response) {
        String query = "DELETE FROM response WHERE id_response=?";
        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setInt(1, response.getId_response());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Response deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Additional useful methods
    public Response getResponseForReclamation(int reclamationId) {
        String query = "SELECT * FROM response WHERE id_rec = ?";
        try  {PreparedStatement st = cnx.prepareStatement(query);
            st.setInt(1, reclamationId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Response(
                        rs.getInt("id_response"),
                        rs.getInt("id_rec"),
                        rs.getInt("id_admin"),
                        rs.getString("content"),
                        rs.getString("date_response"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving response for reclamation: " + e.getMessage());
            e.printStackTrace();
        }
        return null ;
    }

    public List<Response> getResponsesByAdmin(int adminId) {
        List<Response> responses = new ArrayList<>();
        String query = "SELECT * FROM response WHERE id_admin = ?";

        try (PreparedStatement st = cnx.prepareStatement(query)) {
            st.setInt(1, adminId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                responses.add(new Response(
                        rs.getInt("id_response"),
                        rs.getInt("id_rec"),
                        rs.getInt("id_admin"),
                        rs.getString("content"),
                        rs.getString("date_response")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving responses by admin: " + e.getMessage());
            e.printStackTrace();
        }
        return responses;
    }
    public void getreclamationId(){

    }
}