package org.femtoframework.coin.naming;

import org.junit.Test;

import javax.naming.CompoundName;
import javax.naming.Name;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test CoinName
 *
 * @author fengyun
 * @version 1.00 2010-6-11 18:45:06
 */

public class CoinNameTest
{
    @Test
    public void testGetPrefix0() throws Exception
    {
        Properties env = new Properties();
        env.setProperty("jndi.syntax.direction", "left_to_right");
        env.setProperty("jndi.syntax.separator", ":");
        Name name = new CompoundName("www:demo:com", env);
        getPrefix(name);
    }

    private void getPrefix(Name name) throws Exception
    {
        assertEquals(3, name.size());
        assertEquals(0, name.getPrefix(0).size());
        assertEquals("", name.getPrefix(0).toString());
        assertEquals(1, name.getPrefix(1).size());
        assertEquals("www", name.getPrefix(1).toString());
        assertEquals(2, name.getPrefix(2).size());
        assertEquals("www:demo", name.getPrefix(2).toString());
        assertEquals(3, name.getPrefix(3).size());
        assertEquals("www:demo:com", name.getPrefix(3).toString());
    }

    @Test
    public void testGetPrefix1() throws Exception
    {
        Name name = new CoinName("www:demo:com");
        getPrefix(name);
    }

    @Test
    public void testGetSuffix0() throws Exception
    {
        Properties env = new Properties();
        env.setProperty("jndi.syntax.direction", "left_to_right");
        env.setProperty("jndi.syntax.separator", ":");
        Name name = new CompoundName("www:demo:com", env);
        getSuffix(name);
    }

    private void getSuffix(Name name) throws Exception
    {
        assertEquals(3, name.size());
        assertEquals(0, name.getSuffix(3).size());
        assertEquals("", name.getSuffix(3).toString());
        assertEquals(1, name.getSuffix(2).size());
        assertEquals("com", name.getSuffix(2).toString());
        assertEquals(2, name.getSuffix(1).size());
        assertEquals("demo:com", name.getSuffix(1).toString());
        assertEquals(3, name.getSuffix(0).size());
        assertEquals("www:demo:com", name.getSuffix(0).toString());
    }

    @Test
    public void testGetSuffix1() throws Exception
    {
        Name name = new CoinName("www:demo:com");
        getSuffix(name);
    }

    @Test
    public void testEndsWith0() throws Exception
    {
        Properties env = new Properties();
        env.setProperty("jndi.syntax.direction", "left_to_right");
        env.setProperty("jndi.syntax.separator", ":");
        Name name = new CompoundName("www:demo:com", env);
        Name domain = new CompoundName("demo:com", env);
        Name com = new CompoundName("com", env);
        Name empty = new CompoundName("", env);
        endsWith(name, domain, com, empty);
    }

    private void endsWith(Name name, Name domain, Name com, Name empty) throws Exception
    {
        assertTrue(name.endsWith(domain));
        assertTrue(name.endsWith(com));
        assertTrue(domain.endsWith(com));
        assertFalse(com.endsWith(domain));
        assertTrue(name.endsWith(empty));
        assertTrue(domain.endsWith(empty));
        assertTrue(com.endsWith(empty));
    }

    @Test
    public void testEndsWith1() throws Exception
    {
        Name name = new CoinName("www:demo:com");
        Name domain = new CoinName("demo:com");
        Name com = new CoinName("com");
        endsWith(name, domain, com, CoinName.EMPTY);
    }

    @Test
    public void testStartsWith0() throws Exception
    {
        Properties env = new Properties();
        env.setProperty("jndi.syntax.direction", "left_to_right");
        env.setProperty("jndi.syntax.separator", ":");
        Name name = new CompoundName("www:demo:com", env);
        Name prefix = new CompoundName("www:demo", env);
        Name www = new CompoundName("www", env);
        Name empty = new CompoundName("", env);
        startsWith(name, prefix, www, empty);
    }

    private void startsWith(Name name, Name prefix, Name www, Name empty) throws Exception
    {
        assertTrue(name.startsWith(prefix));
        assertTrue(name.startsWith(www));
        assertTrue(prefix.startsWith(www));
        assertFalse(www.startsWith(prefix));
        assertTrue(name.startsWith(empty));
        assertTrue(prefix.startsWith(empty));
        assertTrue(www.startsWith(empty));
    }

    @Test
    public void testStartsWith1() throws Exception
    {
        Name name = new CoinName("www:demo:com");
        Name domain = new CoinName("www:demo");
        Name com = new CoinName("www");
        startsWith(name, domain, com, CoinName.EMPTY);
    }

    @Test
    public void testPath() throws Exception
    {
        Name name = new CoinName("coin:org/femtoframework/coin/api", '/');
        assertEquals(5, name.size());
        assertEquals("coin", name.get(0));
        assertEquals("org", name.get(1));
        assertEquals("femtoframework", name.get(2));
        assertEquals("coin", name.get(3));
        assertEquals("api", name.get(4));
    }

    @Test
    public void testPath2() throws Exception
    {
        Name name = new CoinName("coin", "org", "femtoframework", "coin", "api");
        assertEquals(5, name.size());
        assertEquals("coin", name.get(0));
        assertEquals("org", name.get(1));
        assertEquals("femtoframework", name.get(2));
        assertEquals("coin", name.get(3));
        assertEquals("api", name.get(4));
    }

}