package com.lawyus.study.datastructure;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SkipList<K extends Comparable<? super K>, V> {

    private static final double DEFAULT_PROBABILITY = 0.5;
    private static final int MAX_LEVEL = 32;

    private final Node<K, V> head;
    private final double probability;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile int size;
    private volatile int level;

    public SkipList() {
        this(DEFAULT_PROBABILITY);
    }

    public SkipList(double probability) {
        this.probability = probability;
        this.head = new Node<>(null, null, MAX_LEVEL);
        this.level = 1;
        this.size = 0;
    }

    private static final class Node<K, V> {
        final K key;
        V value;
        Node<K, V>[] next;

        @SuppressWarnings("unchecked")
        Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            this.next = (Node<K, V>[]) new Node[level];
        }
    }

    private int randomLevel() {
        int lvl = 1;
        while (lvl < MAX_LEVEL && ThreadLocalRandom.current().nextDouble() < probability) {
            lvl++;
        }
        return lvl;
    }

    public V put(K key, V value) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V>[] update = newNodeArray(MAX_LEVEL);
            Node<K, V> current = head;

            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
                update[i] = current;
            }

            current = current.next[0];

            if (current != null && current.key.compareTo(key) == 0) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }

            int newLevel = randomLevel();
            if (newLevel > level) {
                for (int i = level; i < newLevel; i++) {
                    update[i] = head;
                }
                level = newLevel;
            }

            Node<K, V> newNode = new Node<>(key, value, newLevel);
            for (int i = 0; i < newLevel; i++) {
                newNode.next[i] = update[i].next[i];
                update[i].next[i] = newNode;
            }

            size++;
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V get(K key) {
        Objects.requireNonNull(key, "key must not be null");

        lock.readLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            current = current.next[0];
            if (current != null && current.key.compareTo(key) == 0) {
                return current.value;
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public V remove(K key) {
        Objects.requireNonNull(key, "key must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V>[] update = newNodeArray(MAX_LEVEL);
            Node<K, V> current = head;

            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
                update[i] = current;
            }

            current = current.next[0];

            if (current == null || current.key.compareTo(key) != 0) {
                return null;
            }

            for (int i = 0; i < level; i++) {
                if (update[i].next[i] != current) {
                    break;
                }
                update[i].next[i] = current.next[i];
            }

            while (level > 1 && head.next[level - 1] == null) {
                level--;
            }

            size--;
            return current.value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < level; i++) {
                head.next[i] = null;
            }
            size = 0;
            level = 1;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public K firstKey() {
        lock.readLock().lock();
        try {
            Node<K, V> first = head.next[0];
            if (first == null) {
                throw new NoSuchElementException();
            }
            return first.key;
        } finally {
            lock.readLock().unlock();
        }
    }

    public K lastKey() {
        lock.readLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null) {
                    current = current.next[i];
                }
            }
            if (current == head) {
                throw new NoSuchElementException();
            }
            return current.key;
        } finally {
            lock.readLock().unlock();
        }
    }

    public V putIfAbsent(K key, V value) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            current = current.next[0];
            if (current != null && current.key.compareTo(key) == 0) {
                return current.value;
            }

            return put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean remove(K key, V value) {
        Objects.requireNonNull(key, "key must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            current = current.next[0];
            if (current != null && current.key.compareTo(key) == 0
                    && Objects.equals(current.value, value)) {
                remove(key);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean replace(K key, V oldValue, V newValue) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(newValue, "newValue must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            current = current.next[0];
            if (current != null && current.key.compareTo(key) == 0
                    && Objects.equals(current.value, oldValue)) {
                current.value = newValue;
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V replace(K key, V value) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            current = current.next[0];
            if (current != null && current.key.compareTo(key) == 0) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(mappingFunction, "mappingFunction must not be null");

        V v = get(key);
        if (v != null) {
            return v;
        }

        lock.writeLock().lock();
        try {
            v = get(key);
            if (v != null) {
                return v;
            }

            V newValue = mappingFunction.apply(key);
            if (newValue != null) {
                put(key, newValue);
                return newValue;
            }
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(remappingFunction, "remappingFunction must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            current = current.next[0];
            if (current != null && current.key.compareTo(key) == 0) {
                V newValue = remappingFunction.apply(key, current.value);
                if (newValue != null) {
                    current.value = newValue;
                    return newValue;
                } else {
                    remove(key);
                    return null;
                }
            }
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(remappingFunction, "remappingFunction must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            current = current.next[0];
            V oldValue = (current != null && current.key.compareTo(key) == 0) ? current.value : null;
            V newValue = remappingFunction.apply(key, oldValue);

            if (newValue != null) {
                if (oldValue != null) {
                    current.value = newValue;
                } else {
                    put(key, newValue);
                }
                return newValue;
            } else if (oldValue != null) {
                remove(key);
            }
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(remappingFunction, "remappingFunction must not be null");

        lock.writeLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            current = current.next[0];
            if (current != null && current.key.compareTo(key) == 0) {
                V merged = remappingFunction.apply(current.value, value);
                if (merged != null) {
                    current.value = merged;
                    return merged;
                } else {
                    remove(key);
                    return null;
                }
            } else {
                put(key, value);
                return value;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action, "action must not be null");

        lock.readLock().lock();
        try {
            Node<K, V> current = head.next[0];
            while (current != null) {
                action.accept(current.key, current.value);
                current = current.next[0];
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<K> keySet() {
        lock.readLock().lock();
        try {
            Set<K> keys = new LinkedHashSet<>();
            Node<K, V> current = head.next[0];
            while (current != null) {
                keys.add(current.key);
                current = current.next[0];
            }
            return Collections.unmodifiableSet(keys);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Collection<V> values() {
        lock.readLock().lock();
        try {
            List<V> vals = new ArrayList<>(size);
            Node<K, V> current = head.next[0];
            while (current != null) {
                vals.add(current.value);
                current = current.next[0];
            }
            return Collections.unmodifiableCollection(vals);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        lock.readLock().lock();
        try {
            Set<Map.Entry<K, V>> entries = new LinkedHashSet<>();
            Node<K, V> current = head.next[0];
            while (current != null) {
                entries.add(new AbstractMap.SimpleImmutableEntry<>(current.key, current.value));
                current = current.next[0];
            }
            return Collections.unmodifiableSet(entries);
        } finally {
            lock.readLock().unlock();
        }
    }

    public K lowerKey(K key) {
        Objects.requireNonNull(key, "key must not be null");
        lock.readLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }
            return current == head ? null : current.key;
        } finally {
            lock.readLock().unlock();
        }
    }

    public K floorKey(K key) {
        Objects.requireNonNull(key, "key must not be null");
        lock.readLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            if (current == head) {
                return null;
            }

            Node<K, V> next = current.next[0];
            if (next != null && next.key.compareTo(key) == 0) {
                return next.key;
            }
            return current.key;
        } finally {
            lock.readLock().unlock();
        }
    }

    public K ceilingKey(K key) {
        Objects.requireNonNull(key, "key must not be null");
        lock.readLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) < 0) {
                    current = current.next[i];
                }
            }

            Node<K, V> candidate = current.next[0];
            return candidate != null ? candidate.key : null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public K higherKey(K key) {
        Objects.requireNonNull(key, "key must not be null");
        lock.readLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null && current.next[i].key.compareTo(key) <= 0) {
                    current = current.next[i];
                }
            }

            Node<K, V> candidate = current.next[0];
            return candidate != null ? candidate.key : null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map.Entry<K, V> lowerEntry(K key) {
        K k = lowerKey(key);
        return k == null ? null : new AbstractMap.SimpleImmutableEntry<>(k, get(k));
    }

    public Map.Entry<K, V> floorEntry(K key) {
        K k = floorKey(key);
        return k == null ? null : new AbstractMap.SimpleImmutableEntry<>(k, get(k));
    }

    public Map.Entry<K, V> ceilingEntry(K key) {
        K k = ceilingKey(key);
        return k == null ? null : new AbstractMap.SimpleImmutableEntry<>(k, get(k));
    }

    public Map.Entry<K, V> higherEntry(K key) {
        K k = higherKey(key);
        return k == null ? null : new AbstractMap.SimpleImmutableEntry<>(k, get(k));
    }

    public Map.Entry<K, V> firstEntry() {
        lock.readLock().lock();
        try {
            Node<K, V> first = head.next[0];
            return first == null ? null
                    : new AbstractMap.SimpleImmutableEntry<>(first.key, first.value);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map.Entry<K, V> lastEntry() {
        lock.readLock().lock();
        try {
            Node<K, V> current = head;
            for (int i = level - 1; i >= 0; i--) {
                while (current.next[i] != null) {
                    current = current.next[i];
                }
            }
            return current == head ? null
                    : new AbstractMap.SimpleImmutableEntry<>(current.key, current.value);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map.Entry<K, V> pollFirstEntry() {
        lock.writeLock().lock();
        try {
            Node<K, V> first = head.next[0];
            if (first == null) {
                return null;
            }
            Map.Entry<K, V> entry = new AbstractMap.SimpleImmutableEntry<>(first.key, first.value);
            remove(first.key);
            return entry;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map.Entry<K, V> pollLastEntry() {
        lock.writeLock().lock();
        try {
            if (size == 0) {
                return null;
            }
            K lastKey = lastKey();
            V lastValue = get(lastKey);
            remove(lastKey);
            return new AbstractMap.SimpleImmutableEntry<>(lastKey, lastValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Iterator<Map.Entry<K, V>> iterator() {
        lock.readLock().lock();
        try {
            List<Map.Entry<K, V>> snapshot = new ArrayList<>(size);
            Node<K, V> current = head.next[0];
            while (current != null) {
                snapshot.add(new AbstractMap.SimpleImmutableEntry<>(current.key, current.value));
                current = current.next[0];
            }
            return snapshot.iterator();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Iterator<K> keyIterator() {
        return new Iterator<K>() {
            private Node<K, V> current = head.next[0];

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public K next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                K key = current.key;
                current = current.next[0];
                return key;
            }
        };
    }

    public Stream<Map.Entry<K, V>> stream() {
        return StreamSupport.stream(
                Spliterators.spliterator(iterator(), size, Spliterator.ORDERED | Spliterator.DISTINCT),
                false
        );
    }

    public Stream<K> keyStream() {
        return StreamSupport.stream(
                Spliterators.spliterator(keyIterator(), size, Spliterator.ORDERED | Spliterator.DISTINCT),
                false
        );
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            StringBuilder sb = new StringBuilder("{");
            Node<K, V> current = head.next[0];
            boolean first = true;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key).append("=").append(current.value);
                first = false;
                current = current.next[0];
            }
            sb.append("}");
            return sb.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkipList<?, ?> that)) return false;

        lock.readLock().lock();
        try {
            if (this.size != that.size) return false;

            Node<K, V> thisNode = this.head.next[0];
            Node<?, ?> thatHead = that.head;
            if (thatHead == null) return false;
            Node<?, ?> thatNode = thatHead.next[0];

            while (thisNode != null && thatNode != null) {
                if (!thisNode.key.equals(thatNode.key)) return false;
                if (!Objects.equals(thisNode.value, thatNode.value)) return false;
                thisNode = thisNode.next[0];
                thatNode = thatNode.next[0];
            }

            return thisNode == null && thatNode == null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.readLock().lock();
        try {
            int result = 1;
            Node<K, V> current = head.next[0];
            while (current != null) {
                result = 31 * result + current.key.hashCode();
                result = 31 * result + Objects.hashCode(current.value);
                current = current.next[0];
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] newNodeArray(int length) {
        return (Node<K, V>[]) new Node[length];
    }
}