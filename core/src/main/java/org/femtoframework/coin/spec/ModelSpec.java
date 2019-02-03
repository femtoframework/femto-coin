package org.femtoframework.coin.spec;

import org.femtoframework.bean.NamedBean;

/**
 * Spec which has Version and Model structure
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface ModelSpec extends MapSpec<Element>, NamedBean {

    /**
     * Version
     */
    default String getVersion() {
        return SpecConstants.VERSION_CORE_KIND;
    }

}
