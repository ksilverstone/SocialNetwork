package ui;

import algorithms.BFSAlgorithm;
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
        setTitle("Sosyal Ağ Analizi - Kişi 1 Test Paneli");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Veri Oluştur
        graph = new SocialGraph();
        createTestGraph();

        // 2. Çizim Paneli
        graphPanel = new GraphPanel(graph);
        add(graphPanel, BorderLayout.CENTER);

        // 3. Kontrol Paneli
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(Color.DARK_GRAY);

        // Butonlar
        JButton btnBFS = new JButton("BFS Başlat (Gezinti)");
        JButton btnColoring = new JButton("Welsh-Powell (Renklendir)");
        JButton btnReset = new JButton("Grafı Sıfırla");

        // --- TASARIM DÜZELTMESİ BURADA ---
        // Yazı renklerini SİYAH (Black) yaptık ki okunsun.
        styleButton(btnBFS, new Color(135, 206, 250)); // Açık Mavi
        styleButton(btnColoring, new Color(221, 160, 221)); // Erik Rengi (Plum)
        styleButton(btnReset, Color.LIGHT_GRAY);

        // --- AKSİYONLAR ---

        // 1. BFS Butonu
        btnBFS.addActionListener(e -> {
            BFSAlgorithm bfs = new BFSAlgorithm();
            UserNode startNode = graph.getNode(0);
            if(startNode != null) {
                List<UserNode> result = bfs.execute(graph, startNode, null);
                graphPanel.setHighlightedPath(result);
                JOptionPane.showMessageDialog(this, "BFS Sırası (Dalga Dalga Yayılım):\n" + result);
            }
        });

        // 2. Welsh-Powell Butonu
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
            JOptionPane.showMessageDialog(this, "Graf renklendirildi!\nKural: Komşu olanlar aynı renk olamaz.");
        });

        // 3. Sıfırla
        btnReset.addActionListener(e -> {
            graphPanel.setHighlightedPath(null);
            for(UserNode node : graph.getAllNodes()) {
                node.setColor(Color.LIGHT_GRAY);
            }
            graphPanel.repaint();
        });

        controlPanel.add(btnBFS);
        controlPanel.add(btnColoring);
        controlPanel.add(btnReset);

        add(controlPanel, BorderLayout.SOUTH);
    }

    // Buton Tasarım Metodu (Okunabilirlik için güncellendi)
    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK); // YAZI RENGİ SİYAH OLDU
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14)); // Font büyütüldü
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btn.setPreferredSize(new Dimension(200, 40));
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