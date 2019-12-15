package dangolawski.services;

import dangolawski.models.ProductCollection;
import dangolawski.models.Relationship;
import dangolawski.models.Transaction;

import java.util.List;

public class DataContainer {

    public static final String csvFilePath = "C:/Users/User/Desktop/AssociationAnalysis_JAVA/src/dangolawski/dataset.csv";

    public static final float minSupport = (float) 0.01;

    public static List<Transaction> transactionList;

    public static List<String> productNames;

    public static List<ProductCollection> oneElementProductCollectionList;

    public static List<ProductCollection> twoElementProductCollectionList;

    public static List<ProductCollection> threeElementProductCollectionList;

    public static List<ProductCollection> frequentOneElemCollections;

    public static List<ProductCollection> nonFrequentOneElemCollections;

    public static List<ProductCollection> frequentTwoElemCollections;

    public static List<ProductCollection> nonFrequentTwoElemCollections;

    public static List<ProductCollection> frequentThreeElemCollections;

    public static List<Relationship> twoElementRelationshipList;

    public static List<Relationship> threeElementRelationShipList;

}
