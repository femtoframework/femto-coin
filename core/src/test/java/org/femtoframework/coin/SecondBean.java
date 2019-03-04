package org.femtoframework.coin;

import java.util.*;

/**
 * Second Bean
 *
 * @author fengyun
 * @version 1.00 2005-1-26 20:59:11
 */
public class SecondBean implements SecondInterface
{
    private List<ThirdInterface> thirds = new ArrayList<>(2);

    private Properties properties = new Properties();

    public List<ThirdInterface> getThirds()
    {
        return thirds;
    }

    public void setThirds(List<ThirdInterface> thirds) {
        this.thirds = thirds;
    }

    private List<String> stringList;

    private Set<String> stringSet;

    private Map<Integer, Integer> intMap;

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    private Map<String, Integer> stringMap;

    public Map<String, Integer> getStringMap() {
        return stringMap;
    }

    public void setStringMap(Map<String, Integer> stringMap) {
        this.stringMap = stringMap;
    }

    private int[] ints;

    public int[] getInts() {
        return ints;
    }

    public void setInts(int[] ints) {
        this.ints = ints;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Map<Integer, Integer> getIntMap() {
        return intMap;
    }

    public void setIntMap(Map<Integer, Integer> intMap) {
        this.intMap = intMap;
    }
}
