package com.galsie.lib.certificates.csr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.io.IOException;
import java.security.KeyPair;

@AllArgsConstructor
@Getter
public class SomeCSRManager {
    private KeyPair keyPair;
    private SomeCSRHolder csrHolder;

    public SomeCSRManager(KeyPair keyPair, PKCS10CertificationRequest pkcs10CertificationRequest) throws PEMException {
        this(keyPair, new SomeCSRHolder(pkcs10CertificationRequest));
    }

    public String getBase64DEREncoded() throws IOException {
        return this.csrHolder.getBase64DEREncoded();
    }

    public String getPEMEncoded() throws IOException{
        return this.csrHolder.getPEMEncoded();
    }
}
