package algorithms;

import model.SocialGraph;
import model.UserNode;

import java.util.*;

/**
 * A* (A-Star) Algoritması
 * İki düğüm arasındaki en kısa yolu bulur.
 * Heuristic fonksiyon kullanarak Dijkstra'dan daha verimli çalışır.
 */
public class AStarAlgorithm implements IGraphAlgorithm {
    private SocialGraph graph;
    private long executionTime;

    public AStarAlgorithm() {
        // Constructor
    }

    @Override
    public List<UserNode> execute(SocialGraph graph, UserNode startNode, UserNode endNode) {
        this.graph = graph;
        long startTime = System.nanoTime();

        if (startNode == null || endNode == null) {
            throw new IllegalArgumentException("Başlangıç ve hedef düğümler null olamaz");
        }

        if (startNode.getId() == endNode.getId()) {
            return new ArrayList<>(Collections.singletonList(startNode));
        }

        // A* algoritması için gerekli veri yapıları
        Map<Integer, Double> gScore = new HashMap<>(); // Başlangıçtan düğüme olan gerçek maliyet
        Map<Integer, Double> fScore = new HashMap<>(); // f(n) = g(n) + h(n)
        Map<Integer, UserNode> cameFrom = new HashMap<>(); // Yol takibi için
        Set<Integer> closedSet = new HashSet<>(); // Ziyaret edilen düğümler
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> fScore.getOrDefault(n.nodeId, Double.POSITIVE_INFINITY)));

        // Başlangıç düğümünü başlat
        gScore.put(startNode.getId(), 0.0);
        fScore.put(startNode.getId(), heuristic(startNode, endNode));
        openSet.add(new AStarNode(startNode.getId(), fScore.get(startNode.getId())));

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();
            int currentNodeId = current.nodeId;

            // Hedefe ulaşıldı mı?
            if (currentNodeId == endNode.getId()) {
                long endTime = System.nanoTime();
                executionTime = (endTime - startTime) / 1_000_000; // milisaniye
                return reconstructPath(cameFrom, startNode, endNode);
            }

            closedSet.add(currentNodeId);
            UserNode currentNode = graph.getNode(currentNodeId);

            // Komşuları incele
            List<UserNode> neighbors = graph.getNeighbors(currentNode);
            for (UserNode neighbor : neighbors) {
                int neighborId = neighbor.getId();

                if (closedSet.contains(neighborId)) {
                    continue; // Zaten ziyaret edildi
                }

                // Komşuya olan maliyet
                double tentativeGScore = gScore.get(currentNodeId) + graph.getEdgeWeight(currentNode, neighbor);

                if (!gScore.containsKey(neighborId) || tentativeGScore < gScore.get(neighborId)) {
                    cameFrom.put(neighborId, currentNode);
                    gScore.put(neighborId, tentativeGScore);
                    double h = heuristic(neighbor, endNode);
                    fScore.put(neighborId, tentativeGScore + h);

                    // Open set'te yoksa ekle
                    if (!openSet.contains(new AStarNode(neighborId, 0))) {
                        openSet.add(new AStarNode(neighborId, fScore.get(neighborId)));
                    }
                }
            }
        }

        // Yol bulunamadı
        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1_000_000;
        return new ArrayList<>(); // Boş liste döndür
    }

    /**
     * Heuristic fonksiyon: İki düğüm arasındaki tahmini maliyet
     * Özellik farklarına dayalı bir heuristic kullanıyoruz
     */
    private double heuristic(UserNode from, UserNode to) {
        double diffActive = from.getActiveScore() - to.getActiveScore();
        double diffInteraction = from.getInteractionScore() - to.getInteractionScore();
        double diffConnection = from.getConnectionScore() - to.getConnectionScore();

        double distance = Math.sqrt(
            diffActive * diffActive +
            diffInteraction * diffInteraction +
            diffConnection * diffConnection
        );

        // Heuristic değeri, özellik farklarına dayalı bir tahmin
        // Benzer düğümler arası mesafe küçük olur
        return distance;
    }

    /**
     * Yolu geri oluşturur (cameFrom map'inden)
     */
    private List<UserNode> reconstructPath(Map<Integer, UserNode> cameFrom, UserNode start, UserNode end) {
        List<UserNode> path = new ArrayList<>();
        UserNode current = end;

        while (current != null) {
            path.add(current);
            current = cameFrom.get(current.getId());
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
        return "A* (A-Star) Pathfinding";
    }

    /**
     * A* algoritması için node wrapper sınıfı
     */
    private static class AStarNode {
        int nodeId;
        double fScore;

        AStarNode(int nodeId, double fScore) {
            this.nodeId = nodeId;
            this.fScore = fScore;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AStarNode aStarNode = (AStarNode) o;
            return nodeId == aStarNode.nodeId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeId);
        }
    }
}

