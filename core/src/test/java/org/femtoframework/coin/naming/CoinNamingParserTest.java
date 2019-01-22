package org.femtoframework.coin.naming;


import org.junit.Test;

import javax.naming.Name;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test CoinNamingParser
 *
 * @author fengyun
 * @version 1.00 2010-6-11 17:32:49
 */
public class CoinNamingParserTest
{
    @Test
    public void testParse() throws Exception
    {
        CoinNamingParser parser = new CoinNamingParser();
        Name name = parser.parse("coin:jmx.mbeanServer");
        assertNotNull(name);
        assertEquals(name.toString(), "coin:jmx.mbeanServer");

        assertEquals(3, name.size());
        assertEquals("coin", name.get(0));
        assertEquals("jmx", name.get(1));
        assertEquals("mbeanServer", name.get(2));
    }
}