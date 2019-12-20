package dangolawski.services;

import dangolawski.models.ProductCollection;
import dangolawski.models.Relationship;
import dangolawski.models.Transaction;

import java.util.List;
import java.util.Set;

public class DataContainer {

    public static int numberOfTransactions;

    public static final String csvFilePath = "C:/Users/181535/Desktop/AssociationAnalysis_JAVA/src/dangolawski/dataset_test.csv";

    public static final float minSupport = (float) 0.00;

    public static List<Transaction> transactionList;

    public static List<String> productNames;

    public static List<ProductCollection> oneElementProductCollectionList;

    public static List<ProductCollection> twoElementProductCollectionList;

    public static List<ProductCollection> threeElementProductCollectionList;

    public static List<ProductCollection> frequentOneElemCollectionList;

//    public static Set<String> frequentOneElemCollectionProducts;

    public static List<ProductCollection> nonFrequentOneElemCollectionList;

    public static List<ProductCollection> frequentTwoElemCollectionList;

//    public static Set<String> frequentTwoElemCollectionProducts;

    public static List<ProductCollection> nonFrequentTwoElemCollectionList;

//    public static List<ProductCollection> frequentThreeElemCollections;

    public static List<Relationship> twoElementRelationshipList;

    public static List<Relationship> threeElementRelationShipList;

}
