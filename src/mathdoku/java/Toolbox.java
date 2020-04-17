package mathdoku.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A small Toolbox with useful array/lists conversion methods.
 */
public class Toolbox {
    /**
     * Creates a transpose for a matrix (2D List) (swaps rows and columns).
     *
     * @param list The initial list to be transposed
     * @param <T>  Object that (the inside) list consists of
     * @return The result transpose list
     */
    public static <T> List<List<T>> getTranspose(List<List<T>> list) {
        final int N = list.stream().mapToInt(List::size).max().orElse(-1);
        List<Iterator<T>> iterList = list.stream().map(List::iterator).collect(Collectors.toList());

        return IntStream.range(0, N)
                .mapToObj(n -> iterList.stream()
                        .filter(Iterator::hasNext)
                        .map(Iterator::next)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    /**
     * Splits an array into a matrix(2D list) made up of equal size ArrayLists.
     *
     * @param array The initial array to be split
     * @param size  Size of sub lists to be created
     * @param <T>   Object that (the inside) list consists of
     * @return The resultant matrix (2D List)
     */
    public static <T> ArrayList<List<T>> getMatrix(T[] array, int size) {
        ArrayList<T> arrayList = new ArrayList<>(Arrays.asList(array));
        ArrayList<List<T>> matrix = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i += size) {
            matrix.add(arrayList.subList(i, Math.min(arrayList.size(), i + size)));
        }

        return matrix;
    }

}
