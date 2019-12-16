package dangolawski;

import dangolawski.models.ProductCollection;
import dangolawski.models.Relationship;
import dangolawski.services.DataContainer;
import dangolawski.services.DataFactory;
import dangolawski.services.StatisticalCalculator;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {



    public static void main(String[] args) throws IOException {
        // punkt 1
        DataFactory.getTransactionsAndProductNames();
        System.out.println("Liczba wszystkich transakcji bez NONE : " + StatisticalCalculator.getNumberOfTransactions() + "\n");

        // punkt 2
        System.out.println("Srednia liczba elementow w transakcji : " + StatisticalCalculator.getProductNumberAverage() + "\n");

        // punkt 3
        DataFactory.createOneElementProductCollectionList();
        System.out.println("Trzy zbiory jednoelementowe o największym poziomie wsparcia :");
        DataContainer.oneElementProductCollectionList.sort(Comparator.comparing(ProductCollection::getSupport).reversed());
        DataContainer.oneElementProductCollectionList.subList(0, 3).forEach(element -> System.out.println("zbior : " + element.getProducts() + " --- poziom wsparcia : " + element.getSupport() + " --- liczba transakcji : " + element.getTransactionsNumber()));

        // punkt 4
        DataFactory.getFrequentOneElementCollectionList();
        System.out.println("Liczba jednoelementowych zbiorów częstych : " + DataContainer.frequentOneElemCollectionList.size() + "\n");




        // punkt 5
        DataFactory.createTwoElementProductCollectionList();
        DataFactory.getFrequentTwoElementProductCollectionList();
        System.out.println("Liczba dwuelementowych zbiorów częstych : " + DataContainer.frequentTwoElemCollections.size() + "\n");
        // punkt 6
        displayThreeFrequentTwoElementCollections();
        // punkt 7
        DataFactory.createTwoElementRelationshipsBasedOnFrequentLists();
        displayTwoElementRelationshipWithLargestConfidence();
        // punkt 8, 9, 10, 11
        displayTwoElementRelationshipWithLargestSupport();
        // punkt 12
//        DataFactory.createThreeElementProductCollectionArray();

    }

    private static void displayTwoElementRelationshipWithLargestConfidence() {
        Relationship bestRelationship = Collections.max(DataContainer.twoElementRelationshipList, Comparator.comparing(relationship -> relationship.getConfidence()));
        System.out.println("Silna reguła o największym współczynniku zaufania : " + bestRelationship.getProducts() + " - " + bestRelationship.getConfidence() + "\n");
    }

    private static void displayTwoElementRelationshipWithLargestSupport() {
        Relationship bestRelationship = Collections.max(DataContainer.twoElementRelationshipList, Comparator.comparing(relationship -> relationship.getSupport()));
        System.out.println("Reguła o największym poziomie wsparcia : " + bestRelationship.getProducts() + " - " + bestRelationship.getConfidence() + "\n");
        displayTwoElementRelationshipWithLargestSupportWithReversedProducts(bestRelationship.getProducts());
    }

    private static void displayTwoElementRelationshipWithLargestSupportWithReversedProducts(List<String>products) {
        List<Relationship> thisRelationship = DataContainer.twoElementRelationshipList.stream().filter(s -> s.getProducts().equals(DataFactory.reverseList(products)))
                .collect(Collectors.toList());
        System.out.println("Reguła o odwróconej kolejności elementów : " + thisRelationship.get(0).getProducts() + " - " + thisRelationship.get(0).getConfidence() + "\n");
    }

    private static void displayThreeFrequentTwoElementCollections() {
        DataContainer.twoElementProductCollectionList.sort(Comparator.comparing(ProductCollection::getSupport).reversed());
        System.out.println("Trzy dwuelementowe zbiory częste :");
        for(int i = 0; i < 3; i++) {
            System.out.println(DataContainer.twoElementProductCollectionList.get(i).getProducts() + " - " + DataContainer.twoElementProductCollectionList.get(i).getTransactionsNumber());
        }
        System.out.println();
    }


}
