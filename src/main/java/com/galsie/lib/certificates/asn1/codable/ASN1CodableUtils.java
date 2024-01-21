package com.galsie.lib.certificates.asn1.codable;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1PrintableString;
import org.bouncycastle.asn1.ASN1UTF8String;

import java.util.Optional;

public class ASN1CodableUtils {


    public static String extractUtf8OrPrintableStringFrom(ASN1Encodable value) throws Exception{
        if (value instanceof ASN1PrintableString asn1PrintableString){
            return asn1PrintableString.getString();
        }else if(value instanceof ASN1UTF8String utf8String){
            return utf8String.getString();
        }
        throw new Exception("ASN1 Type  " + value.getClass().getName() + " not supported as a value for rdn");
    }
    public static Optional<String> extractOptionalUtf8OrPrintableStringFrom(ASN1Encodable value){
        try {
            return Optional.of(extractUtf8OrPrintableStringFrom(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
