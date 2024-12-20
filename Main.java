import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    Database db = new Database("myDB");

    try {
      // Initialize database
      db.init().join();

      // Create users table
      Map<String, FieldSchema> userSchema = new HashMap<>();
      userSchema.put("name", new FieldSchema("string", true));
      userSchema.put("email", new FieldSchema("string", true));
      userSchema.put("age", new FieldSchema("number", false));
      db.createTable("users", userSchema).join();

      // Create posts table
      Map<String, FieldSchema> postSchema = new HashMap<>();
      postSchema.put("title", new FieldSchema("string", true));
      postSchema.put("content", new FieldSchema("string", true));
      postSchema.put("userId", new FieldSchema("number", true));
      db.createTable("posts", postSchema).join();

      // Insert user
      Map<String, Object> userData = new HashMap<>();
      userData.put("name", "John Doe");
      userData.put("email", "john.doe@gmail.com");
      userData.put("age", 30);
      Map<String, Object> user = db.insert("users", userData).join();

      db.printState();

      // Insert post
      Map<String, Object> postData = new HashMap<>();
      postData.put("title", "My First Post");
      postData.put("content", "Hello, world");
      postData.put("userId", user.get("id"));
      db.insert("posts", postData).join();

      System.out.println("Post created");
      db.printState();

      // Select users
      Map<String, Object> conditions = new HashMap<>();
      conditions.put("name", "John Doe");
      List<Map<String, Object>> users = db.select("users", conditions);
      System.out.println("\nUsers: " + users);

      // Join users and posts
      List<Map<String, Object>> postsWithUsers = db.join("users", "users", "userId");
      System.out.println("\nPosts with users: " + postsWithUsers);

      // Update user
      Map<String, Object> updates = new HashMap<>();
      updates.put("age", 32);
      db.update("users", (Integer) user.get("id"), updates).join();

      System.out.println("\nData after updating operation");
      db.printState();

      // Delete user
      // db.delete("users", (Integer) user.get("id")).join();

      // Print final state
      // db.printState();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
