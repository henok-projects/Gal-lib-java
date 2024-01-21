package com.galsie.lib.certificates.asn1.object;

/**
 * A ASN1-Object-Identifier identifies a certain object type.
 * - The certificate has many fields which can hold objects or sets of objects
 * - Objects are key value pairs, where an object has an identifier, and a value
 *
 * For instance, the Subject field of a certificate is a distinguished name that holds a set of RDNs
 * - An RDN is a key value pair where the key is a ASN1-Object-Identifier, and the value is of some type depending on that object
 */
public interface AbstractASN1ObjectIdentifier {

    String getASN1OID();
}
