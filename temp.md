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

Displayed com.jingyao.easybike/com.hellobike.atlas.business.splash.SplashActivity: +6s512ms



### 2023年参会提议

1. 建议允许秸秆焚烧：秸秆焚烧既能有效杀死病虫害还能增加土壤肥力，最重要的是能减少农药和化肥的过度使用对土壤造成的伤害和减少农作物中的农药残留，保证食品质量安全。
2. 建议允许自由选择种植粮食、树木，还是其他经济作物：反对使用行政命令强制实施老百姓退耕还林和退林还耕等自相矛盾，并对老百姓造成损失的措施。如果想提高小麦等农作物耕种面积，农业部可以提高小麦等农作物采购价格，使用经济手段解决供需关系。

