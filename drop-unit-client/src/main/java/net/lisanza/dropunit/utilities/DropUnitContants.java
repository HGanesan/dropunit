package net.lisanza.dropunit.utilities;

import javax.ws.rs.core.MediaType;

/**
 * DropUnit Contants Class
 */
public final class DropUnitContants {
    //Content-Type
    public static final String XML_TYPE = MediaType.APPLICATION_XML;
    public static final String JSON_TYPE = MediaType.APPLICATION_JSON;

    // Rest methods
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    // Clear -Identifiers
    public static final String HARD= "hard";
    public static final String SOFT = "soft";

    //POST path
    public static final String POST_URL = "/dropunit/delivery/";

    //DELETE path
    public static final String DELETE_URL = "/dropunit/clearAllDrop";

    //DELETE with identifier path
    public static final String DELETE_IDENTIFIER_DROP_URL = "/dropunit/clearAllIdentifierDrop/";

    public static final int SUCCESS_CODE=200;


}
