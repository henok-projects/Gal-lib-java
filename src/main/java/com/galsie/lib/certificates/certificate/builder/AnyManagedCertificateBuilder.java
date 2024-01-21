package com.galsie.lib.certificates.certificate.builder;

/**
    Can build self-signed certificates, and non-self-signed certificates
 */
public class AnyManagedCertificateBuilder extends ManagedCertificateBuilder<AnyManagedCertificateBuilder> {

    AnyManagedCertificateBuilder(){
        super();
    }
    public static AnyManagedCertificateBuilder start(){
        return new AnyManagedCertificateBuilder();
    }

}
