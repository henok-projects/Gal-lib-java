package com.galsie.lib.certificates.asn1.object;

import lombok.AllArgsConstructor;

/**
 * This enum holds a list of Galsie-Specific ASN1-Object identifiers
 * - Inherits from {@link AbstractASN1ObjectIdentifier}
 *
 * OUR private enterprise number is 60877 https://iana.org/assignments/enterprise-numbers
 */
@AllArgsConstructor
public enum GalsieASN1ObjectIdentifier implements AbstractASN1ObjectIdentifier {

    GALSIE_HOME_ID("1.3.6.1.4.1.60877.1.1"); // A Galsie home id


    private final String asn1OID;

    @Override
    public String getASN1OID() {
        return asn1OID;
    }
}
