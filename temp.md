```
/**
 * Inserts an element into the array at the specified index, growing the array if there is no
 * more room.
 *
 * @param array The array to which to append the element. Must NOT be null.
 * @param currentSize The number of elements in the array. Must be less than or equal to
 *                    array.length.
 * @param element The element to insert.
 * @return the array to which the element was appended. This may be different than the given
 *         array.
 */
public static <T> T[] insert(T[] array, int currentSize, int index, T element) {
    assert currentSize <= array.length;
    if (currentSize + 1 <= array.length) {
        System.arraycopy(array, index, array, index + 1, currentSize - index);
        array[index] = element;
        return array;
    }
    @SuppressWarnings("unchecked")
    T[] newArray = ArrayUtils.newUnpaddedArray((Class<T>)array.getClass().getComponentType(),
            growSize(currentSize));
    System.arraycopy(array, 0, newArray, 0, index);
    newArray[index] = element;
    System.arraycopy(array, index, newArray, index + 1, array.length - index);
    return newArray;
}
    
```