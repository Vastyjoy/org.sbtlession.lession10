
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Streams<T> {
    enum typeOperation {mFilter, mTransform}
    class operation {
        typeOperation type;
        Object operation;

        public operation(typeOperation type, Object operation) {
            this.type = type;
            this.operation = operation;
        }

    }

    private List<operation> listOperations = new ArrayList<>();
    private List<T> objectList = new ArrayList<>();
    private final List<T> ofList;
    private boolean flagFinal = false;

    /**
     * Другой вариант remove делать, что быстрее не знаю
     *
     * @param predicate
     */
    private void realFilter(Predicate<T> predicate) {
        List<T> curList = new ArrayList<>();
        for (T obj : objectList) {
            if (predicate.test(obj)) curList.add(obj);
        }
        objectList = curList;
    }

    /**
     * @param operator
     */
    private void realTransform(UnaryOperator<T> operator) {
        List<T> curList = new ArrayList<>();
        for (T obj : objectList) {
            curList.add(operator.apply(obj));
        }
        objectList = curList;
    }

    private Predicate<T> reducePredicateAnd(List<Predicate<T>> tempListPredicate) {
        Predicate<T> tPredicate = tempListPredicate.get(0);
        tempListPredicate.remove(0);
        tPredicate = tempListPredicate.stream().reduce(tPredicate, Predicate::and);
        return tPredicate;
    }

    /**
     * Метод оптимизации списка операций
     * схлопывает все операции фильтра
     */
    private void optimize() {

        List<operation> curList = new ArrayList<>();
        List<Predicate<T>> tempListPredicate = new ArrayList<>();
        for (operation op : listOperations) {
            switch (op.type) {
                case mFilter:
                    tempListPredicate.add((Predicate<T>) op.operation);
                    break;
                case mTransform:
                    if (!tempListPredicate.isEmpty()) {
                        Predicate<T> tPredicate = reducePredicateAnd(tempListPredicate);
                        curList.add(new operation(typeOperation.mFilter, tPredicate));
                    }
                    tempListPredicate.clear();
                    curList.add(new operation(typeOperation.mTransform, op.operation));
                    break;
            }
        }

        if (!tempListPredicate.isEmpty()) {
            Predicate<T> tPredicate = reducePredicateAnd(tempListPredicate);
            curList.add(new operation(typeOperation.mFilter, tPredicate));
            tempListPredicate.clear();
        }
        listOperations = curList;
    }

    /**
     * Функция выполнения всех промежуточных операций
     */
    private void performIntermidiantOperation() {
        for(operation op:listOperations){
            switch (op.type){
                case mFilter:
                    realFilter((Predicate<T>) op.operation);
                    break;
                case mTransform:
                    realTransform((UnaryOperator<T>) op.operation);
                    break;
            }

        }
    }

    private Streams(List<T> list) {
        ofList = list;
    }

    private void copyList() {
        for (T obj : ofList) {
            objectList.add(obj);
        }
    }

    /**
        Как правильно сделать, чтобы не было непроверяемого кастования.
     * @param list
     * @return
     */
    public static Streams of(List list) {
        return new Streams(list);
    }

    /**
     * Убрать все элементы соответствующие  предикату predicate
     * @param predicate условие
     * @return
     */
    public Streams<T> filter(Predicate<T> predicate) {
        listOperations.add(new operation(typeOperation.mFilter, predicate));
        return this;
    }

    /**
     * Преобразовать все элементы в соответствии с unaryOperator
     * * @return
     */

    public Streams<T> transform(UnaryOperator<T> unaryOperator) {
        listOperations.add(new operation(typeOperation.mTransform, unaryOperator));
        return this;
    }

    /**
     * Преобразует Stream<T>  в ArrayMap<k,v> Ключ задается функцией keyMap, значеник функцией valueMap
     */
    public <K, V> Map<K, V> toMap(Function<T, K> keyMap, Function<T, V> valueMap) throws IllegalAccessException {
        if (flagFinal) throw new IllegalAccessException();
        flagFinal = true;
        copyList();
        optimize();
        performIntermidiantOperation();
        Map<K, V> resultMap = new HashMap<>();
        for (T obj : objectList) {
            resultMap.put(keyMap.apply(obj), valueMap.apply(obj));
        }
        return resultMap;
    }

}
