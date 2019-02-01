package org.femtoframework.coin.info;

import org.femtoframework.bean.NamedBean;

/**
 * A feature information, either a property or function
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface FeatureInfo extends NamedBean {

    /**
     * Description of this feature
     *
     * @return Description of this feature
     */
    String getDescription();
}
