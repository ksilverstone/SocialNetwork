package model;

import java.awt.Color; // Renk kütüphanesi eklendi

// Kullanıcı (Düğüm) Sınıfı
public class UserNode {
    private int id;
    private String name; // İsteğe bağlı
    private double activeScore;      // Özellik I (Aktiflik)
    private double interactionScore; // Özellik II (Etkileşim)
    private double connectionScore;  // Özellik III (Bağl. Sayısı)

    // Görselleştirme koordinatları (UI için)
    private int x, y;

    // Görselleştirme rengi (Varsayılan: Açık Gri)
    // Welsh-Powell algoritması bu rengi değiştirecek.
    private Color color = Color.LIGHT_GRAY;

    public UserNode(int id, double active, double interaction, double connection) {
        this.id = id;
        this.activeScore = active;
        this.interactionScore = interaction;
        this.connectionScore = connection;
    }

    // --- UI KOORDİNAT METOTLARI ---
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    // --- RENK METOTLARI (YENİ EKLENEN KISIM) ---
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    // --- TEMEL GETTER METOTLARI ---
    public int getId() { return id; }
    public double getActiveScore() { return activeScore; }
    public double getInteractionScore() { return interactionScore; }
    public double getConnectionScore() { return connectionScore; }

    @Override
    public String toString() {
        return "Node " + id;
    }
}