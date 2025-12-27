package ui;

import model.RelationshipEdge;
import model.SocialGraph;
import model.UserNode;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class GraphPanel extends JPanel {
    private SocialGraph graph;
    private List<UserNode> highlightedNodes; // Algoritma sonucu boyanacak düğümler
    private List<UserNode> pathOrder;        // Yol sırası (Çizgi çekmek için)

    public GraphPanel(SocialGraph graph) {
        this.graph = graph;
        this.highlightedNodes = new ArrayList<>();
        this.pathOrder = new ArrayList<>();
        this.setBackground(Color.WHITE); // Arka plan beyaz
    }

    // Algoritma çalışınca sonucu buraya gönderip ekranı yenileyeceğiz
    public void setHighlightedPath(List<UserNode> path) {
        this.highlightedNodes = path;
        this.pathOrder = path;
        repaint(); // Ekrana "Tekrar Çiz" emri verir
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Önce Bağlantıları (Kenarları) Çiz
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(2)); // İnce çizgi

        for (RelationshipEdge edge : graph.getAllEdges()) {
            UserNode u1 = edge.getSource();
            UserNode u2 = edge.getDestination();
            g2d.drawLine(u1.getX(), u1.getY(), u2.getX(), u2.getY());

            // Ağırlığı çizginin ortasına yaz (Opsiyonel)
            int midX = (u1.getX() + u2.getX()) / 2;
            int midY = (u1.getY() + u2.getY()) / 2;
            g2d.setColor(Color.GRAY);
            g2d.drawString(String.format("%.1f", edge.getWeight()), midX, midY);
            g2d.setColor(Color.LIGHT_GRAY); // Rengi geri al
        }

        // 2. Varsa Algoritma Yolunu (Path) Farklı Renkle Üstüne Çiz
        if (pathOrder != null && pathOrder.size() > 1) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(4)); // Kalın çizgi
            for (int i = 0; i < pathOrder.size() - 1; i++) {
                UserNode n1 = pathOrder.get(i);
                UserNode n2 = pathOrder.get(i + 1);
                // Eğer bu iki düğüm arasında gerçekten kenar varsa çiz
                if(graph.hasEdge(n1.getId(), n2.getId()) || graph.hasEdge(n2.getId(), n1.getId())){
                    g2d.drawLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
                }
            }
        }

        // 3. Düğümleri Çiz
        for (UserNode node : graph.getAllNodes()) {
            // Eğer düğüm highlight listesindeyse Kırmızı, değilse Mavi yap
            if (highlightedNodes.contains(node)) {
                g2d.setColor(Color.ORANGE);
            } else {
                g2d.setColor(new Color(100, 149, 237)); // Cornflower Blue
            }

            // Daire çiz (Merkezi x,y olacak şekilde)
            int radius = 15;
            int drawX = node.getX() - radius;
            int drawY = node.getY() - radius;

            g2d.fillOval(drawX, drawY, radius * 2, radius * 2);

            // Çerçeve
            g2d.setColor(Color.BLACK);
            g2d.drawOval(drawX, drawY, radius * 2, radius * 2);

            // ID Yaz
            g2d.setColor(Color.BLACK);
            g2d.drawString("ID:" + node.getId(), drawX, drawY - 5);
        }
    }
}