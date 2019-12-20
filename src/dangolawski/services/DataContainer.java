package dangolawski.services;

import dangolawski.models.ProductCollection;
import dangolawski.models.Relationship;
import dangolawski.models.Transaction;

import java.util.List;
import java.util.Set;

public class DataContainer {

    public static int numberOfTransactions;

    public static final String csvFilePath = "C:/Users/User/Desktop/AssociationAnalysis_JAVA/src/dangolawski/dataset.csv";

    public static final float minSupport = (float) 0.01;

    public static List<Transaction> transactionList;

    public static List<String> productNames;

    public static List<ProductCollection> oneElementProductCollectionList;

    public static List<ProductCollection> twoElementProductCollectionList;

    public static List<ProductCollection> threeElementProductCollectionList;

    public static List<ProductCollection> frequentOneElemCollectionList;

    public static List<ProductCollection> frequentTwoElemCollectionList;

    public static List<ProductCollection> nonFrequentTwoElemCollectionList;

}
