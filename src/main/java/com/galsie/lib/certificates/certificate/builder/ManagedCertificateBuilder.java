package com.galsie.lib.certificates.certificate.builder;

import com.galsie.lib.certificates.certificate.SomeX509v3CertificateHolder;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import com.galsie.lib.certificates.keypair.InternalKeypairBuilder;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.util.Date;


/**
 * Common Certificate builder Implementation, implement parameters and methods for:
 * - Creating the Subject DN
 * - Setting the Extensions
 * - Setting the Serial Number
 * - Setting the Certificate Validity
 * - Building to {@link SomeX509v3CertificateManager} as a self-signed certificate
 * - Building to {@link }
 *
 * @param <T> A type reference to the class that extends this class.
 *            - This aims at matching the return types of the methods to the inheriting class
 *
 */
public abstract class ManagedCertificateBuilder<T extends CertificateBuilder> extends CertificateBuilderCommonImpl<T> {


    /**
     * The Keypair (private & public keys) of an X.509 certificate can be generated through:
     * - Different algorithms & algorithm parameters
     * - Different Security Providers
     *
     */
    protected final InternalKeypairBuilder<T> keypairBuilder = new InternalKeypairBuilder<T>((T) this);


    /**
     * The Certificate has a private & public key, these can be generated trough different algorithms with different providers
     * - The keypair builder allows configuring this.
     * - Through this method, you access the Keypair builder
     *
     * @return The Subject DN Builder
     */
    public InternalKeypairBuilder<T> keypair() {
        return this.keypairBuilder;
    }




    /**
     * Generates a KeyPair using the ECDSA algorithm with the secp256r1 curve.
     * - This requirement is based on matters certificate requirements.
     *
     * @return The generated keyPair
     * @throws NoSuchAlgorithmException           if the ECDSA algorithm wasn't found for some reason
     * @throws NoSuchProviderException            If the Bouncy Castle provider wasn't found
     * @throws InvalidAlgorithmParameterException If the Elliptic curve specified 'secp256r1' wasn't found
     */
    protected KeyPair generateKeypair() throws Exception {
        this.keypairBuilder.build();
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
        g.initialize(ecSpec, new SecureRandom());
        return g.generateKeyPair();
    }



    /**
     * Builds as a Self Signed Certificate where:
     * - The subject & issuer hold the same Distinguished Name
     * - The Keypair is generated through {@link ManagedCertificateBuilder#generateKeypair()}
     * - Most importantly, the certificate is signed using the generated keypair
     * <p>
     * Usually, The Root Certificate Authority Certificate is Self-Signed
     * - Note: This itself does not make the certificate a Certificate Authority, to do so, the BasicConstraints extension must be used.
     *
     * @return {@link SomeX509v3CertificateManager}
     * @throws NoSuchAlgorithmException           if the ECDSA algorithm wasn't found for some reason
     * @throws NoSuchProviderException            If the Bouncy Castle provider wasn't found
     * @throws InvalidAlgorithmParameterException If the Elliptic curve specified 'secp256r1' wasn't found
     * @throws CertIOException                    If one of the extensions failed to be added
     * @throws OperatorCreationException          If the certificate signing failed
     */
    public SomeX509v3CertificateManager buildAsSelfSigned() throws Exception {
        KeyPair keyPair = this.generateKeypair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        X500Name subjectName = this.subjectDNBuilder.getDistinguishedNameBuilder().build();
        X500Name issuerName = subjectName; // Self signed
        // Build the certificate
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuerName,
                new BigInteger(1, this.serialNumber),
                this.validFrom,
                this.getValidTo(),
                subjectName,
                publicKey
        );
        this.auxAddExtensions(certBuilder, publicKey, publicKey);

        String signingAlgo = signingHashingAlgorithm.getAlgorithmIdentifier().replaceAll("-","") + "with" + publicKey.getAlgorithm();
        ContentSigner signer = new JcaContentSignerBuilder(signingAlgo).setProvider("BC").build(privateKey);
        SomeX509v3CertificateHolder certificateHolder = new SomeX509v3CertificateHolder(certBuilder.build(signer));
        return new SomeX509v3CertificateManager(keyPair, certificateHolder);
    }

    public SomeX509v3CertificateManager buildSignedBy(SomeX509v3CertificateManager x509V3CertificateManager) throws Exception {
        KeyPair keyPair = this.generateKeypair();
        PublicKey publicKey = keyPair.getPublic();

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
        SomeX509v3CertificateHolder certificateHolder = x509V3CertificateManager.signCertificate(certBuilder, HashingAlgorithm.SHA256);
        return new SomeX509v3CertificateManager(keyPair, certificateHolder);
    }

}
