package com.galsie.lib.certificates.certificate.builder;

import com.galsie.lib.certificates.asn1.object.MatterASN1ObjectIdentifier;
import com.galsie.lib.certificates.dn.InternalDNBuilder;
import com.galsie.lib.certificates.exception.FabricIdNotSupportedException;
import com.galsie.lib.certificates.exception.MaxSupportedRDNCountExceededException;
import com.galsie.lib.certificates.extension.CertificateExtensionsBuilder;
import com.galsie.lib.utils.DateUtils;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import com.galsie.lib.utils.lang.NotNull;
import com.galsie.lib.utils.lang.Nullable;
import com.galsie.lib.utils.pair.Pair;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Date;

public class CertificateBuilderCommonImpl<T extends CertificateBuilder> implements CertificateBuilder {

    /**
     * The Subject Name field of an X.509 certificate holds a Distinguished name (DN.
     */
    protected final InternalDNBuilder<T> subjectDNBuilder = new InternalDNBuilder<T>((T) this);

    /**
     * The Extension field X.509 certificate holds a list of extensions
     */
    protected final CertificateExtensionsBuilder<T> extensionsBuilder = new CertificateExtensionsBuilder<T>((T) this);

    protected HashingAlgorithm signingHashingAlgorithm = HashingAlgorithm.SHA256;
    /**
     * Corresponds to the 'serial-num' tag of the certificate
     * - Can use serial numbers up to 20 octets in length
     */
    @NotNull
    protected byte[] serialNumber; // A 20 octet string

    /**
     * Corresponds to the 'not-before' tag of the certificate
     * - The certificate would not be valid before that time
     */
    @NotNull
    protected Date validFrom;

    /**
     * Corresponds to the 'not-after' tag of the certificate
     * - The certificate would not be valid after that time
     * - If null, this corresponds to the X.509/RFC 5280 defined special time value 99991231235959Z which implies a no defined expiry
     */
    @Nullable
    protected Date validTo;

    /**
     * The 'Subject' field of the certificate holds a Distinguished Name
     * - Through this method, you access the Subject Distinguished name Builder
     *
     * @return The Subject DN Builder
     */
    public InternalDNBuilder<T> subjectDN() {
        return this.subjectDNBuilder;
    }

    /**
     * The 'Extensions' field of the certificate holds a set of extensions
     * - Through this method, you access the Extensions builder
     *
     * @return The Subject DN Builder
     */
    public CertificateExtensionsBuilder<T> extensions() {
        return this.extensionsBuilder;
    }

    /**
     * Sets the Hashing algorithm for the signing of the certificate
     *
     * @param hashingAlgorithm The hashing algorithm
     */
    public T setSigningHashingAlgorithm(HashingAlgorithm hashingAlgorithm){
        this.signingHashingAlgorithm = hashingAlgorithm;
        return (T) this;
    }
    /**
     * Sets the 'Serial Number' of the Certificate
     * - Must be a maximum of 20 bytes
     *
     * @param serialNumber The serial number.
     *                     - If longer than 20 octets, it is trimmed down to 20 octets.
     *                     - If shorter, its extended by zeros until it reaches 20 octets.
     */
    public T setSerialNumber(byte[] serialNumber) {
        byte[] fixedSerialNum = new byte[20];
        for (int i = 0; i < fixedSerialNum.length; i++) {
            if (i < serialNumber.length) {
                fixedSerialNum[i] = serialNumber[i];
                continue;
            }
            fixedSerialNum[i] = 0b00;
        }
        this.serialNumber = fixedSerialNum;
        return (T) this;
    }

