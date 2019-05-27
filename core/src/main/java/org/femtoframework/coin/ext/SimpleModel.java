package org.femtoframework.coin.ext;

import org.femtoframework.coin.Model;
import org.femtoframework.coin.spec.Kind;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.util.CollectionUtil;

import java.util.UUID;

public class SimpleModel<SPEC, STATUS> implements Model<SPEC, STATUS> {

    private SPEC spec;
    private STATUS status;
    private String name;
    private String namespace;
    private long creationTimestamp;
    private String apiVersion;
    private Kind kind;
    private String uid;

    public SimpleModel() {
        this.uid = UUID.randomUUID().toString().toLowerCase();
    }

    /**
     * Return the SPEC of COIN Object
     *
     * @return Spec
     */
    @Override
    public SPEC getSpec() {
        return spec;
    }

    /**
     * Return the status of COIN Object
     *
     * @return Status
     */
    @Override
    public STATUS getStatus() {
        return status;
    }

    /**
     * Return generated name
     *
     * @return generated name
     */
    @Override
    public String getGenerateName() {
        return getName();
    }

    /**
     * Return namespace of current object
     *
     * @return Namespace
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * System generated UID
     *
     * @return UID
     */
    @Override
    public String getUid() {
        return uid;
    }

    /**
     * Creation timestamp
     *
     * @return Creation Timestamp
     */
    @Override
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Labels
     *
     * @return Labels
     */
    @Override
    public Parameters<String> getLabels() {
        return CollectionUtil.emptyParameters();
    }

    /**
     * Annotations
     *
     * @return Annotations
     */
    @Override
    public Parameters<String> getAnnotations() {
        return CollectionUtil.emptyParameters();
    }

    /**
     * Name of the object
     *
     * @return Name of the object
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Return the Kind of the Type
     *
     * @return Kind
     */
    @Override
    public Kind getKind() {
        return kind;
    }

    /**
     * API Version
     *
     * @return API Version
     */
    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    public void setSpec(SPEC spec) {
        this.spec = spec;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }
}
