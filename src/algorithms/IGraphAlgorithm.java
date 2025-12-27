package algorithms;

import model.SocialGraph; // Graph yerine SocialGraph kullanıyoruz
import model.UserNode;
import java.util.List;

public interface IGraphAlgorithm {
    // Algoritma SocialGraph türünde bir graf kabul etmeli
    List<UserNode> execute(SocialGraph graph, UserNode startNode, UserNode endNode);

    String getName();
}