import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc.
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {

    private final Map<String, Document> storage = new HashMap<>();


    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.id == null) {
            document.id = UUID.randomUUID().toString();
            document.created = Instant.now();
        } else {
            Document existingDoc = storage.get(document.id);
            if (existingDoc != null) {
                document.created = existingDoc.created;
            }
        }
        storage.put(document.id, document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */

    public List<Document> search(SearchRequest request) {
        return storage.values().stream()
                .filter(doc -> filterByTitlePrefixes(doc, request.getTitlePrefixes()))
                .filter(doc -> filterByContentContains(doc, request.getContainsContents()))
                .filter(doc -> filterByAuthorIds(doc, request.getAuthorIds()))
                .filter(doc -> filterByCreatedRange(doc, request.getCreatedFrom(), request.getCreatedTo()))
                .collect(Collectors.toList());
    }

    private boolean filterByTitlePrefixes(Document doc, List<String> titlePrefixes) {
        if (titlePrefixes == null || titlePrefixes.isEmpty()) {
            return true;
        }
        return titlePrefixes.stream().anyMatch(prefix -> doc.getTitle() != null && doc.getTitle().startsWith(prefix));
    }

    private boolean filterByContentContains(Document doc, List<String> containsContents) {
        if (containsContents == null || containsContents.isEmpty()) {
            return true;
        }
        return containsContents.stream().anyMatch(content -> doc.getContent() != null && doc.getContent().contains(content));
    }

    private boolean filterByAuthorIds(Document doc, List<String> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return true;
        }
        return doc.getAuthor() != null && authorIds.contains(doc.getAuthor().getId());
    }

    private boolean filterByCreatedRange(Document doc, Instant createdFrom, Instant createdTo) {
        if (createdFrom != null && doc.getCreated().isBefore(createdFrom)) {
            return false;
        }
        return createdTo == null || !doc.getCreated().isAfter(createdTo);
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }



    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}