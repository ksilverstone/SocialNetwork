package algorithms;

import model.SocialGraph;
import model.UserNode;

import java.util.*;

/**
 * DFS (Derinlik Öncelikli Arama) Algoritması
 * Bir düğümden erişilebilen tüm kullanıcıları bulur.
 */
public class DFS {
    private SocialGraph graph;
    private Set<Integer> visited;
    private List<UserNode> reachableNodes;
    private long executionTime;

    public DFS(SocialGraph graph) {
        this.graph = graph;
        this.visited = new HashSet<>();
        this.reachableNodes = new ArrayList<>();
    }

    /**
     * Belirli bir düğümden erişilebilen tüm düğümleri bulur
     * @param startNodeId Başlangıç düğümü ID'si
     * @return Erişilebilen düğümlerin listesi
     */
    public List<UserNode> findReachableNodes(int startNodeId) {
        long startTime = System.nanoTime();
        
        visited.clear();
        reachableNodes.clear();

        if (graph.getNode(startNodeId) == null) {
            throw new IllegalArgumentException("Başlangıç düğümü graf içinde bulunamadı: " + startNodeId);
        }

        dfsRecursive(startNodeId);

        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1_000_000; // milisaniye cinsinden

        return new ArrayList<>(reachableNodes);
    }

    /**
     * Recursive DFS implementasyonu
     */
    private void dfsRecursive(int nodeId) {
        visited.add(nodeId);
        UserNode currentNode = graph.getNode(nodeId);
        if (currentNode != null) {
            reachableNodes.add(currentNode);
        }

        // Komşuları ziyaret et
        List<UserNode> neighbors = graph.getNeighbors(nodeId);
        for (UserNode neighbor : neighbors) {
            if (!visited.contains(neighbor.getId())) {
                dfsRecursive(neighbor.getId());
            }
        }
    }

    /**
     * Iterative DFS implementasyonu (stack kullanarak)
     */
    public List<UserNode> findReachableNodesIterative(int startNodeId) {
        long startTime = System.nanoTime();
        
        visited.clear();
        reachableNodes.clear();

        if (graph.getNode(startNodeId) == null) {
            throw new IllegalArgumentException("Başlangıç düğümü graf içinde bulunamadı: " + startNodeId);
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(startNodeId);

        while (!stack.isEmpty()) {
            int currentNodeId = stack.pop();

            if (!visited.contains(currentNodeId)) {
                visited.add(currentNodeId);
                UserNode currentNode = graph.getNode(currentNodeId);
                if (currentNode != null) {
                    reachableNodes.add(currentNode);
                }

                // Komşuları stack'e ekle
                List<UserNode> neighbors = graph.getNeighbors(currentNodeId);
                for (UserNode neighbor : neighbors) {
                    if (!visited.contains(neighbor.getId())) {
                        stack.push(neighbor.getId());
                    }
                }
            }
        }

        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1_000_000; // milisaniye cinsinden

        return new ArrayList<>(reachableNodes);
    }

    /**
     * Algoritmanın çalışma süresini getirir (milisaniye)
     */
    public long getExecutionTime() {
        return executionTime;
    }

    /**
     * Ziyaret edilen düğüm sayısını getirir
     */
    public int getVisitedCount() {
        return visited.size();
    }
}


