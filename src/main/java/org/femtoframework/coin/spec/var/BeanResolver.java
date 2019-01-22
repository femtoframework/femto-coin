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
package org.femtoframework.coin.spec.var;

import org.femtoframework.coin.*;
import org.femtoframework.coin.exception.BeanSpecSyntaxException;
import org.femtoframework.coin.spec.VariableResolver;
import org.femtoframework.coin.spec.VariableSpec;
import org.femtoframework.coin.spi.CoinModuleAware;
import org.femtoframework.util.StringUtil;

import javax.naming.NamingException;

/**
 * Bean Resolver
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanResolver implements VariableResolver, CoinModuleAware {

    private CoinModule coinModule;

    /**
     * Resolve variable by spec
     *
     * @param <T> Convert it to type
     * @param var VariableSpec
     * @param expectedType
     * @param component
     * @return
     */
    @Override
    public <T> T resolve(VariableSpec var, Class<T> expectedType, Component component) {
        NamespaceFactory namespaceFactory = component.getNamespaceFactory();
        String prefix = var.getPrefix();
        Namespace namespace;
        if (StringUtil.isInvalid(prefix) || "b".equalsIgnoreCase(prefix)) {
            namespace = component.getCurrentNamespace();;
        }
        else {
            namespace = namespaceFactory.get(prefix);
            if (namespace == null) {
                throw new BeanSpecSyntaxException("No such namespace '" + prefix + "' in ${" + var.getName() + "}");
            }
        }
        String suffix = var.getSuffix();
        if (suffix == null) {
            return (T)namespace;
        }
        if (suffix.indexOf(CoinConstants.CHAR_DELIM) > 0) {
            try {
                return (T)coinModule.getLookup().lookup(namespace.getName() + CoinConstants.CHAR_COLON + var.getSuffix());
            } catch (NamingException e) {
                throw new BeanSpecSyntaxException("Bean name syntax exception:" + var.getName(), e);
            }
        }
        else {
            Component next = namespace.getComponentFactory().get(suffix);
            if (next == null) {
                throw new BeanSpecSyntaxException("No such component '" + suffix + "' in ${" + var.getName() + "}");
            }
            return next.getBean(expectedType);
        }
    }

    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
