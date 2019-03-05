package net.lisanza.dropunit.utilities;


public enum DropUnit {
    ILS_LOOKUP("/ils/lookup", 200, DropUnitContants.POST, DropUnitContants.XML_TYPE, DropUnitContants.XML_TYPE, DropUnitContants.SOFT, 0),
    ILS_RESERVE("/ils/requestitem", 200, DropUnitContants.POST, DropUnitContants.XML_TYPE, DropUnitContants.XML_TYPE, DropUnitContants.SOFT, 0),
    ILS_SHIP("/ils/itemshipped", 200, DropUnitContants.POST, DropUnitContants.XML_TYPE, DropUnitContants.XML_TYPE, DropUnitContants.SOFT, 0),
    ILS_CANCEL("/ils/itemrequestcancelled", 200, DropUnitContants.POST, DropUnitContants.XML_TYPE, DropUnitContants.XML_TYPE, DropUnitContants.SOFT, 0),
    ILS_CANCEL_REQUEST_ITEM("/ils/cancelrequestitem", 200, DropUnitContants.POST, DropUnitContants.XML_TYPE, DropUnitContants.XML_TYPE, DropUnitContants.SOFT, 0);

    private final String url;
    private final int code;
    private final String method;
    private final String reqContentType;
    private final String resContentType;
    private final String identifier;
    private final int delay;

    DropUnit(String url, int code, String method, String reqContentType, String resContentType, String identifier, int delay) {
        this.url = url;
        this.code = code;
        this.method = method;
        this.reqContentType = reqContentType;
        this.resContentType = resContentType;
        this.identifier = identifier;
        this.delay = delay;
    }

    public String getUrl() {
        return url;
    }

    public int getCode() {
        return code;
    }

    public String getMethod() {
        return method;
    }

    public String getReqContentType() {
        return reqContentType;
    }

    public String getResContentType() {
        return resContentType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getDelay() {
        return delay;
    }
}
