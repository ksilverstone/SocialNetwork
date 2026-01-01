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
    private List<UserNode> highlightedNodes; // Algoritma sonucu (Örn: BFS sonucu)
    private List<UserNode> pathOrder;        // Yol sırası (Çizgi çekmek için)

    public GraphPanel(SocialGraph graph) {
        this.graph = graph;
        // Başlangıçta boş listeler oluşturuyoruz ki null hatası almayalım
        this.highlightedNodes = new ArrayList<>();
        this.pathOrder = new ArrayList<>();
        this.setBackground(Color.WHITE);
    }

    // --- HATAYI ÇÖZEN KISIM BURASI ---
    public void setHighlightedPath(List<UserNode> path) {
        if (path == null) {
            // Eğer null gelirse (Sıfırla butonuna basılınca), listeleri boşalt ama YOK ETME.
            this.highlightedNodes = new ArrayList<>();
            this.pathOrder = new ArrayList<>();
        } else {
            this.highlightedNodes = path;
            this.pathOrder = path;
        }
        repaint(); // Ekrana "Tekrar Çiz" emri verir
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Çizim kalitesini artır
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Bağlantıları (Kenarları) Çiz
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(2));

        for (RelationshipEdge edge : graph.getAllEdges()) {
            UserNode u1 = edge.getSource();
            UserNode u2 = edge.getDestination();

            g2d.drawLine(u1.getX(), u1.getY(), u2.getX(), u2.getY());

            // Ağırlığı yaz
            int midX = (u1.getX() + u2.getX()) / 2;
            int midY = (u1.getY() + u2.getY()) / 2;

            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            g2d.drawString(String.format("%.1f", edge.getWeight()), midX, midY);

            g2d.setColor(Color.LIGHT_GRAY);
        }

        // 2. Algoritma Yolunu Çiz (Kırmızı Çizgi)
        // Null kontrolü ekliyoruz ama yukarıdaki düzeltme sayesinde artık null gelmeyecek
        if (pathOrder != null && pathOrder.size() > 1) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(4));

            for (int i = 0; i < pathOrder.size() - 1; i++) {
                UserNode n1 = pathOrder.get(i);
                UserNode n2 = pathOrder.get(i + 1);

                if(graph.hasEdge(n1.getId(), n2.getId()) || graph.hasEdge(n2.getId(), n1.getId())){
                    g2d.drawLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
                }
            }
        }

        // 3. Düğümleri Çiz
        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        for (UserNode node : graph.getAllNodes()) {

            // HighlightedNodes artık asla null olamaz, güvenle contains çağırabiliriz.
            if (highlightedNodes.contains(node)) {
                g2d.setColor(Color.ORANGE); // Seçili yol üzerindeyse Turuncu
            } else {
                g2d.setColor(node.getColor()); // Değilse kendi rengi
            }

            int radius = 15;
            int drawX = node.getX() - radius;
            int drawY = node.getY() - radius;

            g2d.fillOval(drawX, drawY, radius * 2, radius * 2);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(drawX, drawY, radius * 2, radius * 2);

            g2d.setColor(Color.BLACK);
            g2d.drawString("ID:" + node.getId(), drawX, drawY - 5);
        }
    }
}