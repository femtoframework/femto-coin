package org.femtoframework.coin.json;

import com.jsoniter.annotation.*;
import com.jsoniter.spi.Config;
import com.jsoniter.spi.Decoder;
import com.jsoniter.spi.Encoder;
import org.femtoframework.coin.annotation.*;

import java.lang.annotation.Annotation;

/**
 * Jsoniter + COIN annotations
 */
public class CoinCompatibilityMode extends Config {

    public static class Builder extends Config.Builder {
        public CoinCompatibilityMode build() {
            return (CoinCompatibilityMode) super.build();
        }

        @Override
        protected Config doBuild(String configName) {
            return new CoinCompatibilityMode(configName, this);
        }

        @Override
        public String toString() {
            return super.toString() + " => CoinCompatibilityMode{}";
        }
    }

    public static final Config DEFAULT = new CoinCompatibilityMode.Builder().build();

    private CoinCompatibilityMode(String configName, CoinCompatibilityMode.Builder builder) {
        super(configName, builder);
    }

    @Override
    protected JsonIgnore getJsonIgnore(Annotation[] annotations) {
        JsonIgnore jsoniterObj = super.getJsonIgnore(annotations);
        if (jsoniterObj != null) {
            return jsoniterObj;
        }
        final Ignore jacksonObj = getAnnotation(annotations, Ignore.class);
        if (jacksonObj == null) {
            return null;
        }
        return new JsonIgnore() {
            @Override
            public boolean ignoreDecoding() {
                return true;
            }

            @Override
            public boolean ignoreEncoding() {
                return true;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonIgnore.class;
            }
        };
    }

    @Override
    protected JsonProperty getJsonProperty(Annotation[] annotations) {
        JsonProperty jsoniterObj = super.getJsonProperty(annotations);
        if (jsoniterObj != null) {
            return jsoniterObj;
        }
        final Property jacksonObj = getAnnotation(annotations, Property.class);
        if (jacksonObj == null) {
            return null;
        }
        return new JsonProperty() {
            @Override
            public String value() {
                return jacksonObj.value();
            }

            @Override
            public String[] from() {
                return new String[]{};
            }

            @Override
            public String[] to() {
                return new String[]{};
            }

            @Override
            public boolean required() {
                return jacksonObj.required();
            }

            @Override
            public Class<? extends Decoder> decoder() {
                return Decoder.class;
            }

            @Override
            public Class<?> implementation() {
                return Object.class;
            }

            @Override
            public Class<? extends Encoder> encoder() {
                return Encoder.class;
            }

            @Override
            public boolean nullable() {
                return !jacksonObj.required();
            }

            @Override
            public boolean collectionValueNullable() {
                return !jacksonObj.required();
            }

            @Override
            public String defaultValueToOmit() {
                return jacksonObj.defaultValue();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonProperty.class;
            }
        };
    }

    @Override
    protected JsonCreator getJsonCreator(Annotation[] annotations) {
        JsonCreator jsoniterObj = super.getJsonCreator(annotations);
        if (jsoniterObj != null) {
            return jsoniterObj;
        }
        return null;
//        return new JsonCreator() {
//            @Override
//            public Class<? extends Annotation> annotationType() {
//                return JsonCreator.class;
//            }
//        };
    }

    @Override
    protected JsonUnwrapper getJsonUnwrapper(Annotation[] annotations) {
        JsonUnwrapper jsoniterObj = super.getJsonUnwrapper(annotations);
        if (jsoniterObj != null) {
            return jsoniterObj;
        }
        AnyGetter jacksonObj = getAnnotation(annotations, AnyGetter.class);
        AsValue asValue = getAnnotation(annotations, AsValue.class);
        if (jacksonObj == null && asValue == null) {
            return null;
        }
        return new JsonUnwrapper() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonUnwrapper.class;
            }
        };
    }

    @Override
    protected JsonWrapper getJsonWrapper(Annotation[] annotations) {
        JsonWrapper jsoniterObj = super.getJsonWrapper(annotations);
        if (jsoniterObj != null) {
            return jsoniterObj;
        }
        AnySetter jacksonObj = getAnnotation(annotations, AnySetter.class);
        if (jacksonObj == null) {
            return null;
        }
        return new JsonWrapper() {
            @Override
            public JsonWrapperType value() {
                return JsonWrapperType.KEY_VALUE;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonWrapper.class;
            }
        };
    }
}
