import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        DocumentManager documentManager = new DocumentManager();

        DocumentManager.Author author1 = DocumentManager.Author.builder()
                .id("auth1")
                .name("John Doe")
                .build();

        DocumentManager.Author author2 = DocumentManager.Author.builder()
                .id("auth2")
                .name("Jane Smith")
                .build();

        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .title("Document One")
                .content("This is the first document.")
                .author(author1)
                .build();

        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Document Two")
                .content("This is the second document.")
                .author(author2)
                .build();

        documentManager.save(doc1);
        documentManager.save(doc2);

        System.out.println("Saved Document 1: " + doc1);
        System.out.println("Saved Document 2: " + doc2);

        Optional<DocumentManager.Document> foundDoc = documentManager.findById(doc1.getId());
        System.out.println("Found Document by ID: " + foundDoc.orElse(null));

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Collections.singletonList( "Document"))
                .authorIds(Collections.singletonList( "auth1"))
                .build();

        List<DocumentManager.Document> searchResults = documentManager.search(searchRequest);

        for (DocumentManager.Document doc : searchResults) {
            System.out.println(doc);
        }
    }
}
