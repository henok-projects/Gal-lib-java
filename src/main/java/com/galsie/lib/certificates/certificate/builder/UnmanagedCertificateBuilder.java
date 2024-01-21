package com.galsie.lib.certificates.certificate.builder;

import com.galsie.lib.certificates.certificate.SomeX509v3CertificateHolder;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import com.galsie.lib.certificates.csr.SomeCSRHolder;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.math.BigInteger;
import java.security.PublicKey;

public abstract class UnmanagedCertificateBuilder<T extends CertificateBuilder> extends CertificateBuilderCommonImpl<T>{

    PublicKey publicKey;

    public T setPublicKey(PublicKey publicKey){
        this.publicKey = publicKey;
        return (T) this;
    }

    /**
     * NOTE: There are some attributes that may not be accounted for (for instance, some CSRs include an email address as an attribute - noting that this is seperate from the one that may be included in the subject)
     * @param someCSRHolder The CSR holder
     * @param ignoreRequestedExtensions When true, the requested extensions of a CSR are ignored. When false, an exception is thrown since no system exists for accounting for them yet
     */
    public T forCertificateSigningRequest(SomeCSRHolder someCSRHolder, boolean ignoreRequestedExtensions) throws Exception {
        this.forCertificateSigningRequest(someCSRHolder.getPkcs10CertificationRequest(), ignoreRequestedExtensions);
        return (T) this;
    }

    /**
     * NOTE: There are some attributes that may not be accounted for (for instance, some CSRs include an email address as an attribute - noting that this is seperate from the one that may be included in the subject)
     * @param certificationRequest The CSR
     * @param ignoreRequestedExtensions When true, the requested extensions of a CSR are ignored. When false, an exception is thrown since no system exists for accounting for them yet
     */
    public T forCertificateSigningRequest(PKCS10CertificationRequest certificationRequest, boolean ignoreRequestedExtensions) throws Exception {

        if (!SomeCSRHolder.isSignatureValid(certificationRequest)){
            throw new Exception("Invalid Signature.");
        }
        var x500Name = certificationRequest.getSubject();
        var publicKey = SomeCSRHolder.getPublicKeyFromSPKI(certificationRequest.getSubjectPublicKeyInfo());

        this.subjectDN().getDistinguishedNameBuilder().addContentsOfX500Name(x500Name);
        this.publicKey = publicKey;
        if (!ignoreRequestedExtensions) {
            /**
             *
             * Extensions in CSR:
             * - A CSR can include extensions, such as key usage, extended key usage, subject alternative names (SANs), etc.,
             * - These are essentially "requests" or "suggestions" from the entity generating the CSR.
             * - The extensions in a CSR are part of the attributes section and can carry values specified by the requestor.
             *
             * Discretion of the CA:
             * - The actual issuance of these extensions in the final certificate is at the discretion of the Certificate Authority (CA).
             * - The CA may choose to honor these requests, modify them, or ignore them, based on its policies and the type of certificate being requested.
             * - For example, a CA might ignore a request for a specific key usage in a CSR if it doesn't align with the CA's policy for that type of certificate.
             *
             * Setting Values in CSR:
             * - When creating a CSR, the requestor can specify values for these extensions.
             * - For example, in a CSR, one can specify different domains in the SAN extension that the certificate should be valid for.
             * - Or, the CSR might specify that the requested certificate should be used for code signing or secure email.
             *
             * Validation by the CA:
             * - The CA typically performs its own validation of the information in the CSR, including any values in the extensions.
             * - This validation is crucial to maintain the trustworthiness of the certificates it issues.
             *
             * Security Considerations:
             * - It's essential for the CA to carefully evaluate these requests, as automatically including all extensions and their values from the CSR without proper validation could introduce security risks.
             */
            throw new Exception("NO SYSTEM EXISTS FOR ACCOUNTING FOR REQUESTED EXTENSIONS YET. You can do so manually through PKCS10CertificationRequest#getExtensions & act accordingly.");
        }
        // NOTE: The signature algorithm of the CSR matters insofar as it needs to be secure enough to authenticate the CSR.
        // IT DOES NOT dictate the signature algorithm which the CA should use. So we ignore it here
        return (T) this;
    }

    public SomeX509v3CertificateHolder buildSignedBy(SomeX509v3CertificateManager x509V3CertificateManager) throws Exception {
        PublicKey publicKey = this.publicKey;

        X500Name subjectName = this.subjectDNBuilder.getDistinguishedNameBuilder().build();
        X500Name issuerName = x509V3CertificateManager.getX509CertificateHolder().getSubject(); // The isser of this certificate is its signer
        // Build the certificate
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuerName,
                new BigInteger(1, this.serialNumber),
                this.validFrom,
                this.getValidTo(),
                subjectName,
                publicKey
        );
        this.auxAddExtensions(certBuilder, x509V3CertificateManager.getPublicKey(), publicKey);
        return x509V3CertificateManager.signCertificate(certBuilder, this.signingHashingAlgorithm);
    }

}
