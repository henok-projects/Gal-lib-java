package com.galsie.lib.certificates.dn;

import com.galsie.lib.certificates.certificate.builder.CertificateBuilder;
import com.galsie.lib.certificates.asn1.object.AbstractASN1ObjectIdentifier;
import com.galsie.lib.certificates.exception.MaxSupportedRDNCountExceededException;
import com.galsie.lib.utils.builder.InternalBuilder;

public class InternalDNBuilder<T> extends InternalBuilder<T> {
    private DistinguishedNameBuilder distinguishedNameBuilder = new DistinguishedNameBuilder();

    /**
     * Initializes the Internal Builder for an External Builder
     * - The outerBuilder is expected for convenient chaining operations
     *
     * @param outerBuilder The Outer Builder holding this Internal Builder
     */
    public InternalDNBuilder(T outerBuilder) {
        super(outerBuilder);
    }

    /**
     * Initializes the Distinguished Name Builder
     * - The {@link CertificateBuilder} is expected for convenient chaining operations
     * @param certificateBuilder The {@link CertificateBuilder} holding this {@link InternalDNBuilder}
     */
    /**
     * Adds a distinguished name to the subject
     * @param dnObjectIdentifier The ASN1 object identifier (dot joined string)
     * @param value The value, whatever its datatype is based on the requirements of the DN, encoded to a string
     * @return
     */
    public InternalDNBuilder<T> addRDN(String dnObjectIdentifier, String value) throws MaxSupportedRDNCountExceededException {
        this.distinguishedNameBuilder.addRDN(dnObjectIdentifier, value);
        return this;
    }

    /**
     * Adds a matter distinguished name to the subject
     * - This is equivalent to calling {@link InternalDNBuilder#addRDN(String, String) with the matter-specific dnObjectIdentifier}
     * @param dn A subclass of {@link AbstractASN1ObjectIdentifier} (dot joined string)
     *           -
     * @param value The value, whatever its datatype is based on the requirements of the DN, encoded to a string
     * @return
     */
    public InternalDNBuilder<T> addRDN(AbstractASN1ObjectIdentifier dn, String value) throws MaxSupportedRDNCountExceededException {
        this.distinguishedNameBuilder.addRDN(dn, value);
        return this;
    }

    /**
     * Not used while building, used to finally build.
     * @return The {@link DistinguishedNameBuilder} used to actually build the DN
     */
    public DistinguishedNameBuilder getDistinguishedNameBuilder(){
        return this.distinguishedNameBuilder;
    }
}
