package com.galsie.lib.certificates.csr;

import com.galsie.lib.certificates.dn.InternalDNBuilder;
import com.galsie.lib.certificates.keypair.InternalKeypairBuilder;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class CertificateSigningRequestBuilder {
    protected final InternalDNBuilder<CertificateSigningRequestBuilder> subjectDNBuilder = new InternalDNBuilder(this);

    /**
     * The Keypair (private & public keys) of an X.509 certificate can be generated through:
     * - Different algorithms & algorithm parameters
     * - Different Security Providers
     */
    private InternalKeypairBuilder<CertificateSigningRequestBuilder> keypairBuilder = new InternalKeypairBuilder(this);


    /**
     * The 'Subject' field of the certificate holds a Distinguished Name
     * - Through this method, you access the Subject Distinguished name Builder
     *
     * @return The Subject DN Builder
     */
    public InternalDNBuilder<CertificateSigningRequestBuilder> subjectDN() {
        return this.subjectDNBuilder;
    }

    /**
     * The Certificate has a private & public key, these can be generated trough different algorithms with different providers
     * - The keypair builder allows configuring this.
     * - Through this method, you access the Keypair builder
     *
     * @return The Subject DN Builder
     */
    public InternalKeypairBuilder<CertificateSigningRequestBuilder> keypair() {
        return this.keypairBuilder;
    }

    public SomeCSRManager build() throws Exception {
        // Get the subject name
        X500Name subjectName = this.subjectDNBuilder.getDistinguishedNameBuilder().build();

        // Generate the keypair
        KeyPair keyPair = keypairBuilder.build();  // Assuming you have a build method in your keypairBuilder
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // CSR builder
        JcaPKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                subjectName, publicKey);

        // Signing the CSR using SHA-256 with ECDSA
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withECDSA").setProvider("BC").build(privateKey);

        // Build and return the CSR
        PKCS10CertificationRequest csr = p10Builder.build(signer);
        return new SomeCSRManager(keyPair, csr);
    }


    public static CertificateSigningRequestBuilder start(){
        return new CertificateSigningRequestBuilder();
    }

}
