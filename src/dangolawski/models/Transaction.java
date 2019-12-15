package dangolawski.models;

import java.util.List;
import java.util.Set;

public class Transaction {

    private Set<String> productList;

    public void setProductList(Set<String> productList) {
        this.productList = productList;
    }

    public boolean productsInTransaction(List<String> collection) {
        return productList.containsAll(collection);
    }

    public int getNumberOfProducts() {
        return productList.size();
    }

    public Set<String> getProductList() {
        return this.productList;
    }

}
