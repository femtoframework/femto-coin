package org.femtoframework.coin.codec.json;

import com.jsoniter.JsonIterator;
import org.femtoframework.bean.annotation.AnySetter;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;
import org.femtoframework.coin.json.CoinCompatibilityMode;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CoinCompatibilityModeTest {

    public static class TestObject1 {
        private int _id;
        private String _name;

        @AnySetter
        public void setProperties(String key, Object value) {
            if (key.equals("name")) {
                _name = (String) value;
            } else if (key.equals("id")) {
                _id = ((Number) value).intValue();
            }
        }
    }

    @Test
    public void testAnySetter() throws IOException {
        TestObject1 obj = JsonIterator.deserialize(new CoinCompatibilityMode.Builder().build(),
                "{\"name\":\"hello\",\"id\":100}", TestObject1.class);
        assertEquals("hello", obj._name);
        assertEquals(100, obj._id);
    }

    public static class TestObject2 {
        @Property("field-1")
        public String field1;
    }

    @Test
    public void testProperty() throws IOException {
        TestObject2 obj = JsonIterator.deserialize(new CoinCompatibilityMode.Builder().build(),
                "{\"field-1\":\"hello\"}", TestObject2.class);
        assertEquals("hello", obj.field1);
    }

    public static class TestObject3 {
        @Ignore
        public String field1;
    }

    @Test
    public void testIgnore() throws IOException {
        TestObject3 obj = JsonIterator.deserialize(new CoinCompatibilityMode.Builder().build(),
                "{\"field1\":\"hello\"}", TestObject3.class);
        assertNull(obj.field1);
    }
}