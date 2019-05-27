package org.femtoframework.coin.api.ext;

import org.femtoframework.bean.annotation.Action;
import org.femtoframework.bean.info.ext.SimpleBeanInfoFactory;
import org.femtoframework.coin.ResourceType;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.parameters.ParametersMap;
import org.junit.Test;

import static org.junit.Assert.*;

public class YamlPatchTest {

    @Test
    public void parsePatch() {
        YamlPatch yamlPatch = new YamlPatch(null);
        Parameters patch = yamlPatch.parsePatch("spec:\n" +
                "  containers:\n" +
                "    - name: nginx\n" +
                "      image: nginx-1.0");

        assertEquals(1, patch.size());
        assertNotNull(patch.getParameters("spec"));

        Parameters spec = patch.getParameters("spec");
        assertNotNull(spec.getList("containers"));
        assertEquals(spec.getList("containers").size(), 1);
    }

    public static class SimpleBean  {

        private String abc;
        private String bcd;

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }

        public String getBcd() {
            return bcd;
        }

        public void setBcd(String bcd) {
            this.bcd = bcd;
        }

        @Action
        public void sayHello() {
            System.out.println("Hello");
        }

        @Action
        public void sayHello1(String message) {
            System.out.println("Hello " + message);
        }

        @Action
        public void sayHello2(String message, int value) {
            System.out.println("Hello " + message + " times:" + value);
        }
    }

    @Test
    public void apply() throws Exception {

        YamlPatch yamlPatch = new YamlPatch(new SimpleBeanInfoFactory());
        SimpleBean bean = new SimpleBean();
        bean.setAbc("123");
        bean.setBcd("456");

        ParametersMap patch = new ParametersMap();
        patch.put("abc", "abc");
        patch.put("bcd", "bcd");
        yamlPatch.apply(ResourceType.BEAN, bean, patch);

        assertEquals(bean.getAbc(), "abc");
        assertEquals(bean.getBcd(), "bcd");

        ParametersMap patch1 = new ParametersMap();
        patch1.put("$invoke/sayHello", null);
        yamlPatch.apply(ResourceType.BEAN, bean, patch1);

        ParametersMap patch2 = new ParametersMap();
        patch2.put("$invoke/sayHello1", "Sheldon");
        yamlPatch.apply(ResourceType.BEAN, bean, patch2);

        ParametersMap patch3 = new ParametersMap();
        patch3.put("$invoke/sayHello2", new Object[] {"Sheldon", 123});
        yamlPatch.apply(ResourceType.BEAN, bean, patch3);
    }
}