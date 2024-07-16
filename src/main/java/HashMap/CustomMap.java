package HashMap;

import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс описывает собственную реализацию HashMap.
 * Для разрешения коллизий используется метод цепочек.
 * Тип параметров:
 * @param <K> тип ключей.
 * @param <V> тип значений.
 */

public class CustomMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {
    /**
     * Коэффициент загрузки, используемый по умолчанию.
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     *  Емкость хеш-таблицы по умолчанию.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    /**
     * Коэффициент расширения массива
     */
    private static final int EXPANSION_COEFFICIENT = 2;
    /**
     * Длина массива
     */
    private int arrayLength;
    /**
     * Количество элементов в массиве
     */
    private int size;
    /**
     * Коэффициент загрузки
     */
    private final float loadFactor;
    /**
     * Хеш-таблица, реализованная на основе массива,
     * для хранения пар «ключ-значение» в виде узлов. Здесь хранятся Node
     */
    private Node<K, V>[] table;

    /**
     * Конструктор с параметрами по умолчанию
     */
    public CustomMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    /**
     * Конструктор с заданной длиной массива arrayLenght
     * @param arrayLength длинна массива
     */
    public CustomMap(int arrayLength) {
        this(arrayLength, DEFAULT_LOAD_FACTOR);
    }
    /**
     * Конструктор с заданным коэффициентом загрузки
     *
     * @param loadFactor коэффициент загрузки мапы
     */
    public CustomMap(float loadFactor) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor);
    }
    /**
     * Конструктор инициализируется произвольной длиной массива и коэффициентом загрузки.
     *
     * @param arrayLength длинна массива.
     * @param loadFactor  коэффициент загрузки.
     */
    public CustomMap(int arrayLength, float loadFactor)
    {
        this.arrayLength = arrayLength;
        this.loadFactor = loadFactor;
        this.table = new Node[arrayLength];
    }
    /**
     * Метод возвращает количество сохраненных элементов.
     *
     * @return количество сохраненных элементов.
     */
    @Override
    public int size() {
        return size;
    }
    /**
     * Метод проверяет мапу на наличие элементов
     *
     * @return мапа пуста
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    /**
     * Метод проверяет имеется ли ключ в мапе.
     *
     * @param key ключ который нужно проверить есть ли он в мапе
     * @return ключ.
     */
    @Override
    public boolean containsKey(Object key) {
        return keySet().stream().anyMatch(HaveKey -> HaveKey.equals(key));
    }
    /**
     * Метод проверяет имеется ли значение в мапе.
     *
     * @param value значение которое нужно проверить есть ли он в мапе
     * @return значение
     */
    @Override
    public boolean containsValue(Object value) {
        return values().stream().anyMatch(HaveValue -> HaveValue.equals(value));
    }
    /**
     * Метод возвращает значение по ключу
     *
     * @param key ключ по которому возвращаем значение
     * @return значение полученное по ключу
     */
    @Override
    public V get(Object key) {
        int hash = hashCode(key);
        int index = getIndex(hash, arrayLength);
        Node<K, V> node = table[index];
        if (node == null) {
            return null;
        }
        K nodeKey = node.key;
        if (key == null && nodeKey == null) {
            return node.value;
        }
        while (node.next != null) {
            if (hash == node.hashCode) {
                if (key != null && key.equals(node.getKey())) {
                    return node.value;
                }
            } else {
                node = node.next;
            }
        }
        if (key != null && key.equals(node.getKey())) {
            return node.value;
        }
        return null;
    }
    /**
     * Метод добавляет ключ/значение
     *
     * @param key   ключ с которым должно быть связано значение
     * @param value значение, которое связано с ключом
     * @return добавлено значение, которое соответсвует ключу
     */
    @Override
    public V put(K key, V value) {
        int hash = hashCode(key);
        int index = getIndex(hash, arrayLength);
        Node<K, V> node = table[index];
        if (node == null) {
            node = new Node<>(key, value, hash, null);
            table[index] = node;
            size++;
            increaseSize();
        } else if (key != null && node.key != null) {
            while (node.next != null) {
                if (key.equals(node.getKey())) {
                    node.setValue(value);
                }
                node = node.next;
            }
            if (key.equals(node.getKey())) {
                node.setValue(value);
            } else {
                node.next = new Node<>(key, value, hash, null);
                size++;
                increaseSize();
            }
        } else {
            table[index] = new Node<>(key, value, hash, null);
        }
        return value;
    }
    /**
     * Метод удаляет ключ и значение связанное с ним
     *
     * @param key ключ который должен быть удален
     * @return удаленное значение
     */
    @Override
    public V remove(Object key) {
        int hash = hashCode(key);
        int index = getIndex(hash, arrayLength);
        if (table[index] != null) {
            Node<K, V> node = table[index];
            Node<K, V> prevNode = null;
            if (node.key == null) {
                node = table[index];
                table[index] = null;
                size--;
                return node.value;
            }
            while (node != null) {
                if (hash == node.hashCode && node.key.equals(key)) {
                    if (prevNode == null) {
                        node = table[index];
                        table[index] = table[index].next;
                        size--;
                        return node.value;
                    } else {
                        prevNode.next = node.next;
                        size--;
                        return node.value;
                    }
                }
                prevNode = node;
                node = node.next;
            }
        }
        return null;
    }

    /**
     * Метод добавляет все элементы из одной мапы в другую
     *
     * @param map мапа в которую добавляются элементы
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Метод очищает мапу
     */
    @Override
    public void clear() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }
    /**
     * Метод возвращает набор ключей
     *
     * @return набор ключей
     */
    @Override
    public Set<K> keySet() {
        return entrySet().stream().map(Entry::getKey).collect(Collectors.toSet());
    }
    /**
     * Метод возвращает набор значений
     *
     * @return набор значений
     */
    @Override
    public Collection<V> values() {
        return entrySet().stream().map(Entry::getValue).collect(Collectors.toSet());
    }
    /**
     * Метод возвращает набор всех ключ-значений
     *
     * @return набор entries
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<>();
        for (Node<K, V> node : table) {
            while (node != null) {
                set.add(node);
                node = node.next;
            }
        }
        return set;
    }
    /**
     * Метод сравнивает объект в мапе
     *
     * @param object объект который нужно сравнить на равенство
     * @return объект == мапе
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CustomMap<?, ?> customMap)) return false;
        if (!super.equals(object)) return false;
        return arrayLength == customMap.arrayLength
                && size == customMap.size
                && Float.compare(loadFactor, customMap.loadFactor) == 0
                && Arrays.equals(table, customMap.table);
    }

    public Node<K, V>[] getTable() {
        return table;
    }

    /**
     * Метод возвращает хэш-код ключа
     *
     * @param key ключ для которого необходимо вычислить хэшкод
     * @return хэш-код ключа
     */
    private int hashCode(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    /**
     *Метод возвращает индекс элемента по хэшкоду и длинне массива
     *
     * @param hash   хэш для ключа.
     * @param length длина массива.
     * @return индекс элемента.
     */
    private int getIndex(int hash, int length) {
        return Math.abs(hash) % length;
    }

    /**
     * Метод проверяет, нужно ли расширять массив,
     * и если да (превышен коэффициент загрузки) - расширяет массив.
     */
    private void increaseSize() {
        if (arrayLength * loadFactor < size) {
            int newArrayLength = arrayLength * EXPANSION_COEFFICIENT;
            Node<K, V>[] newTable = new Node[newArrayLength];
            Set<Entry<K, V>> entrySet = entrySet();
            for (Entry<K, V> entry : entrySet) {
                Node<K, V> nodeFromEntry = (Node<K, V>) entry;
                nodeFromEntry.next = null;
                int newIndex = getIndex(nodeFromEntry.hashCode, newArrayLength);
                Node<K, V> node = newTable[newIndex];
                if (node == null) {
                    newTable[newIndex] = nodeFromEntry;
                } else {
                    while (node.next != null) {
                        node = node.next;
                    }
                    node.next = nodeFromEntry;
                }
            }
            table = newTable;
            arrayLength = newArrayLength;
        }
    }

    /**
     * Класс описывает Ноду, которая хранит ключ и значение в массиве
     *
     * @param <K> тип ключа
     * @param <V> тип значения
     */
    private static class Node<K, V> implements Map.Entry<K, V> {
        /**
         * Поле, в котором хранится ключ
         */
        private final K key;
        /**
         * Поле, в котором хранится значение
         */
        private V value;
        /**
         * Ссылка на следущую ноду
         */
        private Node<K, V> next;
        /**
         * Хэш для ключа
         */
        private final int hashCode;
        /**
         * Конструктор объекта Ноды
         *
         * @param key      ключ с которым связано значение
         * @param value    значение связанное с ключом
         * @param hashCode хэш для ключа
         * @param next     ссылка на следующую ноду
         */
        private Node(K key, V value, int hashCode, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hashCode = hashCode;
            this.next = next;
        }
        /**
         * Метод возвращает сохраненный ключ
         *
         * @return ключ
         */
        @Override
        public K getKey() {
            return key;
        }
        /**
         * Метод возвращает сохраненное значение
         *
         * @return значение
         */
        @Override
        public V getValue() {
            return value;
        }
        /**
         * Метод устанавливает новое значение для ноды
         *
         * @param value новое значение которое будет сохранено в Ноде
         * @return новое значение
         */
        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
        /**
         * Метод сравнивает полученный объект с Нодой
         *
         * @param object сравниваемый объект
         * @return равны ли узел и объект
         */
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Map.Entry<?, ?>)) return false;
            Node<?, ?> node = (Node<?, ?>) object;
            return hashCode == node.hashCode
                    && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }
        /**
         * Метод возвращает хэш для Ноды
         *
         * @return хэш
         */
        @Override
        public int hashCode() {
            return Objects.hash(key, value, next, hashCode);
        }
        /**
         * Метод возвращает строковое представление Ноды
         *
         * @return Нода в toString
         */
        @Override
        public String toString() {
            return key + " = " + value;
        }
    }
}