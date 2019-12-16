package dangolawski.models;

import java.util.List;

public class ProductCollection {

    private List<String> products;

    private float support;

    private float confidence;

    private float lift;

    private int transactionsNumber;



    public ProductCollection(List<String> products) {
        this.products = products;
    }

    // SETTERS //

    public void setSupport(float support) {
        this.support = support;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public void setLift(float lift) {
        this.lift = lift;
    }

    public void setTransactionsNumber(int transactionsNumber) {
        this.transactionsNumber = transactionsNumber;
    }

    // GETTERS //

    public List<String> getProducts() {
        return products;
    }

    public float getSupport() {
        return support;
    }

    public float getConfidence() {
        return confidence;
    }

    public float getLift() {
        return lift;
    }

    public int getTransactionsNumber() {
        return transactionsNumber;
    }
}
