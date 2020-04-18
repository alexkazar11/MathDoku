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

    /**
     * A stack implementation, using an ArrayList.
     *
     * @param <T> Type of elements, the stack can store and work with.
     */
    public static class ListStack<T> {

        private ArrayList<T> stack;

        public ListStack() {
            this.stack = new ArrayList<>();
        }

        public void push(T item) {
            stack.add(item);
        }

        public T pop() {
            if (stack.size() == 0)
                throw new IllegalStateException("Stack is empty");
            return stack.remove(stack.size() - 1);
        }

        public boolean contains(T item) {
            return stack.contains(item);
        }

        public int size() {
            return stack.size();
        }

        public boolean isEmpty() {
            return stack.size() == 0;
        }

        public T peek() {
            return stack.get(stack.size() - 1);
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder("ListStack = { ");
            for (T t : stack) {
                string.append(t);
            }
            string.append(" }");
            return string.toString();
        }

        public ArrayList<T> getList() {
            return stack;
        }

        public void clear() {
            stack.clear();
        }

    }

}
