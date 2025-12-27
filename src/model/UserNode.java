package model;

// Kullanıcı Node Sınıfı
public class UserNode {
    private int id;
    private String name; // İsteğe bağlı
    private double activeScore;      // Özellik I (Aktiflik)
    private double interactionScore; // Özellik II (Etkileşim)
    private double connectionScore;  // Özellik III (Bağl. Sayısı)

    // Görselleştirme koordinatları (UI için)
    private int x, y;

    public UserNode(int id, double active, double interaction, double connection) {
        this.id = id;
        this.activeScore = active;
        this.interactionScore = interaction;
        this.connectionScore = connection;
    }

    // --- EKLENEN KISIM BAŞLANGIÇ ---
    // UI (GraphPanel) bu metotları aradığı için hata veriyordu.
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    // --- EKLENEN KISIM BİTİŞ ---

    // Diğer Getter metodları
    public int getId() { return id; }
    public double getActiveScore() { return activeScore; }
    public double getInteractionScore() { return interactionScore; }
    public double getConnectionScore() { return connectionScore; }

    @Override
    public String toString() {
        return "Node " + id;
    }
}