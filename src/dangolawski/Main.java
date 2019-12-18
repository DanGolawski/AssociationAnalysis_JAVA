package dangolawski;

import dangolawski.models.ProductCollection;
import dangolawski.services.DataContainer;
import dangolawski.services.DataFactory;
import dangolawski.services.StatisticalCalculator;

import java.io.IOException;
import java.util.Comparator;

public class Main {



    public static void main(String[] args) throws IOException {
        // punkt 1 Liczba wszystkich transakcji
        DataFactory.getTransactionsAndProductNames();
        System.out.println("Liczba wszystkich transakcji bez NONE : " + StatisticalCalculator.getNumberOfTransactions() + "\n");

        // punkt 2 Średnia liczba produktów w transakcji
        System.out.println("Srednia liczba elementow w transakcji : " + StatisticalCalculator.getProductNumberAverage() + "\n");

        // punkt 3 Trzy zbiory jednoelementowe o największym poziomie wsparcia
        DataFactory.createOneElementProductCollectionList();
        System.out.println("Trzy zbiory jednoelementowe o największym poziomie wsparcia :");
        DataContainer.oneElementProductCollectionList.sort(Comparator.comparing(ProductCollection::getSupport).reversed());
        DataContainer.oneElementProductCollectionList.subList(0, 3).forEach(element -> System.out.println("zbior : " + element.getProducts() + " --- poziom wsparcia : " + element.getSupport() + " --- liczba transakcji : " + element.getTransactionsNumber()));
        System.out.println();

        // punkt 4 Liczba jednoelementowych zbiorów częstych
        DataFactory.getFrequentOneElementCollectionList();
        System.out.println("Liczba jednoelementowych zbiorów częstych : " + DataContainer.frequentOneElemCollectionList.size() + " (support = " + DataContainer.minSupport + ")" + "\n");

        // punkt 5 Liczba dwuelementowych zbiorów częstych
        DataFactory.createTwoElementProductCollectionList();
        DataFactory.getFrequentAndNonFrequentTwoElementCollectionList();
        System.out.println("Liczba dwuelementowych zbiorów częstych (X=>Y) oraz (Y=>X) : " + DataContainer.frequentTwoElemCollectionList.size() + " (support = " + DataContainer.minSupport + ")" + "\n");

        // punkt 6 Trzy dwuelementowe zbiory częste
        DataFactory.displayThreeTwoElementFrequentCollections();

        // punkt 7 Silna reguła o największym współczynniku zaufania
        DataFactory.displayCollectionWithTheBiggestSupportLevel();







        // punkt 8, 9, 10, 11
//        displayTwoElementRelationshipWithLargestSupport();
        // punkt 12
//        DataFactory.createThreeElementProductCollectionArray();

    }

}
