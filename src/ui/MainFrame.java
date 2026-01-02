package ui;

import algorithms.*;
import model.SocialGraph;
import model.UserNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
    private SocialGraph graph;
    private GraphPanel graphPanel;

    public MainFrame() {
        // --- PENCERE AYARLARI ---
        setTitle("Sosyal Ağ Analizi - Proje 2 (Gelişmiş Arayüz)");
        setSize(1200, 850); // Pencereyi daha büyük başlatalım
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Veri Oluştur
        graph = new SocialGraph();
        createTestGraph();

        // 2. Çizim Panelini Ekle (Orta Kısım)
        // Panelin etrafına hafif boşluk bırakalım ki çizimler kenara yapışmasın
        graphPanel = new GraphPanel(graph);
        JPanel graphContainer = new JPanel(new BorderLayout());
        graphContainer.setBorder(new EmptyBorder(10, 10, 10, 10)); // Çerçeve boşluğu
        graphContainer.add(graphPanel, BorderLayout.CENTER);
        add(graphContainer, BorderLayout.CENTER);

        // 3. Kontrol Paneli (Alt Kısım)
        JPanel controlPanel = new JPanel();
        // 2 Satır, 4 Sütun, 20px yatay ve dikey boşluk (Daha ferah)
        controlPanel.setLayout(new GridLayout(2, 4, 20, 20));
        controlPanel.setBackground(new Color(45, 45, 45)); // Koyu Gri Modern Arkaplan
        controlPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Dıştan 20px boşluk

        // --- BUTONLARI OLUŞTUR ---
        // Buton metinlerini daha kısa ve net yapalım
        JButton btnBFS = new JButton("BFS Gezinti");
        JButton btnDFS = new JButton("DFS Derinlik");
        JButton btnDijkstra = new JButton("Dijkstra (En Kısa)");
        JButton btnAStar = new JButton("A* (En Kısa)");
        JButton btnColoring = new JButton("Welsh-Powell");
        JButton btnConnected = new JButton("Bağlı Bileşenler");
        JButton btnReset = new JButton("SIFIRLA");
        JButton btnExit = new JButton("ÇIKIŞ");

        // --- BUTON RENKLENDİRMELERİ (Pastel ve Modern Tonlar) ---
        styleModernButton(btnBFS, new Color(52, 152, 219));      // Peter River (Mavi)
        styleModernButton(btnDFS, new Color(46, 204, 113));      // Emerald (Yeşil)
        styleModernButton(btnDijkstra, new Color(230, 126, 34)); // Carrot (Turuncu)
        styleModernButton(btnAStar, new Color(241, 196, 15));    // Sun Flower (Sarı)
        styleModernButton(btnColoring, new Color(155, 89, 182)); // Amethyst (Mor)
        styleModernButton(btnConnected, new Color(26, 188, 156));// Turquoise
        styleModernButton(btnReset, new Color(149, 165, 166));   // Concrete (Gri)
        styleModernButton(btnExit, new Color(231, 76, 60));      // Alizarin (Kırmızı)

        // --- AKSİYONLAR ---

        // 1. BFS Butonu
        btnBFS.addActionListener(e -> {
            BFSAlgorithm bfs = new BFSAlgorithm();
            UserNode startNode = graph.getNode(0);
            if(startNode != null) {
                List<UserNode> result = bfs.execute(graph, startNode, null);
                graphPanel.setHighlightedPath(result);
                showMessage("BFS Tamamlandı", "Ziyaret Sırası:\n" + result);
            }
        });

        // 2. DFS Butonu
        btnDFS.addActionListener(e -> {
            DFS dfs = new DFS(graph);
            UserNode startNode = graph.getNode(0);
            if(startNode != null) {
                List<UserNode> result = dfs.findReachableNodes(startNode.getId());
                graphPanel.setHighlightedPath(result);
                showMessage("DFS Tamamlandı", "Ziyaret Sırası:\n" + result);
            }
        });

        // 3. Dijkstra Butonu
        btnDijkstra.addActionListener(e -> {
            String startInput = JOptionPane.showInputDialog(this, "Başlangıç ID:", "0");
            String endInput = JOptionPane.showInputDialog(this, "Hedef ID:", "4");

            try {
                if (startInput != null && endInput != null) {
                    int startId = Integer.parseInt(startInput);
                    int endId = Integer.parseInt(endInput);
                    UserNode startNode = graph.getNode(startId);
                    UserNode endNode = graph.getNode(endId);

                    if (startNode != null && endNode != null) {
                        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
                        List<UserNode> path = dijkstra.execute(graph, startNode, endNode);

                        if (path.isEmpty()) showMessage("Bilgi", "Yol bulunamadı!");
                        else {
                            graphPanel.setHighlightedPath(path);
                            showMessage("Dijkstra Sonucu", "En Kısa Yol:\n" + path +
                                    "\n(Süre: " + dijkstra.getExecutionTime() + " ms)");
                        }
                    } else showMessage("Hata", "Düğüm bulunamadı.");
                }
            } catch (Exception ex) { showMessage("Hata", "Lütfen sayı girin."); }
        });

        // 4. A* Butonu
        btnAStar.addActionListener(e -> {
            String startInput = JOptionPane.showInputDialog(this, "Başlangıç ID:", "0");
            String endInput = JOptionPane.showInputDialog(this, "Hedef ID:", "5");
            try {
                if (startInput != null && endInput != null) {
                    int startId = Integer.parseInt(startInput);
                    int endId = Integer.parseInt(endInput);
                    UserNode s = graph.getNode(startId);
                    UserNode en = graph.getNode(endId);

                    if (s != null && en != null) {
                        AStarAlgorithm aStar = new AStarAlgorithm();
                        List<UserNode> path = aStar.execute(graph, s, en);
                        if (path.isEmpty()) showMessage("Bilgi", "Yol yok!");
                        else {
                            graphPanel.setHighlightedPath(path);
                            showMessage("A* Sonucu", "En Kısa Yol:\n" + path);
                        }
                    }
                }
            } catch (Exception ex) { showMessage("Hata", "Hata: " + ex.getMessage()); }
        });

        // 5. Welsh-Powell Butonu
        btnColoring.addActionListener(e -> {
            WelshPowellAlgorithm wp = new WelshPowellAlgorithm();
            Map<UserNode, Integer> colors = wp.execute(graph);

            Color[] palette = {
                    Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                    Color.CYAN, Color.MAGENTA, Color.PINK, Color.ORANGE
            };

            for (Map.Entry<UserNode, Integer> entry : colors.entrySet()) {
                int colorIndex = entry.getValue() % palette.length;
                entry.getKey().setColor(palette[colorIndex]);
            }
            graphPanel.setHighlightedPath(null);
            graphPanel.repaint();
            showMessage("Başarılı", "Graf renklendirildi. Komşular farklı renkte!");
        });

        // 6. Bağlı Bileşenler
        btnConnected.addActionListener(e -> {
            ConnectedComponents cc = new ConnectedComponents(graph);
            List<List<UserNode>> components = cc.findConnectedComponents();

            Color[] palette = { Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA };
            for(int i = 0; i < components.size(); i++) {
                Color c = palette[i % palette.length];
                for(UserNode n : components.get(i)) n.setColor(c);
            }
            graphPanel.setHighlightedPath(null);
            graphPanel.repaint();
            showMessage("Sonuç", "Toplam " + components.size() + " ayrık topluluk bulundu.");
        });

        // 7. Sıfırla
        btnReset.addActionListener(e -> {
            graphPanel.setHighlightedPath(null);
            for(UserNode node : graph.getAllNodes()) {
                node.setColor(Color.LIGHT_GRAY);
            }
            graphPanel.repaint();
        });

        // 8. Çıkış
        btnExit.addActionListener(e -> System.exit(0));

        // Panellere Ekleme
        controlPanel.add(btnBFS);
        controlPanel.add(btnDFS);
        controlPanel.add(btnDijkstra);
        controlPanel.add(btnAStar);
        controlPanel.add(btnColoring);
        controlPanel.add(btnConnected);
        controlPanel.add(btnReset);
        controlPanel.add(btnExit);

        add(controlPanel, BorderLayout.SOUTH);
    }

    // --- MODERN BUTON TASARIMI ---
    private void styleModernButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE); // Yazı rengi beyaz (Koyu arkaplan üstüne)
        btn.setFocusPainted(false);
        // Fontu büyüttük ve Modern (Segoe UI veya SansSerif) yaptık
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Kenarlık ve İç Boşluk (Padding) - Butonu şişkin gösterir
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Mouse üzerine gelince el işareti çıksın
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Yardımcı Mesaj Gösterme Metodu
    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void createTestGraph() {
        UserNode n0 = new UserNode(0, 10, 5, 2); n0.setX(300); n0.setY(100);
        UserNode n1 = new UserNode(1, 20, 10, 5); n1.setX(100); n1.setY(250);
        UserNode n2 = new UserNode(2, 5, 2, 1);   n2.setX(500); n2.setY(250);
        UserNode n3 = new UserNode(3, 50, 40, 20); n3.setX(200); n3.setY(400);
        UserNode n4 = new UserNode(4, 15, 5, 3);   n4.setX(400); n4.setY(400);
        UserNode n5 = new UserNode(5, 30, 20, 10); n5.setX(300); n5.setY(550);

        graph.addNode(n0); graph.addNode(n1); graph.addNode(n2);
        graph.addNode(n3); graph.addNode(n4); graph.addNode(n5);

        try {
            graph.addEdge(n0, n1); graph.addEdge(n0, n2);
            graph.addEdge(n1, n3); graph.addEdge(n2, n4);
            graph.addEdge(n3, n5); graph.addEdge(n4, n5);
            graph.addEdge(n3, n4); graph.addEdge(n1, n2);
        } catch (Exception e) { System.out.println("Hata: " + e.getMessage()); }
    }
}