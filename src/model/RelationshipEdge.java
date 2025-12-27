package model;

public class RelationshipEdge {
    private UserNode source;
    private UserNode destination;
    private double weight; // Dinamik hesaplanan ağırlık

    public RelationshipEdge(UserNode source, UserNode destination) {
        this.source = source;
        this.destination = destination;
        this.weight = calculateDynamicWeight();
    }

    //Dinamik Ağırlık Hesaplama
    // Formül: 1 + Sqrt((DiffAktiflik^2) + (DiffEtkilesim^2) + (DiffBaglanti^2))
    private double calculateDynamicWeight() {
        double diffActive = source.getActiveScore() - destination.getActiveScore();
        double diffInteraction = source.getInteractionScore() - destination.getInteractionScore();
        double diffConnection = source.getConnectionScore() - destination.getConnectionScore();

        double sumOfSquares = (diffActive * diffActive) +
                (diffInteraction * diffInteraction) +
                (diffConnection * diffConnection);

        return 1.0 + Math.sqrt(sumOfSquares);
    }

    public double getWeight() { return weight; }
    public UserNode getSource() { return source; }
    public UserNode getDestination() { return destination; }
}