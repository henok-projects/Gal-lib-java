package com.galsie.lib.certificates.certificate;

import com.galsie.lib.certificates.keypair.KeyUtils;
import com.galsie.lib.utils.crypto.coder.Coder;
import com.galsie.lib.utils.crypto.coder.CodingAlgorithm;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SomeX509v3CertificateManager {

    KeyPair keyPair;
    SomeX509v3CertificateHolder x509CertificateHolder;


    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public PublicKey getPublicKey(){
        return keyPair.getPublic();
    }

    public String getBase64EncodedPublicKey(){
        return Coder.encode(CodingAlgorithm.BASE64, this.getPublicKey().getEncoded());
    }

    public String getBase64EncodedPrivateKey(){
        return Coder.encode(CodingAlgorithm.BASE64, this.getPrivateKey().getEncoded());
    }

    public byte[] getEncodedCertificate() throws IOException {
        return this.getX509CertificateHolder().getEncoded();
    }
    public String getBase64EncodedCertificate() throws IOException {
        return this.getX509CertificateHolder().getBase64DEREncoded();
    }

    public String getPEMEncoded() throws IOException{
        return this.getX509CertificateHolder().getPEMEncoded();
    }
    public SomeX509v3CertificateHolder signCertificate(X509v3CertificateBuilder x509v3CertificateBuilder, HashingAlgorithm hashingAlgorithm) throws OperatorCreationException, PEMException {
        String signingAlgo = hashingAlgorithm.getAlgorithmIdentifier().replaceAll("-","") + "with" + getPublicKey().getAlgorithm();
        ContentSigner contentSigner = new JcaContentSignerBuilder(signingAlgo).setProvider("BC").build(getPrivateKey()); // signing with the root's private key
        var x509CertificateHolder = x509v3CertificateBuilder.build(contentSigner);
        return new SomeX509v3CertificateHolder(x509CertificateHolder);
    }

    /*
    Factory
     */
    public static SomeX509v3CertificateManager fromPrivateKeyAndDERData(String base64EncodedPrivateKey, String base64EncodedDer) throws Exception {
        byte[] decodedKey = Coder.decode(CodingAlgorithm.BASE64, base64EncodedPrivateKey);
        byte[] decodedDER = Coder.decode(CodingAlgorithm.BASE64, base64EncodedDer);
        return fromPrivateKeyAndDERData(decodedKey,decodedDER);
    }
    public static SomeX509v3CertificateManager fromPrivateKeyAndDERData(String base64EncodedPrivateKey, byte[] derData) throws Exception {
        byte[] decodedKey = Coder.decode(CodingAlgorithm.BASE64, base64EncodedPrivateKey);
        return fromPrivateKeyAndDERData(decodedKey, derData);
    }

    public static SomeX509v3CertificateManager fromPrivateKeyAndDERData(byte[] privateKeyData, byte[] derData) throws Exception {
        var certificateHolder = SomeX509v3CertificateHolder.fromDERData(derData);

        var publicKey = certificateHolder.getPublicKey();
        var privateKey = KeyUtils.decodePrivateKey(privateKeyData, publicKey.getAlgorithm());

        var keypair =  new KeyPair(publicKey, privateKey);
        return new SomeX509v3CertificateManager(keypair, certificateHolder);
    }

}
