package org.femtoframework.coin.naming;

import org.femtoframework.coin.CoinConstants;

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
    private char delim = CoinConstants.CHAR_DELIM;

    public CoinNamingParser() {
    }

    public CoinNamingParser(char delim) {
        this.delim = delim;
    }

    public Name parse(String name)
    {
        return new CoinName(name, delim);
    }

}
