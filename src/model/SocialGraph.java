package model;

import java.util.*;

public class SocialGraph {
    // Değişkenler (Final yapmak iyi pratiktir ama şimdilik böyle kalsın)
    private Map<Integer, UserNode> nodes;
    private Map<Integer, List<RelationshipEdge>> adjacencyList;
    private List<RelationshipEdge> edges;

    public SocialGraph() {
        this.nodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    public void addNode(UserNode node) {
        if (nodes.containsKey(node.getId())) {
            throw new IllegalArgumentException("Düğüm zaten mevcut: " + node.getId());
        }
        nodes.put(node.getId(), node);
        adjacencyList.put(node.getId(), new ArrayList<>());
    }

    public void addEdge(UserNode source, UserNode destination) {
        if (source.getId() == destination.getId()) {
            throw new IllegalArgumentException("Self-loop'a izin verilmez");
        }
        // Düğümler yoksa hata fırlatmak yerine ekleyebiliriz veya hata fırlatabiliriz.
        // Mevcut yapıda hata fırlatıyoruz:
        if (!nodes.containsKey(source.getId()) || !nodes.containsKey(destination.getId())) {
            throw new IllegalArgumentException("Düğümler graf içinde olmalıdır");
        }

        if (hasEdge(source.getId(), destination.getId())) {
            return;
        }

        RelationshipEdge edge1 = new RelationshipEdge(source, destination);
        RelationshipEdge edge2 = new RelationshipEdge(destination, source); // Yönsüz olduğu için

        adjacencyList.get(source.getId()).add(edge1);
        adjacencyList.get(destination.getId()).add(edge2);
        edges.add(edge1);
    }

    public boolean hasEdge(int sourceId, int destinationId) {
        if (!adjacencyList.containsKey(sourceId)) {
            return false;
        }
        return adjacencyList.get(sourceId).stream()
                .anyMatch(edge -> edge.getDestination().getId() == destinationId);
    }

    // --- BFS İÇİN GEREKLİ DÜZELTME BURADA ---

    // 1. Versiyon: ID ile çağırmak istersek
    public List<UserNode> getNeighbors(int nodeId) {
        if (!adjacencyList.containsKey(nodeId)) {
            return new ArrayList<>();
        }
        List<UserNode> neighbors = new ArrayList<>();
        for (RelationshipEdge edge : adjacencyList.get(nodeId)) {
            neighbors.add(edge.getDestination());
        }
        return neighbors;
    }

    // 2. Versiyon: BFS algoritması UserNode nesnesi gönderdiği için bu lazım!
    public List<UserNode> getNeighbors(UserNode node) {
        // Doğrudan yukarıdaki ID'li metodu çağırıyoruz, kod tekrarı yapmıyoruz.
        return getNeighbors(node.getId());
    }

    // Getter Metotları
    public UserNode getNode(int nodeId) {
        return nodes.get(nodeId);
    }

    public Collection<UserNode> getAllNodes() {
        return nodes.values();
    }

    public List<RelationshipEdge> getAllEdges() {
        return new ArrayList<>(edges);
    }

    /**
     * İki düğüm arasındaki kenarın ağırlığını getirir
     */
    public double getEdgeWeight(int sourceId, int destinationId) {
        if (!adjacencyList.containsKey(sourceId)) {
            return Double.POSITIVE_INFINITY;
        }
        return adjacencyList.get(sourceId).stream()
                .filter(edge -> edge.getDestination().getId() == destinationId)
                .findFirst()
                .map(RelationshipEdge::getWeight)
                .orElse(Double.POSITIVE_INFINITY);
    }

    /**
     * İki düğüm arasındaki kenarın ağırlığını getirir (UserNode parametreli)
     */
    public double getEdgeWeight(UserNode source, UserNode destination) {
        return getEdgeWeight(source.getId(), destination.getId());
    }
}