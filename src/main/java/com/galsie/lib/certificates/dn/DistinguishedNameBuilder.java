package com.galsie.lib.certificates.dn;

import com.galsie.lib.certificates.asn1.codable.ASN1CodableUtils;
import com.galsie.lib.certificates.asn1.object.AbstractASN1ObjectIdentifier;
import com.galsie.lib.certificates.asn1.object.MatterASN1ObjectIdentifier;
import com.galsie.lib.certificates.exception.MaxSupportedRDNCountExceededException;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;

/**
 * A Distinguished Name (DN) is a set of RDNs (Relative Distinguished Names)
 * - An RDN is a key value pair where the key is a DN-Object {@link MatterASN1ObjectIdentifier}
 */
public class DistinguishedNameBuilder {
    /**
     * A String builder builds the DN with the set of RDNs
     * - The builder is only used by {@link DistinguishedNameBuilder#doAddRDN(String, String)}, which ensures the X.509 v3 format is matched
     */
    private StringBuilder stringBuilder = new StringBuilder();
    private int rdnCount = 0;
    private int rdnMaxCount; // if -1, means unlimited

    /**
     * Some protocols limit the number of RDNs a Distinguished-Name can hold.
     * - For instance, the matter protocol sets that limit at 5
     * -- The limit is in NO WAY stored in the DN, its just used to ensure the developer doesn't mess up.
     * @param rdnMaxCount The maximum RDN count
     */
    public DistinguishedNameBuilder(int rdnMaxCount){
        this.rdnMaxCount = rdnMaxCount;
    }

    /**
     * Default constructor does NOT limit the RDN count
     */
    public DistinguishedNameBuilder(){
        this(-1);
    }

    public DistinguishedNameBuilder rdnMaxCount(int rdnMaxCount){
        this.rdnMaxCount = rdnMaxCount;
        return this;
    }


    /**
     * Auxiliary method to add the DN key value pair to the subject string builder
     * @param dnObjectIdentifier The DN object identifier (dot joined)
     * @param value The value, whatever its datatype is based on the requirements of the DN, encoded to a string
     * @return
     */
    private DistinguishedNameBuilder doAddRDN(String dnObjectIdentifier, String value) throws MaxSupportedRDNCountExceededException {
        if (rdnCount >= rdnMaxCount && rdnMaxCount >= 0){
            throw new MaxSupportedRDNCountExceededException();
        }
        if (rdnCount > 0){
            stringBuilder.append(",");
        }
        stringBuilder.append(dnObjectIdentifier).append("=").append(value);
        rdnCount += 1; // increment the rdnCount
        return this;
    }

    /**
     * Adds an RDN to the Distinguished Name
     * @param dnObjectIdentifier The DN-Object identifier
     * @param value The value, whatever its datatype is based on the requirements of the DN-Object, encoded to a string
     */
    public DistinguishedNameBuilder addRDN(String dnObjectIdentifier, String value) throws MaxSupportedRDNCountExceededException {
        return this.doAddRDN(dnObjectIdentifier, value);
    }

    /**
     * Adds a Distinguished Name to the subject
     * @param dnObject The distinguished name object
     * @param value The value, whatever its datatype is based on the requirements of the DN, encoded to a string
     */
    public DistinguishedNameBuilder addRDN(AbstractASN1ObjectIdentifier dnObject, String value) throws MaxSupportedRDNCountExceededException {
        this.doAddRDN(dnObject.getASN1OID(), value);
        return this;
    }


    public DistinguishedNameBuilder addContentsOfX500Name(X500Name x500Name) throws Exception{
        for (var rdn: x500Name.getRDNs()){
           this.addContentsOfRDN(rdn);
        }
        return this;
    }

    public DistinguishedNameBuilder addContentsOfRDN(RDN rdn) throws Exception{
        for (var typeAndValue: rdn.getTypesAndValues()){
            this.addRDN(typeAndValue.getType(), typeAndValue.getValue());
        }
        return this;
    }

    public DistinguishedNameBuilder addRDN(ASN1ObjectIdentifier asn1ObjectIdentifier, ASN1Encodable value) throws Exception{
        String strValue = ASN1CodableUtils.extractUtf8OrPrintableStringFrom(value);
        this.addRDN(asn1ObjectIdentifier.getId(), strValue);
        return this;
    }


    /**
     * FINALLY builds the string
     * - NOTE: We do not have to encode them in DER format because bouncy castle automatically handles this for us.
     * @return A built DN string composed of all the RDNs
     */
    public X500Name build(){
        if (stringBuilder.isEmpty()){
            return new X500Name(new RDN[]{});
        }
        return new X500Name(stringBuilder.toString());
    }

}
