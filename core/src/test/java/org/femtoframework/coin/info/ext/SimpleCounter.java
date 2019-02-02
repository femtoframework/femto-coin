package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.annotation.Action;

/**
 * Simple Business Object
 *
 * It has properties and actions
 */
public class SimpleCounter implements ICounter {

    private String name;
    private int count;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Action(impact = Action.Impact.ACTION_INFO)
    @Override
    public int increase(int inc) {
        return count += inc;
    }

    @Override
    @Action(impact = Action.Impact.ACTION_INFO)
    public int decrease(int dec) {
        return count -= dec;
    }
}
