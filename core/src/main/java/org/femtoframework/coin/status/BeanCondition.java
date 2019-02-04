package org.femtoframework.coin.status;

import java.util.List;

/**
 * Bean Condition
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanCondition {

    default ConditionState getState() {
        return ConditionState.UNKNOWN;
    }

    List<String> getTypes();

    long getTimestamp();

    enum ConditionState {
        UNKNOWN,

        FALSE,

        TRUE;
    }
}
