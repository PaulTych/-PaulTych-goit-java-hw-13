package hw13;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class ConnectionUtil {
    private static final HttpClient CONNECTION = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    public static String userTool(URI url, String method, User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method(method, HttpRequest.BodyPublishers.ofString(requestBody))
                .headers("Content-type", "application/json", "charset", "UTF-8")
                .build();
        HttpResponse<String> response = CONNECTION.send(request, HttpResponse.BodyHandlers.ofString());
        return "State: " + response.statusCode() + ";\n Body" + response.body();
    }

    public static List<User> getUsers(String url, int id, String username) throws IOException, InterruptedException {
        String parameters = "";
        if (id > 0) {
            parameters += "?id=" + id;
        }
        if (username != null && username.length() > 0) {
            if (parameters.length() > 0) {
                parameters += ",";
            } else {
                parameters = "?" + parameters;
            }
            parameters += "username=" + username;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + parameters))
                .GET()
                .headers("Content-type", "application/json", "charset", "UTF-8")
                .build();
        HttpResponse<String> response = CONNECTION.send(request, HttpResponse.BodyHandlers.ofString());
        List<User> listUsers = GSON.fromJson(response.body(), new TypeToken<List<User>>() {
        }.getType());
        return listUsers;
    }

    public static String getComments(String url_posts, String url_comments, int userId) throws IOException, InterruptedException {
        String parameters = "";
        String result="";

        //Search for user posts
        if (userId > 0) {
            parameters += "/" + userId+"/posts";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url_posts + parameters))
                .GET()
                .headers("Content-type", "application/json", "charset", "UTF-8")
                .build();
        HttpResponse<String> response_posts = CONNECTION.send(request, HttpResponse.BodyHandlers.ofString());
        List<Posts> posts = GSON.fromJson(response_posts.body(), new TypeToken<List<Posts>>() {
        }.getType());

        //Get last user post
        posts.sort((a, b) -> b.getId() - a.getId());
        if (posts.size()>0) {
            int postId = posts.get(0).getId();

            if (postId > 0) {
                //Get comments for last post
                parameters = "?postId=" + postId;
                HttpRequest requestComments = HttpRequest.newBuilder()
                        .uri(URI.create(url_comments + parameters))
                        .GET()
                        .headers("Content-type", "application/json", "charset", "UTF-8")
                        .build();
                HttpResponse<String> response_comments = CONNECTION.send(requestComments, HttpResponse.BodyHandlers.ofString());
                List<Comments> comments = GSON.fromJson(response_comments.body(), new TypeToken<List<Comments>>() {
                }.getType());

                //write file with comments
                String outJSON = new File("").getAbsolutePath() + "\\user-" + userId + "-post-" + postId + "-comments.json";
                File file = new File(outJSON);
                checkFile(file);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outJSON))) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(comments, writer);
                    result = "File: " + outJSON + " with user:" + userId + " comments is created.";
                } catch (IOException e) {
                    result = e.getMessage();
                }
            }
        }
        if (result.equals("")){
            result = "Nothing to save.";
        }
        return result;
    }

    public static void checkFile(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static List<Todos> getTodos(String url, int userId) throws IOException, InterruptedException {
        String parameters = "/"+userId+"/todos?completed=false";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + parameters))
                .GET()
                .headers("Content-type", "application/json", "charset", "UTF-8")
                .build();
        HttpResponse<String> response = CONNECTION.send(request, HttpResponse.BodyHandlers.ofString());
        List<Todos> listTodos = GSON.fromJson(response.body(), new TypeToken<List<Todos>>() {
        }.getType());
        return listTodos;
    }
}