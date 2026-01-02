package algorithms;

import model.SocialGraph;
import model.UserNode;

import java.util.*;

/**
 * Dijkstra Algoritması
 * İki düğüm arasındaki en kısa yolu bulur.
 * Ağırlıklı graflarda en kısa yol problemlerini çözer.
 */
public class DijkstraAlgorithm implements IGraphAlgorithm {
    private long executionTime;

    public DijkstraAlgorithm() {
        // Constructor
    }

    @Override
    public List<UserNode> execute(SocialGraph graph, UserNode startNode, UserNode endNode) {
        long startTime = System.nanoTime();

        if (startNode == null || endNode == null) {
            throw new IllegalArgumentException("Başlangıç ve hedef düğümler null olamaz");
        }

        if (startNode.getId() == endNode.getId()) {
            long endTime = System.nanoTime();
            executionTime = (endTime - startTime) / 1_000_000;
            return new ArrayList<>(Collections.singletonList(startNode));
        }

        // Dijkstra algoritması için gerekli veri yapıları
        Map<Integer, Double> distances = new HashMap<>(); // Başlangıçtan her düğüme olan en kısa mesafe
        Map<Integer, UserNode> previous = new HashMap<>(); // Yol takibi için
        Set<Integer> visited = new HashSet<>(); // Ziyaret edilen düğümler
        PriorityQueue<DijkstraNode> priorityQueue = new PriorityQueue<>(
            Comparator.comparingDouble(n -> distances.getOrDefault(n.nodeId, Double.POSITIVE_INFINITY))
        );

        // Tüm düğümlerin mesafelerini sonsuz olarak başlat
        for (UserNode node : graph.getAllNodes()) {
            distances.put(node.getId(), Double.POSITIVE_INFINITY);
        }

        // Başlangıç düğümünü başlat
        distances.put(startNode.getId(), 0.0);
        priorityQueue.add(new DijkstraNode(startNode.getId()));

        while (!priorityQueue.isEmpty()) {
            DijkstraNode current = priorityQueue.poll();
            int currentNodeId = current.nodeId;

            // Zaten ziyaret edildiyse atla
            if (visited.contains(currentNodeId)) {
                continue;
            }

            visited.add(currentNodeId);

            // Hedefe ulaşıldı mı?
            if (currentNodeId == endNode.getId()) {
                long endTime = System.nanoTime();
                executionTime = (endTime - startTime) / 1_000_000; // milisaniye
                return reconstructPath(previous, startNode, endNode);
            }

            UserNode currentNode = graph.getNode(currentNodeId);
            if (currentNode == null) {
                continue;
            }

            // Komşuları incele
            List<UserNode> neighbors = graph.getNeighbors(currentNode);
            for (UserNode neighbor : neighbors) {
                int neighborId = neighbor.getId();

                if (visited.contains(neighborId)) {
                    continue; // Zaten ziyaret edildi
                }

                // Mevcut düğümden komşuya olan mesafe
                double edgeWeight = graph.getEdgeWeight(currentNode, neighbor);
                double newDistance = distances.get(currentNodeId) + edgeWeight;

                // Daha kısa bir yol bulundu mu?
                if (newDistance < distances.get(neighborId)) {
                    distances.put(neighborId, newDistance);
                    previous.put(neighborId, currentNode);
                    priorityQueue.add(new DijkstraNode(neighborId));
                }
            }
        }

        // Yol bulunamadı
        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1_000_000;
        return new ArrayList<>(); // Boş liste döndür
    }

    /**
     * Yolu geri oluşturur (previous map'inden)
     */
    private List<UserNode> reconstructPath(Map<Integer, UserNode> previous, UserNode start, UserNode end) {
        List<UserNode> path = new ArrayList<>();
        UserNode current = end;

        while (current != null) {
            path.add(current);
            current = previous.get(current.getId());
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * Algoritmanın çalışma süresini getirir (milisaniye)
     */
    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public String getName() {
        return "Dijkstra's Shortest Path";
    }

    /**
     * Dijkstra algoritması için node wrapper sınıfı
     */
    private static class DijkstraNode {
        int nodeId;

        DijkstraNode(int nodeId) {
            this.nodeId = nodeId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DijkstraNode that = (DijkstraNode) o;
            return nodeId == that.nodeId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeId);
        }
    }
}

