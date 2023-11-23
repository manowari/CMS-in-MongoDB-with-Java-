package cms;
// CMSApplication.java
import cms.interface_module.UserInterface;
import cms.repository.ContentRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class CMSApplication {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("cms");

            ContentRepository contentRepository = new ContentRepository(database);

            UserInterface.configureRoutes(contentRepository);
        }
    }
}
