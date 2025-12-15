package model;

// Kullanıcı Node Sınıfı
public class UserNode {
    private int id;
    private String name;
    private double activeScore;      // Özellik I (Aktiflik)
    private double interactionScore; // Özellik II (Etkileşim)
    private double connectionScore;  // Özellik III (Bağl. Sayısı)

    // Görselleştirme koordinatlar (UI için)
    private int x, y;

    public UserNode(int id, double active, double interaction, double connection) {
        this.id = id;
        this.activeScore = active;
        this.interactionScore = interaction;
        this.connectionScore = connection;
    }

    // Getter metodları
    public int getId() { return id; }
    public double getActiveScore() { return activeScore; }
    public double getInteractionScore() { return interactionScore; }
    public double getConnectionScore() { return connectionScore; }

    @Override
    public String toString() {
        return "Node " + id;
    }
}