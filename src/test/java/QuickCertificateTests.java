import com.galsie.lib.certificates.SecurityProvider;
import com.galsie.lib.certificates.asn1.object.GalsieASN1ObjectIdentifier;
import com.galsie.lib.certificates.asn1.object.MatterASN1ObjectIdentifier;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import com.galsie.lib.certificates.certificate.builder.AnyManagedCertificateBuilder;
import com.galsie.lib.certificates.certificate.builder.AnyUnmanagedCertificateBuilder;
import com.galsie.lib.certificates.csr.CertificateSigningRequestBuilder;
import com.galsie.lib.certificates.csr.SomeCSRHolder;
import com.galsie.lib.certificates.exception.MaxSupportedRDNCountExceededException;
import com.galsie.lib.certificates.keypair.algo.ECDSA;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

/**
 * TODO: write better tests, this was written in 5 minutes.
 */
public class QuickCertificateTests {

    @BeforeClass
    public static void setupProviders() throws Exception {
        for (SecurityProvider securityProvider: SecurityProvider.values()){
            securityProvider.registerProvider();
        }
    }
    @Test
    public void testCSR() throws Exception {
        String homeID = "200";
        String pemEncodedCSR = CertificateSigningRequestBuilder.start().keypair().setGenerationAlgorithm(ECDSA.SECP_256_R1).done().subjectDN().addRDN(GalsieASN1ObjectIdentifier.GALSIE_HOME_ID, homeID).done().build().getPEMEncoded();
        System.out.println("CSR:\n" + pemEncodedCSR);
        var decodedCSR = SomeCSRHolder.fromPEMEncoded(pemEncodedCSR);
        assert decodedCSR.getPEMEncoded().equals(pemEncodedCSR);

        var decodedHomeID = decodedCSR.getSubjectRDNValueFor(GalsieASN1ObjectIdentifier.GALSIE_HOME_ID);
        assert decodedHomeID.orElse("").equalsIgnoreCase(homeID);
    }

    @Test
    public void testSelfSignedRootCertificate() throws Exception {
        var rootCA = AnyManagedCertificateBuilder.start().keypair().setGenerationAlgorithm(ECDSA.SECP_256_R1).done().setValidFrom(new Date()).subjectDN().addRDN(MatterASN1ObjectIdentifier.MATTER_RCAC_ID, "1").done().setSerialNumber("019123123123").buildAsSelfSigned();
        System.out.println("ROOT CERT: \n" + rootCA.getPEMEncoded());
    }

    @Test
    public void testSigningCSR() throws Exception {
        String homeID = "200";
        var csrManager = CertificateSigningRequestBuilder.start().keypair().setGenerationAlgorithm(ECDSA.SECP_256_R1).done().subjectDN().addRDN(GalsieASN1ObjectIdentifier.GALSIE_HOME_ID, homeID).done().build();
        System.out.println("CSR:\n" + csrManager.getPEMEncoded());

        var rootCA = AnyManagedCertificateBuilder.start().keypair().setGenerationAlgorithm(ECDSA.SECP_256_R1).done().setValidFrom(new Date()).subjectDN().addRDN(MatterASN1ObjectIdentifier.MATTER_RCAC_ID, "1").done().setSerialNumber("019123123123").buildAsSelfSigned();
        var signedCertificateForCSR = AnyUnmanagedCertificateBuilder.start().forCertificateSigningRequest(csrManager.getCsrHolder(), true).setValidFrom(new Date()).setSerialNumber("1").buildSignedBy(rootCA);
        System.out.println("SIGNED CERT\n" + signedCertificateForCSR);
    }


}
