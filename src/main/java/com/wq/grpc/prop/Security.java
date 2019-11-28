package com.wq.grpc.prop;

import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
public class Security {

    private Boolean clientAuthEnabled;
    private static final boolean DEFAULT_CLIENT_AUTH_ENABLED = false;
    private String certificateChain = null;
    private String privateKey = null;
    private String privateKeyPassword = null;
    private String trustCertCollection = null;
    private String authorityOverride = null;
    private List<String> ciphers = null;
    private String[] protocols = null;
    private Boolean basicAuthEnabled;
    private String user;
    private String pwd;

    public boolean isClientAuthEnabled() {
        return this.clientAuthEnabled == null ? DEFAULT_CLIENT_AUTH_ENABLED : this.clientAuthEnabled;
    }

    public void setClientAuthEnabled(final Boolean clientAuthEnabled) {
        this.clientAuthEnabled = clientAuthEnabled;
    }

    public String getCertificateChain() {
        return this.certificateChain;
    }

    public void setCertificateChain(final String certificateChain) {
        this.certificateChain = certificateChain;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyPassword() {
        return this.privateKeyPassword;
    }

    public void setPrivateKeyPassword(final String privateKeyPassword) {
        this.privateKeyPassword = privateKeyPassword;
    }

    public String getTrustCertCollection() {
        return this.trustCertCollection;
    }

    public void setTrustCertCollection(final String trustCertCollection) {
        this.trustCertCollection = trustCertCollection;
    }

    public String getAuthorityOverride() {
        return this.authorityOverride;
    }

    public void setAuthorityOverride(final String authorityOverride) {
        this.authorityOverride = authorityOverride;
    }

    public List<String> getCiphers() {
        return this.ciphers;
    }

    public void setCiphers(final String ciphers) {
        if (ciphers == null) {
            this.ciphers = null;
        } else {
            this.ciphers = Arrays.asList(ciphers.split("[ :,]"));
        }
    }

    public String[] getProtocols() {
        return this.protocols;
    }

    public void setProtocols(final String protocols) {
        if (protocols == null) {
            this.protocols = null;
        } else {
            this.protocols = protocols.split("[ :,]");
        }
    }

    public void copyAllValueIfNullFrom(final Security config) {
        if (this == config) {
            return;
        }
        if (this.clientAuthEnabled == null) {
            this.clientAuthEnabled = config.clientAuthEnabled;
        }
        if (this.certificateChain == null) {
            this.certificateChain = config.certificateChain;
        }
        if (this.privateKey == null) {
            this.privateKey = config.privateKey;
        }
        if (this.privateKeyPassword == null) {
            this.privateKeyPassword = config.privateKeyPassword;
        }
        if (this.trustCertCollection == null) {
            this.trustCertCollection = config.trustCertCollection;
        }
        if (this.authorityOverride == null) {
            this.authorityOverride = config.authorityOverride;
        }
        if (this.ciphers == null) {
            this.ciphers = config.ciphers;
        }
        if (this.protocols == null) {
            this.protocols = config.protocols;
        }
        if (this.basicAuthEnabled == null) {
            this.basicAuthEnabled = config.basicAuthEnabled;
            this.user = config.user;
            this.pwd = config.pwd;
        }
    }

    public Boolean getBasicAuthEnabled() {
        return basicAuthEnabled;
    }

    public void setBasicAuthEnabled(Boolean basicAuthEnabled, String user, String pwd) {
        this.basicAuthEnabled = basicAuthEnabled;
        this.user = user;
        this.pwd = pwd;
    }

    public Boolean getClientAuthEnabled() {
        return clientAuthEnabled;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }
}