package com.galsie.lib.certificates.csr;

import com.galsie.lib.certificates.PEMStructureHolder;
import com.galsie.lib.certificates.asn1.codable.ASN1CodableUtils;
import com.galsie.lib.certificates.asn1.object.AbstractASN1ObjectIdentifier;
import lombok.Getter;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;

import java.io.IOException;
import java.io.StringReader;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;

@Getter
public class SomeCSRHolder implements PEMStructureHolder {
    private PKCS10CertificationRequest pkcs10CertificationRequest;
    private PublicKey publicKey;
    public SomeCSRHolder(PKCS10CertificationRequest pkcs10CertificationRequest) throws PEMException {
        this.pkcs10CertificationRequest = pkcs10CertificationRequest;
        this.publicKey = getPublicKeyFromSPKI(pkcs10CertificationRequest.getSubjectPublicKeyInfo());
    }

    /**
     * Checks if a signature is valid
     * @param certificationRequest The CSR for which we need to check if th signature is valid
     * @return True if the signature is valid
     * @throws PKCSException
     * @throws OperatorCreationException
     */
    public static boolean isSignatureValid(PKCS10CertificationRequest certificationRequest) throws PKCSException, OperatorCreationException {
        JcaPKCS10CertificationRequest jcaRequest = new JcaPKCS10CertificationRequest(certificationRequest);
        ContentVerifierProvider verifierProvider = new JcaContentVerifierProviderBuilder()
                .build(jcaRequest.getSubjectPublicKeyInfo());
        return jcaRequest.isSignatureValid(verifierProvider);
    }


    public Optional<String> getSubjectRDNValueFor(AbstractASN1ObjectIdentifier abstractASN1ObjectIdentifier) throws Exception {
        for (RDN rdn: pkcs10CertificationRequest.getSubject().getRDNs()){
            for (var typeAndValue: rdn.getTypesAndValues()){
                if (!typeAndValue.getType().getId().equals(abstractASN1ObjectIdentifier.getASN1OID())) {
                    continue;
                }
                var asn1EncodableValue = typeAndValue.getValue();
                return Optional.of(ASN1CodableUtils.extractUtf8OrPrintableStringFrom(asn1EncodableValue));
            }
        }
        return Optional.empty();
    }
    @Override
    public String getBase64DEREncoded() throws IOException {
        return Base64.getEncoder().encodeToString(pkcs10CertificationRequest.getEncoded());
    }

    @Override
    public String getPEMEncoded() throws IOException {
        return "-----BEGIN CERTIFICATE REQUEST-----\n"
                + this.getBase64DEREncoded()
                + "\n"
                + "-----END CERTIFICATE REQUEST-----\n";
    }

    public static SomeCSRHolder fromBase64Encoded(String base64Encoded) throws IOException {
        var csr = new PKCS10CertificationRequest(Base64.getDecoder().decode(base64Encoded));
        return new SomeCSRHolder(csr);
    }

    public static SomeCSRHolder fromPEMEncoded(String pemEncoded) throws Exception {
        var certificationRequest = SomeCSRHolder.parsePKCS10CSRfromPEM(pemEncoded);
        return new SomeCSRHolder(certificationRequest);
    }


    /**
     * Extracts the PublicKey from a given SubjectPublicKeyInfo.
     *
     * @param spki the SubjectPublicKeyInfo
     * @return a PublicKey
     */
    public static PublicKey getPublicKeyFromSPKI(SubjectPublicKeyInfo spki) throws PEMException {
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        return converter.getPublicKey(spki);
    }

    public static PKCS10CertificationRequest parsePKCS10CSRfromPEM(String pemCSR) throws Exception{
        try (PEMParser pemParser = new PEMParser(new StringReader(pemCSR))) {
            Object parsedObj = pemParser.readObject();
            if (parsedObj instanceof PKCS10CertificationRequest) {
                return (PKCS10CertificationRequest) parsedObj;
            }
            throw new IllegalArgumentException("Provided PEM data is not a valid Certificate Siging Request");
        }
    }
}
