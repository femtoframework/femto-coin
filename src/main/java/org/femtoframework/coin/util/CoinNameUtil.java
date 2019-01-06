/**
 * Licensed to the FemtoFramework under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.coin.util;

import org.femtoframework.util.StringUtil;

/**
 * Coin Name Util
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoinNameUtil {

    /**
     * Split qualifier name to two parts.
     *
     * Qualifier name is namespace + ":" + name
     *
     * @param qName qualifier name
     * @return
     */
    public static String[] splitName(String qName) {
        String[] names = new String[2];
        names[1] = qName;
        if (StringUtil.isValid(qName)) {
            int i = qName.indexOf(':');
            if (i > 0) {
                names[0] = qName.substring(0, i);
                names[1] = qName.substring(i + 1);
            }
        }
        return names;
    }
}
