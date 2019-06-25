package cn.mh.test;


import cn.mh.HashMap;

/**
 * HashMap测试类
 */
public class TestHashMap {


    public static void main(String[] args) {
        System.out.println(123);
        HashMap<Object, Object> map = new HashMap<>();
        System.out.println(map.put(1, 0));
        System.out.println(map.put(2, 2));
        System.out.println(map.put(1, 1));
        System.out.println(map.put(4, 4));
        System.out.println(map.put(17, 17));
        System.out.println(map.put(33, 33));
        System.out.println(map.put(1, 1111));
        System.out.println(map.put(49, 33));
        System.out.println(map.put(33, 3333333));
    }
}
