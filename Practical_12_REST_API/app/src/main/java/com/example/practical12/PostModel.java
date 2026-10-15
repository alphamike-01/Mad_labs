package com.example.practical12;

/**
 * Model representing an enterprise data post fetched from the REST API.
 */
public class PostModel {
    private final int id;
    private final int userId;
    private final String title;
    private final String body;

    public PostModel(int id, int userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
