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
package org.femtoframework.coin.spec.ext;

import org.femtoframework.coin.exception.BeanSpecSyntaxException;
import org.femtoframework.coin.spec.*;
import org.femtoframework.coin.spec.element.BeanElement;
import org.femtoframework.coin.spec.element.MapElement;
import org.femtoframework.coin.spec.element.NamespaceElement;
import org.femtoframework.coin.spec.element.ModelElement;
import org.femtoframework.util.DataUtil;
import org.femtoframework.util.StringUtil;

import java.util.Map;

/**
 * Core Kind Spec definition
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoreKindSpec implements KindSpec {
    /**
     * The version this Factory supports
     *
     * @return Version
     */
    @Override
    public String getVersion() {
        return SpecConstants.VERSION_CORE_KIND;
    }

    /**
     * The Kind Class it uses
     *
     * @return The kind Class should be an Enum and Kind implementation
     */
    @Override
    public Class<? extends Kind> getKindClass() {
        return CoreKind.class;
    }

    /**
     * Convert Kind to right spec
     *
     * @param map Map like data
     * @return right spec object
     */
    public <S extends MapSpec> S toSpec(Map map) {
        String kind = DataUtil.getString(ModelElement.getValue(map, SpecConstants._KIND), null);
        if (SpecConstants.NAMESPACE.equals(kind)) {
            return (S)new NamespaceElement(map);
        }
        else if (SpecConstants.BEAN.equals(kind)) {
            return (S)new BeanElement(map);
        }
        else if (StringUtil.isInvalid(kind)) {
            String type = DataUtil.getString(ModelElement.getValue(map, SpecConstants._TYPE), null);
            if (StringUtil.isValid(type)) { //Consider as Bean
                return (S)new BeanElement(map);
            }
            else {
                return (S)new MapElement(map);
            }
        }
        else {
            throw new BeanSpecSyntaxException("No such kind:" + kind + " in version(" + getVersion() + ") ");
        }
    }
}
