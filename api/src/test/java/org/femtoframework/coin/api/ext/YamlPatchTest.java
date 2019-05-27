package org.femtoframework.coin.api.ext;

import org.femtoframework.coin.Component;
import org.femtoframework.coin.ext.SimpleComponent;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.element.BeanElement;
import org.femtoframework.coin.spec.element.ComponentElement;
import org.femtoframework.parameters.Parameters;
import org.junit.Test;

import static org.junit.Assert.*;

public class YamlPatchTest {

    @Test
    public void parsePatch() {
        YamlPatch yamlPatch = new YamlPatch();
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

    @Test
    public void apply() {

        YamlPatch yamlPatch = new YamlPatch();
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
}