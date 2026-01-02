package algorithms;

import model.SocialGraph;
import model.UserNode;

import java.util.*;

/**
 * Bağlı Bileşenler (Connected Components) Algoritması
 * Graf içindeki ayrık toplulukları (bağlı bileşenleri) tespit eder.
 * Her bağlı bileşen, birbirine ulaşılabilen düğümlerin kümesidir.
 */
public class ConnectedComponents {
    private SocialGraph graph;
    private List<List<UserNode>> components;
    private Map<Integer, Integer> nodeToComponentId;
    private long executionTime;

    public ConnectedComponents(SocialGraph graph) {
        this.graph = graph;
        this.components = new ArrayList<>();
        this.nodeToComponentId = new HashMap<>();
    }

    /**
     * Graf içindeki tüm bağlı bileşenleri bulur
     * @return Her bağlı bileşenin düğüm listesini içeren liste
     */
    public List<List<UserNode>> findConnectedComponents() {
        long startTime = System.nanoTime();

        components.clear();
        nodeToComponentId.clear();
        Set<Integer> visited = new HashSet<>();

        // Tüm düğümleri kontrol et
        for (UserNode node : graph.getAllNodes()) {
            if (!visited.contains(node.getId())) {
                // Yeni bir bağlı bileşen bulundu
                List<UserNode> component = new ArrayList<>();
                dfsComponent(node, visited, component);
                components.add(component);

                // Her düğüme bileşen ID'si ata
                int componentId = components.size() - 1;
                for (UserNode n : component) {
                    nodeToComponentId.put(n.getId(), componentId);
                }
            }
        }

        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1_000_000; // milisaniye

        return new ArrayList<>(components);
    }

    /**
     * DFS kullanarak bir bağlı bileşendeki tüm düğümleri bulur
     */
    private void dfsComponent(UserNode node, Set<Integer> visited, List<UserNode> component) {
        visited.add(node.getId());
        component.add(node);

        // Komşuları ziyaret et
        List<UserNode> neighbors = graph.getNeighbors(node);
        for (UserNode neighbor : neighbors) {
            if (!visited.contains(neighbor.getId())) {
                dfsComponent(neighbor, visited, component);
            }
        }
    }

    /**
     * Belirli bir düğümün hangi bileşende olduğunu getirir
     * @param nodeId Düğüm ID'si
     * @return Bileşen listesi (null if node not found)
     */
    public List<UserNode> getComponentOfNode(int nodeId) {
        Integer componentId = nodeToComponentId.get(nodeId);
        if (componentId == null) {
            return null;
        }
        return new ArrayList<>(components.get(componentId));
    }

    /**
     * İki düğümün aynı bileşende olup olmadığını kontrol eder
     * @param nodeId1 İlk düğüm ID'si
     * @param nodeId2 İkinci düğüm ID'si
     * @return Aynı bileşendeyse true
     */
    public boolean areInSameComponent(int nodeId1, int nodeId2) {
        Integer comp1 = nodeToComponentId.get(nodeId1);
        Integer comp2 = nodeToComponentId.get(nodeId2);
        return comp1 != null && comp2 != null && comp1.equals(comp2);
    }

    /**
     * Toplam bağlı bileşen sayısını getirir
     */
    public int getComponentCount() {
        return components.size();
    }

    /**
     * Tüm bağlı bileşenleri getirir
     */
    public List<List<UserNode>> getAllComponents() {
        return new ArrayList<>(components);
    }

    /**
     * Algoritmanın çalışma süresini getirir (milisaniye)
     */
    public long getExecutionTime() {
        return executionTime;
    }

    /**
     * Her bileşenin düğüm sayısını içeren bir map döndürür
     */
    public Map<Integer, Integer> getComponentSizes() {
        Map<Integer, Integer> sizes = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            sizes.put(i, components.get(i).size());
        }
        return sizes;
    }
}

