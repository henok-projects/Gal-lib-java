package com.galsie.lib.certificates;

import java.io.IOException;

public interface PEMStructureHolder {

    String getBase64DEREncoded() throws IOException;
    String getPEMEncoded() throws IOException;
}
