package HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class CustomMapTest {
    private static CustomMap<String, String> mySmallMap() {
        CustomMap<String, String> myMap = new CustomMap<>();
        for (int i = 0; i < 20; i++) {
            myMap.put("key " + i, "value " + i);
        }
        return myMap;
    }

    private static CustomMap<String, String> myBigMap() {
        CustomMap<String, String> myMap2 = new CustomMap<>();
        for (int i = 0; i < 2_000_000; i++) {
            myMap2.put("key " + i, "value " + i);
        }
        return myMap2;
    }

    private static CustomMap<String, String> myEmptyMap() {
        CustomMap<String, String> myMap = new CustomMap<>();
        return myMap;
    }

    @Test
    void sizeTestFullMap() {
        CustomMap<String, String> customMap = mySmallMap();
        Assertions.assertEquals(20, customMap.size());
    }

    @Test
    void sizeTestEmptyMap() {
        CustomMap<String, String> customMap = myEmptyMap();
        Assertions.assertEquals(0, customMap.size());
    }

    @Test
    void isEmptyTestEmptyMap() {
        CustomMap<String, String> customMap = myEmptyMap();
        Assertions.assertTrue(customMap.isEmpty());
    }

    @Test
    void isEmptyTestFullMap() {
        CustomMap<String, String> customMap = myBigMap();
        Assertions.assertFalse(customMap.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"key 2", "key 10", "key 19"})
    void containsKeyTestKeyHave(String key) {
        CustomMap<String, String> customMap = mySmallMap();
        Assertions.assertTrue(customMap.containsKey(key));
    }

    @Test
    void containsKeyTestKeyNotHave() {
        CustomMap<String, String> customMap = myEmptyMap();
        Assertions.assertFalse(customMap.containsKey("key 1"));
    }

    @Test
    void containsKeyTestKeyNull() {
        CustomMap<String, String> customMap = mySmallMap();
        Assertions.assertFalse(customMap.containsKey(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"value 2", "value 17"})
    void containsValueTestValueHave(String value) {
        CustomMap<String, String> customMap = mySmallMap();
        Assertions.assertTrue(customMap.containsValue(value));
    }

    @Test
    void containsValueTestValueNotHave() {
        CustomMap<String, String> customMap = mySmallMap();
        Assertions.assertFalse(customMap.containsValue(5));
    }

    @Test
    void containsValueTestValueHaveNull() {
        CustomMap<String, String> customMap = mySmallMap();
        CustomMap<String, String> customMap2 = myBigMap();
        Assertions.assertFalse(customMap.containsValue(null));
        Assertions.assertFalse(customMap2.containsValue(null));
    }

    @Test
    void getTestExistingKey() {
        CustomMap<String, String> customMap = mySmallMap();
        Assertions.assertEquals("value 0", customMap.get("key 0"));
        Assertions.assertEquals("value 19", customMap.get("key 19"));
    }

    @Test
    void getTestMissingKey() {
        CustomMap<String, String> customMap = mySmallMap();
        Assertions.assertNull(customMap.get("key"));
    }

    @Test
    void getTestNullKey() {
        CustomMap<String, String> customMap = mySmallMap();
        customMap.put(null, "5");
        Assertions.assertEquals("5", customMap.get(null));
    }

    @Test
    void putTestString() {
        CustomMap<String, String> customMap = new CustomMap<>();
        customMap.put("key1", "Ivan");
        Assertions.assertEquals(1, customMap.size());
        Assertions.assertEquals("Ivan", customMap.get("key1"));
    }

    @Test
    void putTestInteger() {
        CustomMap<Integer, Integer> customMap = new CustomMap<>();
        customMap.put(1, 200);
        customMap.put(2, 2200);
        Assertions.assertEquals(2, customMap.size());
        Assertions.assertEquals(200, customMap.get(1));
        Assertions.assertEquals(2200, customMap.get(2));
    }

    @Test
    void putTestNullArgs() {
        CustomMap<String, String> customMap = myBigMap();
        Assertions.assertEquals("value", customMap.put(null, "value"));
        Assertions.assertNull(customMap.put(null, null));
        Assertions.assertNull(customMap.get(null));
    }

    @Test
    void putTestValueReturn() {
        CustomMap<String, Integer> customMap = new CustomMap<>();
        Assertions.assertEquals(100, customMap.put("key", 100));
        Assertions.assertNull(customMap.put("key", null));
    }

    @Test
    void putTestBigValueNodes() {
        CustomMap<String, String> customMap = myBigMap();
        Assertions.assertEquals(2000000, customMap.size());
    }

    @Test
    void removeTest() {
        CustomMap<String, String> customMap = mySmallMap();
        Assertions.assertTrue(customMap.containsKey("key 1") & customMap.containsValue("value 1"));
        customMap.remove("key 1");
        Assertions.assertFalse(customMap.containsKey("key 1") & customMap.containsValue("value 1"));
    }

    @Test
    void removeTestMillionNodes() {
        CustomMap<String, String> customMap = myBigMap();
        for (int i = 1; i <= 1_999_999; i++) {
            customMap.remove("key " + i);
        }
        Assertions.assertEquals(1, customMap.size());
    }
    @Test
    void putAllTest() {
        CustomMap<String, String> customMap = mySmallMap();

        Map<String, String> additionalMap = new HashMap<>();
        for (int i = 10; i < 15; i++) {
            additionalMap.put("key " + i, "value");
        }

        Assertions.assertEquals(20,customMap.size());
        customMap.putAll(additionalMap);

        for (int i = 10; i < 15; i++) {
            Assertions.assertTrue(
                    customMap.containsKey("key " + i) & customMap.containsValue("value")
            );
        }
        Assertions.assertEquals(20, customMap.size());
    }
    @Test
    void clearTest() {
        CustomMap<String, String> myHashMap = mySmallMap();
        Assertions.assertFalse(myHashMap.isEmpty());
        Assertions.assertEquals(20, myHashMap.size());
        myHashMap.clear();
        Assertions.assertTrue(myHashMap.isEmpty());
        Assertions.assertEquals(0, myHashMap.size());
    }
    @Test
    void keySetTest() {
        CustomMap<String, String> myHashMap = mySmallMap();
        Set<?> keySet = myHashMap.keySet();
        Assertions.assertTrue(keySet.size() == myHashMap.size());
        for (int i = 0; i < 20; i++) {
            Assertions.assertTrue(keySet.contains("key " + i));
        }
        Assertions.assertFalse(keySet.contains("key 20"));
    }
    @Test
    void entrySetTest() {
        CustomMap<String, String> myHashMap = mySmallMap();
        Set<?> entrySet = myHashMap.entrySet();
        Assertions.assertEquals(entrySet.size(), myHashMap.size());
    }
    @Test
    void equalsTestSameMap() {
        CustomMap<String, String> myHashMap = mySmallMap();
        CustomMap<String, String> sameLinkMyHashMap = myHashMap;
        Assertions.assertTrue(myHashMap.equals(sameLinkMyHashMap));
    }
    @Test
    void equalsTestEqualMap() {
        CustomMap<String, String> myHashMap = mySmallMap();
        CustomMap<String, String > equalMyHashMap = mySmallMap();
        Assertions.assertTrue(myHashMap.equals(equalMyHashMap));
    }
}