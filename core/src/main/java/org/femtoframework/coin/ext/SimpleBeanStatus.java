package org.femtoframework.coin.ext;


import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.coin.status.BeanCondition;
import org.femtoframework.coin.status.BeanStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Bean Status
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class SimpleBeanStatus implements BeanStatus {

    private BeanPhase phase = BeanPhase.DISABLED;
    private List<BeanCondition> conditions = new ArrayList<>(3);

    /**
     * Return conditions, container changes will add log information into this field.
     *
     * @return Condition
     */
    public List<BeanCondition> getConditions() {
        return conditions;
    }

    /**
     * Add bean condition
     *
     * @param condition BeanCondition
     */
    @Override
    public void addCondition(BeanCondition condition) {
        if (conditions.isEmpty()) {
            conditions.add(condition);
        }
        else {
            BeanCondition last = conditions.get(conditions.size()-1);
            if (condition.getState() == last.getState() && condition.getTimestamp() - last.getTimestamp() < 10) {
                last.getTypes().addAll(condition.getTypes());
            }
            else {
                conditions.add(condition);
            }
        }
    }


    /**
     * Bean Phase
     *
     * @return Phase
     */
    public BeanPhase getPhase() {
        return phase;
    }

    public void setConditions(List<BeanCondition> conditions) {
        this.conditions = conditions;
    }

    public void setPhase(BeanPhase phase) {
        this.phase = phase;
    }
}
