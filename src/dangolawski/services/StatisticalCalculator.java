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

    public static float getProductNumberAverage() throws IOException {
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
        productNumberSum += productNumber;
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
            return calculateConfidence(products) / calculateSupport(Arrays.asList(products.get(products.size()-1)));
        }
        else {
            return 0;
        }
    }

    public static int countTransactions(List<String> products) {
        return (int) DataContainer.transactionList.stream().filter(transaction -> transaction.productsInTransaction(products)).count();
    }

    public static void displayThreeTwoElementFrequentCollections() {
        ArrayList<List<String>> products = new ArrayList<>();
        int counter = 0;
        DataContainer.frequentTwoElemCollectionList.sort(Comparator.comparing(ProductCollection::getSupport).reversed());
        System.out.println("Trzy dwuelementowe zbiory czeste :");
        for(ProductCollection productCollection : DataContainer.frequentTwoElemCollectionList) {
            if(!productsAppeared(products, productCollection.getProducts())) { continue; }
            System.out.println("zbior : " + productCollection.getProducts() + " --- wsparcie : " + productCollection.getSupport() + " --- wsp. zaufania : " + productCollection.getConfidence() + " --- liczba transakcji : " + productCollection.getTransactionsNumber());
            counter += 1;
            products.add(productCollection.getProducts());
            if(counter == 3) { break; }
        }
        System.out.println();
    }

    private static boolean productsAppeared(ArrayList<List<String>> products, List<String> currentCollection) {
        boolean notAppeared = true;
        for(List<String> list : products) {
            if(list.containsAll(currentCollection)){
                notAppeared = false;
                break;
            }
        }
        return notAppeared;
    }

    public static void displayCollectionWithTheLargestConfidenceLevel() {
        System.out.println("Silna reguła o największym współczynniku zaufania :");
        ProductCollection selectedCollection =  Collections.max(DataContainer.frequentTwoElemCollectionList, Comparator.comparing(ProductCollection::getConfidence));
        System.out.println("zbior : " + selectedCollection.getProducts() + " --- wspolczynnik zaufania : " + selectedCollection.getConfidence() + " --- liczba transakcji : " + selectedCollection.getTransactionsNumber());
        System.out.println();
    }

    public static void displayCollectionWithTheLargestSupportLevel() {
        System.out.println("Reguła o największym poziomie wsparcia :");
        ProductCollection selectedCollection =  Collections.max(DataContainer.frequentTwoElemCollectionList, Comparator.comparing(ProductCollection::getSupport));
        System.out.println("zbior : " + selectedCollection.getProducts() + " --- wspolczynnik zaufania : " + selectedCollection.getSupport() + " --- wsparcie : " + selectedCollection.getSupport() + " --- lift : " + selectedCollection.getLift());
        System.out.println();
        displayCollectionWithReversedListOfProducts(selectedCollection);
    }

    private static void displayCollectionWithReversedListOfProducts(ProductCollection supportProductCollection) {
        System.out.println("Reguła o odwróconej kolejności elementów :");
        ProductCollection foundCollection = DataContainer.frequentTwoElemCollectionList.stream().
                filter(collection -> collection.getProducts().equals(reverseArray(supportProductCollection.getProducts()))).
                findAny().orElse(null);
        System.out.println("zbior : " + foundCollection.getProducts() + " --- wspolczynnik zaufania : " + foundCollection.getConfidence() + " --- wsparcie : " + foundCollection.getSupport() + " --- lift : " + foundCollection.getLift());
    }

    public static List<String> reverseArray(List<String> products) {
        List<String> reversedList = new ArrayList<>();
        for(int i = products.size()-1; i >= 0; i--) {
            reversedList.add(products.get(i));
        }
        return reversedList;
    }
}
