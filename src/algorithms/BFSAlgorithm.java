package algorithms;

import model.SocialGraph; // DÜZELTME: Graph değil SocialGraph
import model.UserNode;
// RelationshipEdge importuna gerek kalmadı ama dursa da zarar gelmez
import java.util.*;

public class BFSAlgorithm implements IGraphAlgorithm {

    @Override
    public List<UserNode> execute(SocialGraph graph, UserNode startNode, UserNode endNode) {
        // Ziyaret sırasını tutacağımız liste
        List<UserNode> visitedOrder = new ArrayList<>();

        // Kuyruk (FIFO)
        Queue<UserNode> queue = new LinkedList<>();

        // Ziyaret edilenlerin ID seti
        Set<Integer> visitedIds = new HashSet<>();

        // Başlangıç
        queue.add(startNode);
        visitedIds.add(startNode.getId());

        while (!queue.isEmpty()) {
            UserNode current = queue.poll();
            visitedOrder.add(current);

            // Hedef kontrolü
            if (endNode != null && current.getId() == endNode.getId()) {
                break;
            }

            // Komşuları al (Artık SocialGraph içinde bu metot var)
            List<UserNode> neighbors = graph.getNeighbors(current);

            for (UserNode neighbor : neighbors) {
                if (!visitedIds.contains(neighbor.getId())) {
                    visitedIds.add(neighbor.getId());
                    queue.add(neighbor);
                }
            }
        }

        return visitedOrder;
    }

    @Override
    public String getName() {
        return "Breadth-First Search (BFS)";
    }
}