package dangolawski.services;

import dangolawski.models.ProductCollection;
import dangolawski.models.Relationship;
import dangolawski.models.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataFactory {

    public static void getTransactionsAndProductNames() throws IOException {
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
        createAndAddNewTransaction(currentProducts);


        DataContainer.productNames = new ArrayList<>();
        DataContainer.productNames.addAll(productNames);
        csvReader.close();
    }

    private static void createAndAddNewTransaction(Set<String> products) {
        Transaction transaction = new Transaction();
        transaction.setProductList(products);
        DataContainer.transactionList.add(transaction);
    }

    public static void createOneElementProductCollectionList() {
        DataContainer.oneElementProductCollectionList = new ArrayList<>();
        for(String product : DataContainer.productNames) {
            DataContainer.oneElementProductCollectionList.add(createNewProductCollection(new ArrayList<>(Arrays.asList(product))));
        }
    }

    public static void createTwoElementProductCollectionList() {
        DataContainer.twoElementProductCollectionList = new ArrayList<>();
        for(String productX : DataContainer.frequentOneElemCollectionProducts) {
            for(String productY : DataContainer.frequentOneElemCollectionProducts) {
                if(productX.equals(productY)){ continue; }
                DataContainer.twoElementProductCollectionList.add(createNewProductCollection(new ArrayList<>(Arrays.asList(productX, productY))));
            }
        }
    }

    private static ProductCollection createNewProductCollection(ArrayList<String> products) {
        ProductCollection productCollection = new ProductCollection(products);
        productCollection.setSupport(StatisticalCalculator.calculateSupport(products));
        productCollection.setConfidence(StatisticalCalculator.calculateConfidence(products));
        productCollection.setLift(StatisticalCalculator.calculateLift(products));
        productCollection.setTransactionsNumber(StatisticalCalculator.countTransactions(products));
        return productCollection;
    }

    public static void getFrequentOneElementCollectionList() {
        DataContainer.frequentOneElemCollectionList = DataContainer.oneElementProductCollectionList.stream().filter(collection -> collection.getSupport() >= DataContainer.minSupport).collect(Collectors.toList());
        getFrequentProducts();
    }

    public static void getFrequentTwoElementCollectionList() {
        
    }

    private static void getFrequentProducts(){
        DataContainer.frequentOneElemCollectionProducts = new TreeSet<>();
        DataContainer.frequentOneElemCollectionList.forEach(collection -> {
            collection.getProducts().forEach(productName -> DataContainer.frequentOneElemCollectionProducts.add(productName));
        });
    }



//    public static void createThreeElementProductCollectionArray() {
//        List<List<String>> nonFrequentProducts = getNonFrequentProducts();
//        DataContainer.threeElementProductCollectionList = new ArrayList<>();
//        for(int a = 0; a < DataContainer.productNames.size(); a++) {
//            for(int b = a; b < DataContainer.productNames.size(); b++) {
//                for(int c = b; c < DataContainer.productNames.size(); c++) {
//                    ArrayList<String> products = new ArrayList<>();
//                    products.add(DataContainer.productNames.get(a));
//                    products.add(DataContainer.productNames.get(b));
//                    products.add(DataContainer.productNames.get(c));
//                    if(!nonFrequentProducts.containsAll(products)){
//                        ProductCollection newProductCollection = new ProductCollection(products);
//                        newProductCollection.setSupport(StatisticalCalculator.calculateSupport(products));
//                        newProductCollection.setTransactionsNumber(StatisticalCalculator.countTransactions(products));
//                        DataContainer.threeElementProductCollectionList.add(newProductCollection);
//                    }
//                }
//            }
//        }
//    }

//    private static List<List<String>> getNonFrequentProducts(){
//        List<List<String>> nonFrequentProducts = new ArrayList<>();
//        System.out.println(DataContainer.nonFrequentOneElemCollections);
//        for(ProductCollection productCollection : DataContainer.nonFrequentOneElemCollections){
//            nonFrequentProducts.add(productCollection.getProducts());
//        }
//        for(ProductCollection productCollection : DataContainer.nonFrequentTwoElemCollections){
//            nonFrequentProducts.add(productCollection.getProducts());
//            nonFrequentProducts.add(productCollection.getProducts());
//        }
//        return nonFrequentProducts;
//    }

    private static boolean checkWhetherContains(ArrayList<String> productNames){
        boolean contains = false;
        for(ProductCollection productCollection : DataContainer.frequentTwoElemCollections){
            if(productNames.containsAll(productCollection.getProducts())){
                return true;
            }
        }
        return false;
    }

//    public static void createListOfFrequentOneElementCollections() {
//        DataContainer.frequentOneElemCollections = new ArrayList<>();
//        DataContainer.nonFrequentOneElemCollections = new ArrayList<>();
//        DataContainer.frequentOneElemCollections = StatisticalCalculator.getFrequentOneElementCollections(DataContainer.minSupport);
//        DataContainer.nonFrequentOneElemCollections = StatisticalCalculator.getNonFrequentOneElementCollections(DataContainer.minSupport);
//    }

//    public static void createListOfFrequentTwoElementCollections() {
//        DataContainer.frequentTwoElemCollections = new ArrayList<>();
//        DataContainer.nonFrequentTwoElemCollections = new ArrayList<>();
//        DataContainer.frequentTwoElemCollections = StatisticalCalculator.getFrequentAndNonFrequentTwoElementCollections(DataContainer.nonFrequentOneElemCollections, DataContainer.minSupport);
//    }

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