    /**
     * Sets the 'Serial Number' of the Certificate
     * - Must be an 20 byte string
     *
     * @param serialNumber The serial number.
     *                     - If longer than 20 octets, it is trimmed down to 20 octets.
     *                     - If shorter, its extended by zeros until it reaches 20 octets.
     */
    public T setSerialNumber(String serialNumber) {
        return (T) this.setSerialNumber(serialNumber.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Sets the 'Not Before' field of the certificate, this sets the date from which the certificate is valid from
     *
     * @param date The date from which the certificate is valid from
     */
    public T setValidFrom(@NotNull Date date) {
        this.validFrom = date;
        return (T) this;
    }

    /**
     * Sets the 'Not After' field of the certificate, this sets the date after which the certificate is no longer valid
     * - If null, this corresponds to the X.509/RFC 5280 defined special time value 99991231235959Z which implies a no defined expiry
     *
     * @param date The date from which the certificate is valid from
     */
    public T setValidTo(@Nullable Date date) {
        this.validTo = date;
        return (T) this;
    }

    protected Date getValidTo(){
        return this.validTo == null ? DateUtils.timezoneFormatted("99991231235959Z", "yyyyMMddHHmmssX"): this.validTo;
    }

    protected void auxAddExtensions(X509v3CertificateBuilder certBuilder, PublicKey issuerKey, PublicKey subjectKey) throws Exception{
        for (Extension extension : this.extensions().getExtensions()) {
            certBuilder.addExtension(extension);
        }

        for (Pair<ASN1ObjectIdentifier, Boolean> delayedExtension: this.extensions().getDelayedExtensions()){
            var extensionOID = delayedExtension.getFirst();
            var isCritical = delayedExtension.getSecond();

            if (extensionOID.equals(Extension.subjectKeyIdentifier)){
                var extUtils = new JcaX509ExtensionUtils();
                var subjectKeyIdentifier = extUtils.createSubjectKeyIdentifier(subjectKey);  // publicKey is the public key for the certificate
                extensionsBuilder.addExtension(extensionOID, isCritical, subjectKeyIdentifier);
            }else if (extensionOID.equals(Extension.authorityKeyIdentifier)){
                JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
                extUtils.createAuthorityKeyIdentifier(issuerKey);
            }else{
                throw new Exception("Not Supported");
            }
        }
    }


    /**
     * MATTER AUXILIARY -- Here cause its convenient
     */

    /**
     * From 6.5.6.3. Matter DN Encoding Rule
     *  - All implementations SHALL accept, parse, and handle Matter certificates with up to 5 RDNs in a  single DN.
     */
    public T applyMatterConstraints(){
        this.subjectDNBuilder.getDistinguishedNameBuilder().rdnMaxCount(5);
        return (T) this;
    }
    /**
     * The 'rcac-id' is encoded in an RDN as part of the Subject
     * - The identifier of the 'rcac-id' DN-Object is defined in {@link MatterASN1ObjectIdentifier}
     * - This method is for convenience, you can equivalently get the subject DN and do {@link InternalDNBuilder#addRDN(AbstractASN1ObjectIdentifier, String)}
     * <p>
     * Only an RCAC certificate may hold the rcac-id in its Subject DN
     * - The id is optional and is useful for identifying the CA and debugging purposes.
     * - The identifier must be 64-bits
     *
     * @param id A unique identifier (within Galsie) for the RCAC
     */
    public T setRCACIdRDN(Long id) throws MaxSupportedRDNCountExceededException {
        return (T) this.subjectDN().addRDN(MatterASN1ObjectIdentifier.MATTER_RCAC_ID, String.valueOf(id)).done();
    }

    /**
     * The 'fabric-id' is encoded in an RDN as part of the Subject
     * - The identifier of the 'fabric-id' DN-Object is defined in {@link MatterASN1ObjectIdentifier}
     * - This method is for convenience, you can equivalently get the subject DN and do  {@link InternalDNBuilder#addRDN(AbstractASN1ObjectIdentifier, String)}
     * <p>
     * Any Matter-Certificate may hold a fabric ID:
     * - The NOC must hold the fabric ID
     * - The ICAC may hold the fabric ID
     * - If it does, NOCs signed by the Intermediate Certificate Authority must match the fabric ID
     * - The RCAC may hold the fabric ID
     * - If it does, ICACs/NOCs signed by the Root Certificate Authority must match the fabric ID
     * <p>
     * Note:
     * - The fabricId is a 64-bit integer
     * - Fabric ID 0 is reserved by the protoocol
     *
     * @param fabricId The id of the fabric. CANT BE 0, considered unsigned
     */
    public T setFabricIdRDN(Long fabricId) throws MaxSupportedRDNCountExceededException, FabricIdNotSupportedException {
        if (fabricId == 0){
            throw new FabricIdNotSupportedException(fabricId);
        }
        return (T) this.subjectDN().addRDN(MatterASN1ObjectIdentifier.MATTER_FABRIC_ID, Long.toUnsignedString(fabricId)).done();
    }


    /**
     * The 'icac-id' is encoded in an RDN as part of the Subject
     * - The identifier of the 'icac-id' DN-Object is defined in {@link MatterASN1ObjectIdentifier}
     * - This method is for convenience, you can equivalently get the subject DN and do  {@link InternalDNBuilder#addRDN(AbstractASN1ObjectIdentifier, String)}
     * -  from 6.5.6.2. Matter Certificate Types: The value of matter-icac-id and matter-rcac-id DN attribute types MAY be any 64-bit identifier desired by the certificate’s issuer. It's used for debugging purposes or whatsonot.
     * <p>
     * Only an ICAC may hold the rcac-id in its Subject DN
     * - Of-course, NOCs would then hodl the rcac-id int heir issuer DN if it was issued by an ICA not an RCA
     * <p>
     * Note:
     * - ICAC stands for Intermediate certificate authority certificate
     * - The ICAC id is a 64-bit integer
     *
     * @param icacId the id of the interemdiate certificate authority certificate
     */
    public T setIcacIdRDN(Long icacId) throws MaxSupportedRDNCountExceededException {
        return (T) this.subjectDN().addRDN(MatterASN1ObjectIdentifier.MATTER_ICAC_ID, Long.toUnsignedString(icacId)).done();

    }
}
