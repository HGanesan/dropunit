package net.lisanza.dropunit.integrationtest.httpget;

import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import net.lisanza.dropunit.integrationtest.DropFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class GetWithExceptionTestIT extends BaseRequest {

    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    private DropUnitDto dropUnit;
    private int count;

    @Before
    public void setUp() throws Exception {
        dropUnit = DropFactory.createDropUnit("test-get-exception", "GET",
                Response.Status.BAD_REQUEST, MediaType.APPLICATION_XML, null);

        HttpResponse delivery = executeDropDelivery(dropUnit);
        assertEquals(200, delivery.getStatusLine().getStatusCode());
        String deliveryBody = EntityUtils.toString(delivery.getEntity(), "UTF-8");
        assertNotNull(deliveryBody);
        assertThat(deliveryBody, containsString("droppy registered"));

        count = executeRetrieveCount("get");
    }

    @Test
    public void shouldTestWithException() throws Exception {
        HttpResponse response = executeBasicHttpGet(ENDPOINT_HOST + dropUnit.getUrl());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

        assertThat(count + 1, is(executeRetrieveCount("get")));
    }

}
