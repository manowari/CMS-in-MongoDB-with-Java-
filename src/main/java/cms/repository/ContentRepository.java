package cms.repository;

// ContentRepository.java
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ContentRepository {
    private final MongoCollection<Document> articlesCollection;

    public ContentRepository(MongoDatabase database) {
        this.articlesCollection = database.getCollection("articles");
    }

    public List<Document> getAllArticles() {
        return articlesCollection.find().into(new ArrayList<>());
    }

    public void addArticle(Document article) {
        articlesCollection.insertOne(article);
    }

    public List<Document> getSortedArticles(String order) {
        FindIterable<Document> sortedArticles;
        if (order.equals("asc")) {
            sortedArticles = articlesCollection.find().sort(Sorts.ascending("title"));
        } else {
            sortedArticles = articlesCollection.find().sort(Sorts.descending("title"));
        }
        return sortedArticles.into(new ArrayList<>());
    }

    public List<Document> searchArticles(String query) {
        return articlesCollection.find(Filters.text(query)).into(new ArrayList<>());
    }
}
