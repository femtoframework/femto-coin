package org.femtoframework.coin.ext;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.coin.annotation.Coined;
import org.femtoframework.coin.status.BeanCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Bean Condition
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class SimpleBeanCondition implements BeanCondition {

    private long timestamp;
    private List<String> types;
    private ConditionState state = ConditionState.UNKNOWN;

    public SimpleBeanCondition(BeanPhase phase) {
        this(phase.name(), ConditionState.TRUE);
    }

    public SimpleBeanCondition(String type, ConditionState state) {
        this.types = new ArrayList<>(2);
        this.types.add(type);
        this.state = state;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public ConditionState getState() {
        return state;
    }

    @Override
    public List<String> getTypes() {
        return types;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
