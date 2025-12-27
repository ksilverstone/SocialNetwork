package model;

import java.util.*;

/**
 * Sosyal ağ grafını temsil eden sınıf.
 * Düğümler (UserNode) ve kenarlar (RelationshipEdge) yönetir.
 */
public class SocialGraph {
    private Map<Integer, UserNode> nodes;
    private Map<Integer, List<RelationshipEdge>> adjacencyList;
    private List<RelationshipEdge> edges;

    public SocialGraph() {
        this.nodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    /**
     * Düğüm ekler
     */
    public void addNode(UserNode node) {
        if (nodes.containsKey(node.getId())) {
            throw new IllegalArgumentException("Düğüm zaten mevcut: " + node.getId());
        }
        nodes.put(node.getId(), node);
        adjacencyList.put(node.getId(), new ArrayList<>());
    }

    /**
     * Düğüm siler
     */
    public boolean removeNode(int nodeId) {
        if (!nodes.containsKey(nodeId)) {
            return false;
        }

        // Tüm kenarları kaldır
        List<RelationshipEdge> edgesToRemove = new ArrayList<>(adjacencyList.get(nodeId));
        for (RelationshipEdge edge : edgesToRemove) {
            removeEdge(nodeId, edge.getDestination().getId());
        }

        // Diğer düğümlerden bu düğüme gelen kenarları kaldır
        for (List<RelationshipEdge> edgeList : adjacencyList.values()) {
            edgeList.removeIf(edge -> edge.getDestination().getId() == nodeId);
        }

        nodes.remove(nodeId);
        adjacencyList.remove(nodeId);
        return true;
    }

    /**
     * Kenar ekler (yönsüz graf için her iki yönde de ekler)
     */
    public void addEdge(UserNode source, UserNode destination) {
        if (source.getId() == destination.getId()) {
            throw new IllegalArgumentException("Self-loop'a izin verilmez");
        }
        if (!nodes.containsKey(source.getId()) || !nodes.containsKey(destination.getId())) {
            throw new IllegalArgumentException("Düğümler graf içinde olmalıdır");
        }

        // Aynı kenarın zaten var olup olmadığını kontrol et
        if (hasEdge(source.getId(), destination.getId())) {
            return; // Kenar zaten mevcut
        }

        RelationshipEdge edge1 = new RelationshipEdge(source, destination);
        RelationshipEdge edge2 = new RelationshipEdge(destination, source);

        adjacencyList.get(source.getId()).add(edge1);
        adjacencyList.get(destination.getId()).add(edge2);
        edges.add(edge1);
    }

    /**
     * Kenar siler
     */
    public boolean removeEdge(int sourceId, int destinationId) {
        if (!hasEdge(sourceId, destinationId)) {
            return false;
        }

        adjacencyList.get(sourceId).removeIf(edge -> edge.getDestination().getId() == destinationId);
        adjacencyList.get(destinationId).removeIf(edge -> edge.getDestination().getId() == sourceId);
        edges.removeIf(edge -> 
            (edge.getSource().getId() == sourceId && edge.getDestination().getId() == destinationId) ||
            (edge.getSource().getId() == destinationId && edge.getDestination().getId() == sourceId)
        );
        return true;
    }

    /**
     * İki düğüm arasında kenar olup olmadığını kontrol eder
     */
    public boolean hasEdge(int sourceId, int destinationId) {
        if (!adjacencyList.containsKey(sourceId)) {
            return false;
        }
        return adjacencyList.get(sourceId).stream()
                .anyMatch(edge -> edge.getDestination().getId() == destinationId);
    }

    /**
     * Düğümü ID'ye göre getirir
     */
    public UserNode getNode(int nodeId) {
        return nodes.get(nodeId);
    }

    /**
     * Tüm düğümleri getirir
     */
    public Collection<UserNode> getAllNodes() {
        return nodes.values();
    }

    /**
     * Bir düğümün komşularını getirir
     */
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

    /**
     * Bir düğümün kenarlarını getirir
     */
    public List<RelationshipEdge> getEdges(int nodeId) {
        return adjacencyList.getOrDefault(nodeId, new ArrayList<>());
    }

    /**
     * Tüm kenarları getirir
     */
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
     * Grafın düğüm sayısını getirir
     */
    public int getNodeCount() {
        return nodes.size();
    }

    /**
     * Grafın kenar sayısını getirir
     */
    public int getEdgeCount() {
        return edges.size();
    }

    /**
     * Grafı temizler
     */
    public void clear() {
        nodes.clear();
        adjacencyList.clear();
        edges.clear();
    }
}


