package hw13;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TestTasks {
    private static final String URL_USERS = "https://jsonplaceholder.typicode.com/users";
    private static final String URL_POSTS = "https://jsonplaceholder.typicode.com/posts";
    private static final String URL_COMMENTS = "https://jsonplaceholder.typicode.com/comments";
    private static final String URL_TODOS = "https://jsonplaceholder.typicode.com/todos";

    public static void main(String[] args) throws IOException, InterruptedException {
        Address address1 = new Address("Peremohy", "112a", "Kyiv", "01002", 2.12312F, 0.2323F);
        User user = new User(1, "Alexey", "Alex", "alex@mail.com", address1);

        System.out.println("----------------------Task 1.1-----------------------------");
        final String createdUser = ConnectionUtil.userTool(URI.create(URL_USERS), "POST", user);
        System.out.println("Created user:");
        System.out.println(createdUser);

        System.out.println("----------------------Task 1.2-----------------------------");
        User changedUser = new User(1, "Olexiy", "Alex", "olex@mail.com", address1);
        final String updatedUser = ConnectionUtil.userTool(URI.create(URL_USERS+"/1"), "PUT", changedUser);
        System.out.println("Updated user:");
        System.out.println(updatedUser);

        System.out.println("----------------------Task 1.3-----------------------------");
        final String deletedUser = ConnectionUtil.userTool(URI.create(URL_USERS+"/1"), "DELETE", changedUser);
        System.out.println("Deleted user:");
        System.out.println(deletedUser);

        System.out.println("----------------------Task 1.4-----------------------------");
        List<User> users = ConnectionUtil.getUsers(URL_USERS, 0,"");
        System.out.println("List of users:");
        System.out.println(users);

        System.out.println("----------------------Task 1.5-----------------------------");
        users = ConnectionUtil.getUsers(URL_USERS, 2,"");
        System.out.println("User ws id=2:");
        System.out.println(users);

        System.out.println("----------------------Task 1.6-----------------------------");
        users = ConnectionUtil.getUsers(URL_USERS, 0,"Samantha");
        System.out.println("User ws username Samantha:");
        System.out.println(users);

        System.out.println("----------------------Task 2-----------------------------");
        String comments = ConnectionUtil.getComments(URL_USERS,URL_COMMENTS, 2);
        System.out.println(comments);

        System.out.println("----------------------Task 3-----------------------------");
        List<Todos> todos = ConnectionUtil.getTodos(URL_USERS, 2);
        System.out.println(todos);
    }
}
