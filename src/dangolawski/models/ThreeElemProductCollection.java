package dangolawski.models;

import java.util.List;

public class ThreeElemProductCollection {

    private List<String> products;

    private float support;

    private float confidenceA_BC;

    private float confidenceAB_C;

    private float liftA_BC;

    private float liftAB_C;

    private int transactionsNumber;

    public ThreeElemProductCollection(List<String> products) {
        this.products = products;
    }

    // SETTERS //

    public void setSupport(float support) {
        this.support = support;
    }

    public void setConfidenceA_BC(float confidenceA_BC) {
        this.confidenceA_BC = confidenceA_BC;
    }

    public void setConfidenceAB_C(float confidenceAB_C) {
        this.confidenceAB_C = confidenceAB_C;
    }

    public void setLiftA_BC(float liftA_BC) {
        this.liftA_BC = liftA_BC;
    }

    public void setLiftAB_C(float liftAB_C) {
        this.liftAB_C = liftAB_C;
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

    public float getConfidenceA_BC() {
        return confidenceA_BC;
    }

    public float getConfidenceAB_C() {
        return confidenceAB_C;
    }

    public float getLiftA_BC() {
        return liftA_BC;
    }

    public float getLiftAB_C() {
        return liftAB_C;
    }

    public int getTransactionsNumber() {
        return transactionsNumber;
    }
}
