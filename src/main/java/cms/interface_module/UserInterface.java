package cms.interface_module;

// UserInterface.java
import cms.repository.ContentRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInterface {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("cms");

            ContentRepository contentRepository = new ContentRepository(database);

            configureRoutes(contentRepository);
        }
    }

    public static void configureRoutes(ContentRepository contentRepository) {
        Spark.port(4567);

        // Configure Thymeleaf template engine
        ThymeleafTemplateEngine templateEngine = new ThymeleafTemplateEngine("templates");

        // Define route to display articles
        Spark.get("/articles", (req, res) -> {
            List<Document> articles = contentRepository.getAllArticles();
            // Render articles using Thymeleaf template engine
            Map<String, Object> model = new HashMap<>();
            model.put("articles", articles);
            return templateEngine.render(new ModelAndView(model, "articles"));
        });

        // Define route to add a new article
        Spark.post("/articles", (req, res) -> {
            String title = req.queryParams("title");
            String content = req.queryParams("content");
            Document newArticle = new Document("title", title).append("content", content);
            contentRepository.addArticle(newArticle);
            res.redirect("/articles");
            return null;
        });

        // Define route to display sorted articles
        Spark.get("/articles/sort/:order", (req, res) -> {
            String order = req.params(":order");
            List<Document> sortedArticles = contentRepository.getSortedArticles(order);
            // Render sorted articles using Thymeleaf template engine
            Map<String, Object> model = new HashMap<>();
            model.put("articles", sortedArticles);
            return templateEngine.render(new ModelAndView(model, "articles"));
        });

        // Define route to search for articles
        Spark.get("/articles/search", (req, res) -> {
            String query = req.queryParams("query");
            List<Document> searchResults = contentRepository.searchArticles(query);
            // Render search results using Thymeleaf template engine
            Map<String, Object> model = new HashMap<>();
            model.put("articles", searchResults);
            return templateEngine.render(new ModelAndView(model, "articles"));
        });
    }
}
