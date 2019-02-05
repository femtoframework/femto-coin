package org.femtoframework.coin.naming;

import javax.naming.Name;
import javax.naming.NameParser;

/**
 * Naming Parser
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoinNamingParser implements NameParser
{

    public Name parse(String name)
    {
        return new CoinName(name);
    }

}
