package com.galsie.lib.certificates.certificate;

import com.galsie.lib.certificates.PEMStructureHolder;
import com.galsie.lib.certificates.csr.SomeCSRHolder;
import lombok.Getter;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;

import java.io.IOException;
import java.io.StringReader;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Holds an X509v3 Certificate
 * Extends X509CertificateHolder with methods to encode and decode from a base64 string
 *
 */
@Getter
public class SomeX509v3CertificateHolder extends X509CertificateHolder implements PEMStructureHolder {

    private PublicKey publicKey;
    public SomeX509v3CertificateHolder(byte[] bytes) throws IOException {
        super(bytes);
        this.publicKey = SomeCSRHolder.getPublicKeyFromSPKI(this.getSubjectPublicKeyInfo());
    }

    public SomeX509v3CertificateHolder(Certificate certificate) throws PEMException {
        super(certificate);
        this.publicKey = SomeCSRHolder.getPublicKeyFromSPKI(this.getSubjectPublicKeyInfo());
    }

    public SomeX509v3CertificateHolder(X509CertificateHolder certificateHolder) throws PEMException {
        super(certificateHolder.toASN1Structure());
        this.publicKey = SomeCSRHolder.getPublicKeyFromSPKI(this.getSubjectPublicKeyInfo());

    }

    @Override
    public String getBase64DEREncoded() throws IOException {
        return Base64.getEncoder().encodeToString(this.getEncoded());
    }

    @Override
    public String getPEMEncoded() throws IOException {
        return "-----BEGIN CERTIFICATE-----\n"
                + this.getBase64DEREncoded()
                + "\n"
                + "-----END CERTIFICATE-----";
    }

    public static SomeX509v3CertificateHolder fromBase64EncodedDER(String base64) throws IOException {
        byte[] data = Base64.getDecoder().decode(base64);
        return fromDERData(data);
    }
    public static SomeX509v3CertificateHolder fromDERData(byte[] data) throws IOException {
        return new SomeX509v3CertificateHolder(data);
    }

    public static SomeX509v3CertificateHolder fromPEMEncoded(String pemEncoded) throws Exception {
        var baseCertHolder = SomeX509v3CertificateHolder.parseCertificateHolderFromPEM(pemEncoded);
        return new SomeX509v3CertificateHolder(baseCertHolder);
    }

    private static X509CertificateHolder parseCertificateHolderFromPEM(String pemCertificate) throws Exception {
        try (PEMParser pemParser = new PEMParser(new StringReader(pemCertificate))) {
            Object parsedObj = pemParser.readObject();
            if (parsedObj instanceof X509CertificateHolder) {
                return (X509CertificateHolder) parsedObj;
            }
            throw new IllegalArgumentException("Provided PEM data is not a valid X.509 certificate");
        }
    }
}
