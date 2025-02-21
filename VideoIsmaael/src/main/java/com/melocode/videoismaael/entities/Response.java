package com.melocode.videoismaael.entities;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Response {
    private int id_response;
    private int id_rec;
    private int id_admin;
    private String content;
    private String date_response;

    // Default constructor
    public Response() {
    }

    // Full constructor
    public Response(int id_response, int id_rec, int id_admin, String content, String date_response) {
        this.id_response = id_response;
        this.id_rec = id_rec;
        this.id_admin = id_admin;
        this.content = content;
        this.date_response = date_response;
    }

    // Constructor without ID (for new responses)
    public Response(int id_rec, int id_admin, String content) {
        this.id_rec = id_rec;
        this.id_admin = id_admin;
        this.content = content;

    }

    // Getters and Setters
    public int getId_response() {
        return id_response;
    }

    public void setId_response(int id_response) {
        this.id_response = id_response;
    }

    public int getId_rec() {
        return id_rec;
    }

    public void setId_rec(int id_rec) {
        this.id_rec = id_rec;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_response() {
        return date_response;
    }

    public void setDate_response(String date_response) {
        this.date_response = date_response;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id_response=" + id_response +
                ", id_rec=" + id_rec +
                ", id_admin=" + id_admin +
                ", content='" + content + '\'' +
                ", date_response=" + date_response +
                '}';
    }
}