package org.femtoframework.coin;

/**
 * Bean factory
 * 
 * @author fengyun
 * @version 1.00 2005-1-27 14:12:22
 */
public interface BeanFactory extends ResourceFactory<Object> {

    /**
     * Add bean
     *
     * @param name Name of the bean
     * @param bean Bean
     */
    void add(String name, Object bean);
}
