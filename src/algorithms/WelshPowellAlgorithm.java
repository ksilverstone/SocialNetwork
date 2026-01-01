package algorithms;

import model.SocialGraph;
import model.UserNode;

import java.util.*;

public class WelshPowellAlgorithm {

    /**
     * Algoritmayı çalıştırır ve her düğüm için bir Renk ID'si (0, 1, 2...) döner.
     * @param graph Renklendirilecek graf
     * @return Düğüm -> Renk ID eşleşmesi
     */
    public Map<UserNode, Integer> execute(SocialGraph graph) {
        // Sonuç haritası: Hangi düğüm hangi renge boyandı?
        Map<UserNode, Integer> nodeColors = new HashMap<>();

        // 1. ADIM: Düğümleri derecelerine (komşu sayılarına) göre BÜYÜKTEN KÜÇÜĞE sırala
        List<UserNode> sortedNodes = new ArrayList<>(graph.getAllNodes());

        // Lambda ile sıralama: (b'nin komşu sayısı - a'nın komşu sayısı)
        sortedNodes.sort((a, b) ->
                graph.getNeighbors(b).size() - graph.getNeighbors(a).size()
        );

        int currentColorId = 0;

        // Tüm düğümler boyanana kadar devam et
        while (nodeColors.size() < sortedNodes.size()) {
            // Bu turda (bu renkte) boyananları geçici olarak tutalım
            // ki aynı turda birbirine komşu olanları boyamayalım.
            List<UserNode> coloredInThisRound = new ArrayList<>();

            for (UserNode node : sortedNodes) {
                // Eğer düğüm zaten boyanmışsa atla
                if (nodeColors.containsKey(node)) {
                    continue;
                }

                // Bu düğüm, şu anki renge (currentColorId) boyanabilir mi?
                // Kural: Şu anki renge sahip hiçbir komşusu olmamalı.
                if (canBeColored(node, coloredInThisRound, graph)) {
                    nodeColors.put(node, currentColorId);
                    coloredInThisRound.add(node);
                }
            }

            // Bir sonraki renk için ID'yi artır
            currentColorId++;
        }

        return nodeColors;
    }

    // Yardımcı Metot: Bir düğüm, o turdaki listeyle çakışıyor mu?
    private boolean canBeColored(UserNode node, List<UserNode> currentRoundNodes, SocialGraph graph) {
        for (UserNode coloredNode : currentRoundNodes) {
            // Eğer aday düğümümüz, o turda boyanmış herhangi bir düğümle komşuysa BOYAYAMAZSIN.
            if (graph.hasEdge(node.getId(), coloredNode.getId())) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return "Welsh-Powell Graph Coloring";
    }
}