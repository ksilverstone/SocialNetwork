package ui;

import algorithms.BFSAlgorithm;
import algorithms.WelshPowellAlgorithm;
import algorithms.DFS;
import algorithms.AStarAlgorithm;
import algorithms.ConnectedComponents;
import model.SocialGraph;
import model.UserNode;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
    private SocialGraph graph;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        // Pencere Ayarları
        setTitle("Sosyal Ağ Analizi - Kişi 1 & Kişi 2 Test Paneli");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Veri Oluştur
        graph = new SocialGraph();
        createTestGraph();

        // 2. Sekme Yapısı
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Kişi 1 Sekmesi
        JPanel person1Panel = createPerson1Panel();
        tabbedPane.addTab("Kişi 1", person1Panel);

        // Kişi 2 Sekmesi
        JPanel person2Panel = createPerson2Panel();
        tabbedPane.addTab("Kişi 2", person2Panel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPerson1Panel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Çizim Paneli
        GraphPanel graphPanel = new GraphPanel(graph);
        panel.add(graphPanel, BorderLayout.CENTER);

        // Kontrol Paneli
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(Color.DARK_GRAY);

        // Butonlar
        JButton btnBFS = new JButton("BFS Başlat (Gezinti)");
        JButton btnColoring = new JButton("Welsh-Powell (Renklendir)");
        JButton btnReset = new JButton("Grafı Sıfırla");

        styleButton(btnBFS, new Color(135, 206, 250)); // Açık Mavi
        styleButton(btnColoring, new Color(221, 160, 221)); // Erik Rengi (Plum)
        styleButton(btnReset, Color.LIGHT_GRAY);

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

        panel.add(controlPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createPerson2Panel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Çizim Paneli
        GraphPanel graphPanel = new GraphPanel(graph);
        panel.add(graphPanel, BorderLayout.CENTER);

        // Kontrol Paneli
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(Color.DARK_GRAY);

        // Butonlar
        JButton btnDFS = new JButton("DFS Başlat (Derinlik Öncelikli)");
        JButton btnAStar = new JButton("A* (En Kısa Yol)");
        JButton btnConnected = new JButton("Bağlı Bileşenler");
        JButton btnReset = new JButton("Grafı Sıfırla");

        styleButton(btnDFS, new Color(144, 238, 144)); // Açık Yeşil
        styleButton(btnAStar, new Color(255, 165, 0)); // Turuncu
        styleButton(btnConnected, new Color(176, 196, 222)); // Açık Gri-Mavi
        styleButton(btnReset, Color.LIGHT_GRAY);

        // 1. DFS Butonu
        btnDFS.addActionListener(e -> {
            DFS dfs = new DFS(graph);
            UserNode startNode = graph.getNode(0);
            if(startNode != null) {
                List<UserNode> result = dfs.findReachableNodes(startNode.getId());
                graphPanel.setHighlightedPath(result);
                JOptionPane.showMessageDialog(this, 
                    "DFS Sırası (Derinlik Öncelikli):\n" + result + 
                    "\n\nÇalışma Süresi: " + dfs.getExecutionTime() + " ms" +
                    "\nZiyaret Edilen: " + dfs.getVisitedCount() + " düğüm");
            }
        });

        // 2. A* Butonu
        btnAStar.addActionListener(e -> {
            // Başlangıç ve hedef düğüm seçimi için dialog
            String startInput = JOptionPane.showInputDialog(this, "Başlangıç düğüm ID'si girin:", "0");
            String endInput = JOptionPane.showInputDialog(this, "Hedef düğüm ID'si girin:", "5");
            
            try {
                int startId = Integer.parseInt(startInput);
                int endId = Integer.parseInt(endInput);
                
                UserNode startNode = graph.getNode(startId);
                UserNode endNode = graph.getNode(endId);
                
                if(startNode == null || endNode == null) {
                    JOptionPane.showMessageDialog(this, "Geçersiz düğüm ID'si!");
                    return;
                }
                
                AStarAlgorithm aStar = new AStarAlgorithm();
                List<UserNode> result = aStar.execute(graph, startNode, endNode);
                
                if(result.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Yol bulunamadı!");
                } else {
                    graphPanel.setHighlightedPath(result);
                    JOptionPane.showMessageDialog(this, 
                        "A* En Kısa Yol:\n" + result + 
                        "\n\nÇalışma Süresi: " + aStar.getExecutionTime() + " ms" +
                        "\nYol Uzunluğu: " + result.size() + " düğüm");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Geçersiz giriş! Lütfen sayı girin.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
            }
        });

        // 3. Bağlı Bileşenler Butonu
        btnConnected.addActionListener(e -> {
            ConnectedComponents cc = new ConnectedComponents(graph);
            List<List<UserNode>> components = cc.findConnectedComponents();
            
            // Her bileşeni farklı renkle göster
            Color[] palette = {
                    Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                    Color.CYAN, Color.MAGENTA, Color.PINK, Color.ORANGE
            };
            
            for(int i = 0; i < components.size(); i++) {
                Color compColor = palette[i % palette.length];
                for(UserNode node : components.get(i)) {
                    node.setColor(compColor);
                }
            }
            
            graphPanel.setHighlightedPath(null);
            graphPanel.repaint();
            
            StringBuilder message = new StringBuilder();
            message.append("Bağlı Bileşenler Bulundu!\n\n");
            message.append("Toplam Bileşen Sayısı: ").append(components.size()).append("\n");
            message.append("Çalışma Süresi: ").append(cc.getExecutionTime()).append(" ms\n\n");
            
            for(int i = 0; i < components.size(); i++) {
                message.append("Bileşen ").append(i + 1).append(": ").append(components.get(i)).append("\n");
            }
            
            JOptionPane.showMessageDialog(this, message.toString());
        });

        // 4. Sıfırla
        btnReset.addActionListener(e -> {
            graphPanel.setHighlightedPath(null);
            for(UserNode node : graph.getAllNodes()) {
                node.setColor(Color.LIGHT_GRAY);
            }
            graphPanel.repaint();
        });

        controlPanel.add(btnDFS);
        controlPanel.add(btnAStar);
        controlPanel.add(btnConnected);
        controlPanel.add(btnReset);

        panel.add(controlPanel, BorderLayout.SOUTH);
        return panel;
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