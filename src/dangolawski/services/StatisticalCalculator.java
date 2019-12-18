package dangolawski.services;

import dangolawski.models.ProductCollection;
import dangolawski.models.Transaction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticalCalculator {

    public static int getNumberOfTransactions() {
        DataContainer.numberOfTransactions = DataContainer.transactionList.size();
        return DataContainer.numberOfTransactions;
    }

    public static int getProductNumberAverage() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(DataContainer.csvFilePath));
        String row;
        String currentTransactionNum = "1";
        int transactionCounter = 1;
        int productNumber = 0;
        int productNumberSum = 0;
        csvReader.readLine();
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            if(data[2].equals(currentTransactionNum)){
                productNumber += 1;
            }
            else{
                transactionCounter += 1;
                productNumberSum += productNumber;
                productNumber = 1;
                currentTransactionNum = data[2];
            }

        }
        csvReader.close();

        return productNumberSum / transactionCounter;
    }

    public static float calculateSupport(List<String> products) {
        float containingTransactions = (float) DataContainer.transactionList.stream().filter(transaction -> transaction.productsInTransaction(products)).count();
        return containingTransactions / DataContainer.numberOfTransactions;
    }

    public static float calculateConfidence(List<String> products) {
        float trasnactionsContainingBothProducts = (float) DataContainer.transactionList.stream().filter(transaction -> transaction.productsInTransaction(products)).count();
        float trasnactionsContainingFirstProduct = (float) DataContainer.transactionList.stream().filter(transaction -> transaction.productsInTransaction(Arrays.asList(products.get(0)))).count();
        return trasnactionsContainingBothProducts / trasnactionsContainingFirstProduct;
    }

    public static float calculateLift(List<String> products) {
        if(products.size() > 1) {
            return calculateConfidence(products) / calculateSupport(Arrays.asList(products.get(1)));
        }
        else {
            return 0;
        }
    }

    public static int countTransactions(List<String> products) {
        return (int) DataContainer.transactionList.stream().filter(transaction -> transaction.productsInTransaction(products)).count();
    }



//    public static List<ProductCollection> getFrequentAndNonFrequentTwoElementCollections(List<ProductCollection> nonFrequentOneElemCollections, float minSupport) {
//        List<ProductCollection> filteredCollections = new ArrayList<>();
//        for (ProductCollection productCollection : DataContainer.twoElementProductCollectionList) {
//            boolean contains = true;
//            for(ProductCollection nonFrequentOneElementCollection : nonFrequentOneElemCollections){
//                if(productCollection.getProducts().containsAll(nonFrequentOneElementCollection.getProducts())){
//                    contains = false;
//                    break;
//                }
//            }
//            if(contains && productCollection.getSupport() >= minSupport){
//                filteredCollections.add(productCollection);
//            }
//            else{
//                DataContainer.nonFrequentTwoElemCollections.add(productCollection);
//            }
//        }
//        return filteredCollections;
//    }

    public static List<ProductCollection> displayThreeGreatestSupportLevels() {
        List<ProductCollection> threeCollections = new ArrayList<>();
        for(int i=0; i < 3; i++) {
            threeCollections.add(DataContainer.oneElementProductCollectionList.get(i));
        }
        return threeCollections;
    }

}
