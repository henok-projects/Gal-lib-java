package com.galsie.lib.certificates.extension;

import com.galsie.lib.certificates.certificate.builder.CertificateBuilder;
import com.galsie.lib.utils.builder.InternalBuilder;
import com.galsie.lib.utils.pair.Pair;
import lombok.Getter;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CertificateExtensionsBuilder is a helper used as by subclass builders of {@link CertificateBuilder}
 * - It holds a {@link DistinguishedNameBuilder} to build the distinguished Name
 * - It holds a {@link ManagedCertificateBuilder} to go back to the builder which holds this Distinguished Name
 * @param <T> A builder subclass of {@link CertificateBuilder}
 *           - Used to return to the builder (through calling the {@link InternalDNBuilder#done()} method
 */
public class CertificateExtensionsBuilder<T extends CertificateBuilder> extends InternalBuilder<T> {

    /**
     * Critical Extensions must be processed when present. They need not be present though
     * - The Matter protocol within the context of the X.509 v3 Certificate defines these extensions as critical
     *
     * Note: These are not used by the builder, they are here for reference only
     */
    public static List<ASN1ObjectIdentifier> CRITICAL_EXTENSIONS = Arrays.asList(Extension.basicConstraints, Extension.keyUsage, Extension.extendedKeyUsage);

    /**
     * The {@link CertificateBuilder} Which this extensions builder belong to
     */

    /**
     * A structure used by bouncy castle to build extensions, it was re-used here for convenience
     */
    private ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();

    /**
     * delayed extensions are not added to the extension generator, they are used in another builder
     *
     * This is currently used for:
     * - authority key identifier and subject key identifier
     */
    @Getter
    private List<Pair<ASN1ObjectIdentifier, Boolean>> delayedExtensions = new ArrayList<>();
    /**
     * Initializes the Certificate Extensions Builder
     * - The {@link CertificateBuilder} is expected for convenient chaining operations
     * @param certificateBuilder The {@link CertificateBuilder} holding this {@link CertificateExtensionsBuilder}
     */
    public CertificateExtensionsBuilder(T certificateBuilder) {
        super(certificateBuilder);
    }


    /**
     * Adds an extension
     * @param objectIdentifier The Extensions Object Identifier
     * @param isCritical Whether the Extension is Critical or Not. When 'true', the entity processing the certificate must parse this extension.
     *                   - Note that the X.509 v3 & Matter protocol specify whether extensions are critical or not
     * @param value The value this extension holds, the type of this value is based on the Extensions requirements and must be a subclass of ASN1Encodable
     */
    public CertificateExtensionsBuilder<T> addExtension(ASN1ObjectIdentifier objectIdentifier, boolean isCritical, ASN1Encodable value) throws IOException {
        this.extensionsGenerator.addExtension(objectIdentifier, isCritical, value);
        return this;
    }

    /**
     * Adds an extension
     * - Encodes the value as a DEROctetString
     * @param objectIdentifier The Extensions Object Identifier
     *      * @param isCritical Whether the Extension is Critical or Not. When 'true', the entity processing the certificate must parse this extension.
     *                   - Note that the X.509 v3 & Matter protocol specify whether extensions are critical or not
     * @param value A byte array holding the value
     *              - The value will be encoded as a DEROctetString
     */
    public CertificateExtensionsBuilder<T> addExtension(ASN1ObjectIdentifier objectIdentifier, boolean isCritical, byte[] value) {
        this.extensionsGenerator.addExtension(objectIdentifier, isCritical, value);
        return this;
    }

    /**
     * Adds an extension
     * - Automatically sets the extension as critical if its in {@link CertificateExtensionsBuilder#CRITICAL_EXTENSIONS}
     * @param objectIdentifier The Extensions Object Identifier
     * @param value A subclass of ASN1Encodable
     * @return This builder for chaining operations
     * @throws IOException If the extension failed to add, usually due to a wrong value
     */
    public CertificateExtensionsBuilder<T> addExtension(ASN1ObjectIdentifier objectIdentifier, ASN1Encodable value) throws IOException {
        this.addExtension(objectIdentifier, CRITICAL_EXTENSIONS.contains(objectIdentifier), value);
        return this;
    }

    /**
     * - Encodes the value as a DEROctetString
     * - Automatically sets the extension as critical if its in {@link CertificateExtensionsBuilder#CRITICAL_EXTENSIONS}
     *
     * @param objectIdentifier
     * @param value
     * @return This builder for chaining operations
     */
    public CertificateExtensionsBuilder<T> addExtension(ASN1ObjectIdentifier objectIdentifier, byte[] value) {
        this.addExtension(objectIdentifier, CRITICAL_EXTENSIONS.contains(objectIdentifier), value);
        return this;
    }

    /**
     * The key-usage extension defines the purpose of the key contained in the certificate
     * - This is a Critical Extension
     *
     * @param keyUsageId An identifier for the key usage.
     *                   - Can be constructed with bitwise operations KeyUsage.keyCertSign | KeyUsage.cRLSign
     * @return This builder for chaining operations
     */
    public CertificateExtensionsBuilder<T> setKeyUsage(int keyUsageId) throws IOException {
        this.addExtension(Extension.keyUsage, true, new KeyUsage(keyUsageId));
        return this;
    }

    /**
     * Sets the current certificate as a Certificate Authority (CA) with a specified path length constraint.
     * The path length constraint defines the number of non-self-issued intermediate certificates
     * that may follow this certificate in a valid certification path.
     *
     * @param pathLengthConstraint The maximum number of non-self-issued intermediate certificates
     *                             that may follow this certificate in a valid certification path.
     * @return CertificateExtensionsBuilder The current instance of CertificateExtensionsBuilder
     *                                      to allow for method chaining.
     * @throws IOException If there's an issue adding the BasicConstraints extension.
     */
    public CertificateExtensionsBuilder<T> setAsCertificateAuthority(int pathLengthConstraint) throws IOException {
        this.addExtension(Extension.basicConstraints, new BasicConstraints(pathLengthConstraint));
        return this;
    }

    /**
     * Sets the current certificate as a Certificate Authority (CA) without specifying a path length constraint.
     * This means the certificate can be used as a CA to sign other certificates,
     * but there's no restriction on the depth of the certificate chain.
     *
     * @return CertificateExtensionsBuilder The current instance of CertificateExtensionsBuilder
     *                                      to allow for method chaining.
     * @throws IOException If there's an issue adding the BasicConstraints extension.
     */
    public CertificateExtensionsBuilder<T> setAsCertificateAuthority() throws IOException {
        this.addExtension(Extension.basicConstraints, new BasicConstraints(true));
        return this;
    }

    public CertificateExtensionsBuilder<T> withSubjectKeyIdentifier(boolean isCritical){
        this.delayedExtensions.add(Pair.of(Extension.subjectKeyIdentifier, isCritical));
        return this;
    }

    public CertificateExtensionsBuilder<T> withSubjectKeyIdentifier(){
        var subjectKeyIdentifier = Extension.subjectKeyIdentifier;
        this.delayedExtensions.add(Pair.of(subjectKeyIdentifier, CRITICAL_EXTENSIONS.contains(subjectKeyIdentifier)));
        return this;
    }

    public CertificateExtensionsBuilder<T> withAuthorityKeyIdentifier(boolean isCritical){
        this.delayedExtensions.add(Pair.of(Extension.authorityKeyIdentifier, isCritical));
        return this;
    }

    public CertificateExtensionsBuilder<T> withAuthorityKeyIdentifier(){
        var authorityKeyIdentifier = Extension.authorityKeyIdentifier;
        this.delayedExtensions.add(Pair.of(authorityKeyIdentifier, CRITICAL_EXTENSIONS.contains(authorityKeyIdentifier)));
        return this;
    }


    public List<Extension> getExtensions(){
        Extensions extensions = this.extensionsGenerator.generate();
        return Arrays.stream(extensions.getExtensionOIDs()).map(extensions::getExtension).collect(Collectors.toList());
    }

}