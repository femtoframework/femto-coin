package org.femtoframework.coin.info.ext;

import org.femtoframework.bean.NamedBean;

public interface ICounter extends NamedBean {

    /**
     * Current count
     *
     * @return Current count
     */
    int getCount();

    /**
     * Increase the count by given increment
     *
     * @param inc increment
     * @return Current count
     */
    int increase(int inc);

    /**
     * Decrease the count by given decrement
     *
     * @param dec decrement
     * @return Current Count
     */
    int decrease(int dec);
}
