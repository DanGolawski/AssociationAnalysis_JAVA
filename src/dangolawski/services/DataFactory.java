package dangolawski.services;

import dangolawski.models.ProductCollection;
import dangolawski.models.Relationship;
import dangolawski.models.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataFactory {

    public void getData() throws IOException {
        getTransactions();
        createOneElementProductCollectionArray();
        createTwoElementProductCollectionArray();
    }

    private void getTransactions() throws IOException {
        DataContainer.transactionList = new ArrayList<>();
        Set<String> productNames = new TreeSet<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(DataContainer.csvFilePath));
        String row;
        String currentTransactionNum = "1";
        csvReader.readLine();
        Set<String> currentProducts = new TreeSet<>();
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            if(!data[3].equals("NONE")){
                productNames.add(data[3]);
                if(data[2].equals(currentTransactionNum)) {
                    currentProducts.add(data[3]);
                }
                else {
                    createAndAddNewTransaction(currentProducts);
                    currentProducts = new TreeSet<>();
                    currentProducts.add(data[3]);
                    currentTransactionNum = data[2];
                }
            }

        }

        DataContainer.productNames = new ArrayList<>();
        DataContainer.productNames.addAll(productNames);
        csvReader.close();
    }

    private void createAndAddNewTransaction(Set<String> products) {
        Transaction transaction = new Transaction();
        transaction.setProductList(products);
        DataContainer.transactionList.add(transaction);
    }

    private void createOneElementProductCollectionArray() {
        DataContainer.oneElementProductCollectionList = new ArrayList<>();
        for(String product : DataContainer.productNames) {
            ArrayList<String> productName = new ArrayList<>();
            productName.add(product);
            ProductCollection productCollection = new ProductCollection(productName);
            float supportFrequency = StatisticalCalculator.calculateSupport(productName);
            productCollection.setSupport(supportFrequency);
            productCollection.setFrequency(supportFrequency);
            productCollection.setTransactionsNumber(StatisticalCalculator.calculateTransactionsNumber(productName));
            DataContainer.oneElementProductCollectionList.add(productCollection);
        }
    }

    private void createTwoElementProductCollectionArray() {
        DataContainer.twoElementProductCollectionList = new ArrayList<>();
        for(int i = 0; i < DataContainer.productNames.size() - 1; i++) {
            for(int j = i+1; j < DataContainer.productNames.size(); j++) {
                ArrayList<String> products = new ArrayList<>();
                products.add(DataContainer.productNames.get(i));
                products.add(DataContainer.productNames.get(j));
                ProductCollection productCollection = new ProductCollection(products);
                productCollection.setSupport(StatisticalCalculator.calculateSupport(products));
                productCollection.setTransactionsNumber(StatisticalCalculator.calculateTransactionsNumber(products));
                DataContainer.twoElementProductCollectionList.add(productCollection);
            }
        }
    }

    public static void createThreeElementProductCollectionArray() {
        List<List<String>> nonFrequentProducts = getNonFrequentProducts();
        DataContainer.threeElementProductCollectionList = new ArrayList<>();
        for(int a = 0; a < DataContainer.productNames.size(); a++) {
            for(int b = a; b < DataContainer.productNames.size(); b++) {
                for(int c = b; c < DataContainer.productNames.size(); c++) {
                    ArrayList<String> products = new ArrayList<>();
                    products.add(DataContainer.productNames.get(a));
                    products.add(DataContainer.productNames.get(b));
                    products.add(DataContainer.productNames.get(c));
                    if(!nonFrequentProducts.containsAll(products)){
                        ProductCollection newProductCollection = new ProductCollection(products);
                        newProductCollection.setSupport(StatisticalCalculator.calculateSupport(products));
                        newProductCollection.setTransactionsNumber(StatisticalCalculator.calculateTransactionsNumber(products));
                        DataContainer.threeElementProductCollectionList.add(newProductCollection);
                    }
                }
            }
        }
    }

    private static List<List<String>> getNonFrequentProducts(){
        List<List<String>> nonFrequentProducts = new ArrayList<>();
        System.out.println(DataContainer.nonFrequentOneElemCollections);
        for(ProductCollection productCollection : DataContainer.nonFrequentOneElemCollections){
            nonFrequentProducts.add(productCollection.getProducts());
        }
        for(ProductCollection productCollection : DataContainer.nonFrequentTwoElemCollections){
            nonFrequentProducts.add(productCollection.getProducts());
            nonFrequentProducts.add(productCollection.getProducts());
        }
        return nonFrequentProducts;
    }

    private static boolean checkWhetherContains(ArrayList<String> productNames){
        boolean contains = false;
        for(ProductCollection productCollection : DataContainer.frequentTwoElemCollections){
            if(productNames.containsAll(productCollection.getProducts())){
                return true;
            }
        }
        return false;
    }

    public static void createListOfFrequentOneElementCollections() {
        DataContainer.frequentOneElemCollections = new ArrayList<>();
        DataContainer.nonFrequentOneElemCollections = new ArrayList<>();
        DataContainer.frequentOneElemCollections = StatisticalCalculator.getFrequentOneElementCollections(DataContainer.minSupport);
        DataContainer.nonFrequentOneElemCollections = StatisticalCalculator.getNonFrequentOneElementCollections(DataContainer.minSupport);
    }

    public static void createListOfFrequentTwoElementCollections() {
        DataContainer.frequentTwoElemCollections = new ArrayList<>();
        DataContainer.nonFrequentTwoElemCollections = new ArrayList<>();
        DataContainer.frequentTwoElemCollections = StatisticalCalculator.getFrequentAndNonFrequentTwoElementCollections(DataContainer.nonFrequentOneElemCollections, DataContainer.minSupport);
    }

    public static void createTwoElementRelationshipsBasedOnFrequentLists() {
        DataContainer.twoElementRelationshipList = new ArrayList<>();
        for(ProductCollection productCollection : DataContainer.frequentTwoElemCollections) {
            Relationship relationship = new Relationship(productCollection.getProducts());
            List<String> reversedProducts = reverseList(productCollection.getProducts());
            Relationship relationshipReversed = new Relationship(reversedProducts);
            setRelationshipAttributesAndAddToList(relationship, productCollection.getProducts());
            setRelationshipAttributesAndAddToList(relationshipReversed, reversedProducts);
        }

    }

    public static List<String> reverseList(List<String> products) {
        List<String> reversedList = new ArrayList<>();
        for(int i = products.size()-1; i >= 0; i--) {
            reversedList.add(products.get(i));
        }
        return reversedList;
    }

    private static void setRelationshipAttributesAndAddToList(Relationship relationship, List<String> products) {
        relationship.setSupport(StatisticalCalculator.calculateSupport(products));
        relationship.setConfidence(StatisticalCalculator.calculateConfidence(products));
        relationship.setLift(StatisticalCalculator.calculateLift(products));
        DataContainer.twoElementRelationshipList.add(relationship);
    }

    public void createThreeElementRelationshipsBasedOnFrequentLists() {

    }
}
