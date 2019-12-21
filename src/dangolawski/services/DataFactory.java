package dangolawski.services;

import dangolawski.models.ProductCollection;
import dangolawski.models.Relationship;
import dangolawski.models.ThreeElemProductCollection;
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
            DataContainer.oneElementProductCollectionList.add(createNewProductCollection(new ArrayList<>(Collections.singletonList(product))));
        }
    }

    public static void createTwoElementProductCollectionList() {
        DataContainer.twoElementProductCollectionList = new ArrayList<>();
        for(ProductCollection productCollection1 : DataContainer.frequentOneElemCollectionList) {
            for(ProductCollection productCollection2 : DataContainer.frequentOneElemCollectionList) {
                if(!productCollection1.getProducts().get(0).equals(productCollection2.getProducts().get(0))) {
                    DataContainer.twoElementProductCollectionList.add(createNewProductCollection(Arrays.asList(
                            productCollection1.getProducts().get(0),
                            productCollection2.getProducts().get(0)
                    )));
                }
            }
        }
    }

    public static void createThreeElementProductCollectionList() {
        List<List<String>> products = new ArrayList<>();
        for(ProductCollection oneElemCol : DataContainer.frequentOneElemCollectionList) {
            for(ProductCollection twoElemCol : DataContainer.frequentTwoElemCollectionList) {
                if(!twoElemCol.getProducts().contains(oneElemCol.getProducts())){
                    ArrayList<String> newList = new ArrayList<>(twoElemCol.getProducts());
                    newList.addAll(oneElemCol.getProducts());
                    products.add(newList);
                }
            }
        }
        products.removeIf(DataFactory::listContainsNonFrequentProducts);

        createCollectionsFromPermutations(products);

    }

    private static boolean listContainsNonFrequentProducts(List<String> list) {
        boolean doesNotContain = true;
        for(ProductCollection productCollection : DataContainer.nonFrequentTwoElemCollectionList) {
            if(list.containsAll(productCollection.getProducts())) {
                doesNotContain = false;
                break;
            }
        }
        return doesNotContain;
    }

    private static void createCollectionsFromPermutations(List<List<String>> listOfLists) {
        DataContainer.threeElementProductCollectionList = new ArrayList<>();
        Set<List<String>> set = new HashSet<>();
        for(List<String> list : listOfLists) {
            set.addAll(getPermutations(list));
        }
        for(List<String> s : set){
            DataContainer.threeElementProductCollectionList.add(createNewThreeElemProductCollection(new ArrayList<String>(s)));
        }
    }

    private static List<List<String>> getPermutations(List<String> list) {
        List<List<String>> permutations = new ArrayList<>();
        for(int a = 0; a < 3; a++) {
            for(int b = 0; b < 3; b++) {
                if(b == a) { continue; }
                for(int c = 0; c < 3; c++) {
                    if(c == b || c == a){ continue; }
                    permutations.add(Arrays.asList(list.get(a), list.get(b), list.get(c)));
                }
            }
        }
        return permutations;
    }

    private static ProductCollection createNewProductCollection(List<String> products) {
        ProductCollection productCollection = new ProductCollection(products);
        productCollection.setSupport(StatisticalCalculator.calculateSupport(products));
        productCollection.setConfidence(StatisticalCalculator.calculateConfidence(products));
        productCollection.setLift(StatisticalCalculator.calculateLift(products));
        productCollection.setTransactionsNumber(StatisticalCalculator.countTransactions(products));
        return productCollection;
    }

    private static ThreeElemProductCollection createNewThreeElemProductCollection(List<String> products) {
        ThreeElemProductCollection threeElemProductCollection = new ThreeElemProductCollection(products);
        threeElemProductCollection.setSupport(StatisticalCalculator.calculateSupport(products));
        threeElemProductCollection.setConfidenceA_BC(StatisticalCalculator.calculateConfidence(products));
        threeElemProductCollection.setConfidenceA_BC(StatisticalCalculator.calculateConfidenceAB_C(products));
        threeElemProductCollection.setLiftA_BC(StatisticalCalculator.calculateLiftA_BC(products));
        threeElemProductCollection.setLiftAB_C(StatisticalCalculator.calculateLiftAB_C(products));
        threeElemProductCollection.setTransactionsNumber(StatisticalCalculator.countTransactions(products));
        return threeElemProductCollection;
    }

    public static void getFrequentOneElementCollectionList() {
        DataContainer.frequentOneElemCollectionList = DataContainer.oneElementProductCollectionList.stream().filter(collection -> collection.getSupport() >= DataContainer.minSupport).collect(Collectors.toList());
    }

    public static void getFrequentAndNonFrequentTwoElementCollectionList() {
        DataContainer.frequentTwoElemCollectionList = DataContainer.twoElementProductCollectionList.stream().filter(collection -> collection.getSupport() >= DataContainer.minSupport).collect(Collectors.toList());
        DataContainer.nonFrequentTwoElemCollectionList = DataContainer.twoElementProductCollectionList.stream().filter(collection -> collection.getSupport() < DataContainer.minSupport).collect(Collectors.toList());
    }


}
