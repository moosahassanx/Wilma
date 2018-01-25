package com.epam.browsermob.ssl;

public class ExternalCertificateInformation {
    String path; //proxy.ssl.cert.path
    String name; //proxy.ssl.cert.name
    String privateName; //proxy.ssl.cert.private
    String password; //proxy.ssl.cert.password - jetty.ssl.password
    String keyPassword; //proxy.ssl.cert.keyPassword - jetty.ssl.keypassword
    String alias; //proxy.ssl.cert.alias
    String privKeyAlias; //proxy.ssl.cert.privKeyAlias

    private ExternalCertificateInformation() {}

    public ExternalCertificateInformation(String path, String name, String privateName, String password, String keyPassword, String alias, String privKeyAlias) {
        this.path = path;
        this.name = name;
        this.privateName = privateName;
        this.password = password;
        this.keyPassword = keyPassword;
        this.alias = alias;
        this.privKeyAlias = privKeyAlias;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getPrivateName() {
        return privateName;
    }

    public String getPassword() {
        return password;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public String getAlias() {
        return alias;
    }

    public String getPrivKeyAlias() {
        return privKeyAlias;
    }


}
