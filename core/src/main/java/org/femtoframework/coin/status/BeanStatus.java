package org.femtoframework.coin.status;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.util.CollectionUtil;

import java.util.Collections;
import java.util.List;

/**
 * Bean Status
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanStatus {

    /**
     * Return conditions, container changes will add log information into this field.
     *
     * @return Condition
     */
    default List<BeanCondition> getConditions() {
        return Collections.emptyList();
    }


    /**
     * Add bean condition, it should merge the similar condition together
     *
     * @param condition BeanCondition
     */
    void addCondition(BeanCondition condition);

    /**
     * Bean Phase
     *
     * @return Phase
     */
    default BeanPhase getPhase() {
        return BeanPhase.DISABLED;
    }

    /**
     * Set current phase
     *
     * @param phase Current phase
     */
    void setPhase(BeanPhase phase);


    /**
     * Attributes associated with this component
     *
     * @return Attributes
     */
    default Parameters<Object> getAttributes() {
        return CollectionUtil.emptyParameters();
    }
}
