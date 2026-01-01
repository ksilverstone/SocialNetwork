package ui;

import algorithms.BFSAlgorithm;
import algorithms.DijkstraAlgorithm;
import algorithms.WelshPowellAlgorithm;
import model.SocialGraph;
import model.UserNode;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
    private SocialGraph graph;
    private GraphPanel graphPanel;

    public MainFrame() {
        // Pencere Ayarları
        setTitle("Sosyal Ağ Analizi - Proje 2 (Kişi 1: Algoritmalar)");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Veri Oluştur (Partnerin CSV kısmı bitene kadar Test Verisi)
        graph = new SocialGraph();
        createTestGraph();

        // 2. Çizim Panelini Ekle (Orta)
        graphPanel = new GraphPanel(graph);
        add(graphPanel, BorderLayout.CENTER);

        // 3. Kontrol Paneli (Alt veya Sol)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(Color.DARK_GRAY);

        // Butonlar
        JButton btnBFS = new JButton("BFS Başlat (0 -> ?)");
        JButton btnDijkstra = new JButton("Dijkstra (En Kısa Yol)");
        JButton btnColoring = new JButton("Welsh-Powell Renklendir");
        JButton btnReset = new JButton("Grafı Sıfırla");

        // Buton Tasarımları
        styleButton(btnBFS, Color.CYAN);
        styleButton(btnDijkstra, Color.ORANGE);
        styleButton(btnColoring, Color.MAGENTA);
        styleButton(btnReset, Color.WHITE);

        // --- AKSİYONLAR (Algoritmaların Tetiklendiği Yer) ---

        // 1. BFS Aksiyonu
        btnBFS.addActionListener(e -> {
            BFSAlgorithm bfs = new BFSAlgorithm();
            // Varsayılan olarak ID:0 olan düğümden başlasın
            UserNode startNode = graph.getNode(0);
            if(startNode != null) {
                List<UserNode> result = bfs.execute(graph, startNode, null);
                graphPanel.setHighlightedPath(result);
                JOptionPane.showMessageDialog(this, "BFS Gezinti Sırası:\n" + result);
            }
        });

        // 2. Dijkstra Aksiyonu
        btnDijkstra.addActionListener(e -> {
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
            // Örnek: Node 0'dan Node 4'e git
            UserNode start = graph.getNode(0);
            UserNode end = graph.getNode(4);

            if(start != null && end != null) {
                List<UserNode> path = dijkstra.execute(graph, start, end);
                if(path.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Yol bulunamadı!");
                } else {
                    graphPanel.setHighlightedPath(path);
                    JOptionPane.showMessageDialog(this, "En Kısa Yol:\n" + path);
                }
            }
        });

        // 3. Welsh-Powell Renklendirme Aksiyonu
        btnColoring.addActionListener(e -> {
            WelshPowellAlgorithm wp = new WelshPowellAlgorithm();
            Map<UserNode, Integer> colors = wp.execute(graph);

            // Renk Paleti (0. renk, 1. renk, 2. renk...)
            Color[] palette = {
                    Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                    Color.CYAN, Color.MAGENTA, Color.PINK, Color.ORANGE
            };

            // Her düğüme sonucuna göre renk ata
            for (Map.Entry<UserNode, Integer> entry : colors.entrySet()) {
                int colorIndex = entry.getValue() % palette.length; // Palet dışına taşarsa başa dön
                entry.getKey().setColor(palette[colorIndex]);
            }

            graphPanel.setHighlightedPath(null); // Çizgileri temizle, sadece renkler kalsın
            graphPanel.repaint(); // Ekranı yenile
        });

        // 4. Sıfırla
        btnReset.addActionListener(e -> {
            graphPanel.setHighlightedPath(null);
            for(UserNode node : graph.getAllNodes()) {
                node.setColor(Color.LIGHT_GRAY); // Eski haline döndür
            }
            graphPanel.repaint();
        });

        controlPanel.add(btnBFS);
        controlPanel.add(btnDijkstra);
        controlPanel.add(btnColoring);
        controlPanel.add(btnReset);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    // TEST VERİSİ OLUŞTURUCU
    private void createTestGraph() {
        // Düğümler
        UserNode n0 = new UserNode(0, 10, 5, 2); n0.setX(300); n0.setY(100);
        UserNode n1 = new UserNode(1, 20, 10, 5); n1.setX(100); n1.setY(250);
        UserNode n2 = new UserNode(2, 5, 2, 1);   n2.setX(500); n2.setY(250);
        UserNode n3 = new UserNode(3, 50, 40, 20); n3.setX(200); n3.setY(400);
        UserNode n4 = new UserNode(4, 15, 5, 3);   n4.setX(400); n4.setY(400);
        UserNode n5 = new UserNode(5, 30, 20, 10); n5.setX(300); n5.setY(550);

        graph.addNode(n0); graph.addNode(n1); graph.addNode(n2);
        graph.addNode(n3); graph.addNode(n4); graph.addNode(n5);

        // Kenarlar (Karmaşık bir yapı kuralım ki algoritmalar belli olsun)
        graph.addEdge(n0, n1);
        graph.addEdge(n0, n2);
        graph.addEdge(n1, n3);
        graph.addEdge(n2, n4);
        graph.addEdge(n3, n5);
        graph.addEdge(n4, n5);
        graph.addEdge(n3, n4); // Ortadan bağlantı
        // Welsh-Powell test etmek için n1 ve n2'yi bağlayalım (komşu olsunlar)
        graph.addEdge(n1, n2);
    }
}