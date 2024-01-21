package com.galsie.lib.certificates.certificate.builder;


/**
 * Builds certificates that are signed by a {@link com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager}
 * Can handle building certificates from CSRs
 */
public class AnyUnmanagedCertificateBuilder extends UnmanagedCertificateBuilder<AnyUnmanagedCertificateBuilder>{
    public static AnyUnmanagedCertificateBuilder start(){
        return new AnyUnmanagedCertificateBuilder();
    }


}
