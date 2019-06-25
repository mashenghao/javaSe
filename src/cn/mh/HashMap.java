package cn.mh;

import org.junit.Test;

import javax.swing.tree.TreeNode;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * HashMap的具体实现
 *
 * @param <K>
 * @param <V>
 */
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;

    /* ----------  属性   ------------- */
    transient Node<K, V>[] table; //存储元素的数组

    double loadFactor;//负载因子

    int initCapacity = DEFAULT_INITIAL_CAPACITY;//初始化位桶数组容量

    int threshold;//扩容限制  capacity * load factor

    int modCount;//fast-fail

    int size; //元素个数

    Set<Map.Entry> entrySet; //给entrySet() 方法做缓存

    public HashMap(double loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.initCapacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = 12;
    }

    public HashMap(double loadFactor, int initCapacity) {
        if (loadFactor <= 0 || loadFactor > 1) {
            throw new IllegalArgumentException("Illegal initial loadFactor: " +
                    loadFactor);
        }
        if (initCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial initCapacity: " +
                    initCapacity);
        }
        if (initCapacity > MAXIMUM_CAPACITY) {
            initCapacity = MAXIMUM_CAPACITY;
        }
        this.loadFactor = loadFactor;

        this.initCapacity = tableSizeFor(initCapacity);
        this.threshold = (int) (loadFactor * initCapacity);
    }


    public V put(K key, V value) {

        return putVal(hash(key), key, value);
    }

    V putVal(int hash, K key, V value) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n;

        if ((tab = table) == null || (n = tab.length) == 0) {//第一次存储值的时候，创建位桶数组
            n = (tab = resize()).length;
        }
        if ((p = tab[(n - 1) & hash]) == null) {//查看位桶数组下标位置是否为空
            tab[(n - 1) & hash] = new Node<>(hash, key, value, null);//为空就直接存储了
        } else {//发生hash冲突，解决冲突

            Node<K, V> e = null;
            K k;

            //判断当前位桶元素中存储的key是否和要存的相同
            if (p.hash == hash && ((k = p.key) == key || key != null && key.equals(k))) {
                e = p;
            } else if (p instanceof javax.swing.tree.TreeNode) {
                //TODO 红黑树中插入节点
                return null;
            } else {//链表解决冲突
              /*  e = p;
                for (int binCount = 0; ; binCount++) {
                    if (e.next == null) {
                        p.next = new Node<>(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) {
                            //treeifyBin(tab, hash);
                        }
                        break;
                    }
                    if (e.hash == hash && ((k = e.key) == key || key != null && key.equals(k))) {
                        break;
                    }
                    e = e.next;
                }
*/
                e = p;
                for (int i = 0; ; i++) {
                    if (e.hash == hash && ((k = e.key) == key || key != null && key.equals(k))) {
                        break;
                    } else {
                        if (e.next == null) {
                            e.next = new Node<>(hash, key, value, null);
                            if (i > TREEIFY_THRESHOLD) {
                                //转换为红黑树
                            }
                            e = null;
                            break;
                        }
                    }

                    e = e.next;
                }
            }
            if (e != null) {
                V oldvalue = (V) e.value;
                e.value = value;
                return oldvalue;
            }
        }

        if (++size > threshold) {
            resize();
        }
        modCount++;
        return null;
    }

    Node<K, V>[] resize() {
        table = new Node[initCapacity];
        return table;
    }

    @Override
    public V get(Object key) {
        return super.get(key);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public V remove(Object key) {
        return super.remove(key);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }


    /**
     * 求key的hash算法，让高十六位和低十六位做异或操作。
     * 目的是为了让hachcode都能使用的到，散列更加分布。
     *
     * @param key
     * @return
     */
    final static int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 求大于或者等于initCapacity的最小2次幂的值
     *
     * @param initCapacity
     * @return
     */
    private int tableSizeFor(int initCapacity) {
        int n = initCapacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    /**
     * 红黑树使用的节点，尚未实现
     *
     * @param <K>
     * @param <V>
     */
    final static class TreeNode<K, V> {

    }

    /**
     * HashMap中存储的元素单元，是Entry的子类
     *
     * @param <K>
     * @param <V>
     */
    static class Node<K, V> implements Map.Entry<K, V> {

        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            V oldValue = value;
            value = v;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node<?, ?> node = (Node<?, ?>) o;

            if (hash != node.hash) return false;
            if (key != null ? !key.equals(node.key) : node.key != null) return false;
            if (value != null ? !value.equals(node.value) : node.value != null) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }
}
