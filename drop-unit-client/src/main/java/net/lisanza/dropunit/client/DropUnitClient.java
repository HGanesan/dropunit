package net.lisanza.dropunit.client;

import lombok.extern.slf4j.Slf4j;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.request.RequestWrapper;
import net.lisanza.dropunit.utilities.DropUnitContants;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DropUnit Client Class
 */
@Slf4j
public class DropUnitClient {

    private static final AtomicInteger deliveryCount = new AtomicInteger();

    private RequestWrapper requestWrapper;

    private String postPath;

    private String deleteIdentifierDropPath;

    private DropUnitClient() {
        this.requestWrapper = new RequestWrapper();
    }

    /**
     * LazyHolder for making the Singleton pattern Thread safe.
     */
    private static class LazyHolder {
        static final DropUnitClient INSTANCE = new DropUnitClient();
    }

    /**
     * @return the instance of this class
     */
    public static DropUnitClient getInstance() {
        return DropUnitClient.LazyHolder.INSTANCE;
    }

    /**
     * Method to process the incoming Post request with Dto list
     * @param dtoList - Incoming request list
     * @param host-   DropuUnit host
     */
    public void invokeDropUnitWithList(List<DropUnitDto> dtoList, String host) {
        List<String> response = new ArrayList<>();
        List<DropUnitDto> dropUnitDto = requestWrapper.refineAndValidateRequestList(dtoList);
        for (DropUnitDto request : dropUnitDto) {
            response.add(postDrop(request, host));
        }
        response.forEach(log::info);

    }

    /**
     * Method to process the incoming Post request with Dto list
     * @param dto-  Incoming request
     * @param host- DropuUnit host
     */
    public void invokeDropUnit(DropUnitDto dto, String host) {
        requestWrapper.refineAndValidateRequest(dto);
        String response = postDrop(dto, host);
        log.info(response);

    }

    /**
     * Dropunit client builder
     * @param host -DropUnit host
     * @param path - request path
     */
    private Invocation.Builder dropUnitClientBuilder(String host, String path) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(host);
        WebTarget pathWebTarget
                = webTarget.path(path);
        return pathWebTarget.request(MediaType.APPLICATION_JSON);
    }

    /**
     * Method to invoke POST request
     * @param request -request entity
     * @param host    - DropUnit host
     * @return - response msg
     */
    private String postDrop(DropUnitDto request, String host) {
        formPostEndpoint();
        Invocation.Builder builder = dropUnitClientBuilder(host, postPath);
        Response response
                = builder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
        String responseMsg = "Register Drop:Success";
        int responseCode = response.getStatus();
        if (responseCode != 200) {
            responseMsg = String.format(
                    "Failed:Registering drop to DropUnit is not successful; the received HTTP response-code is '%d' for entity '%s'!", responseCode, request);
        }
        return responseMsg;
    }

    /**
     * Method to call ClearAllDrop request
     * @param host - Dropunit host
     */
    public void deleteAllDrop(String host) {
        Invocation.Builder builder = dropUnitClientBuilder(host, DropUnitContants.DELETE_URL);
        Response response
                = builder.delete();
        int responseCode = response.getStatus();
        if (responseCode != 200) log.error("Error occurred while deleting all the drop");
    }

    /**
     * Method to call ClearAlldentifierDrop
     * @param identifier -  to delete the specific drop
     * @param host       - DropUnit host
     */
    public void deleteIdentifierDrop(String identifier, String host) {
        formDeleteIdentifierDropEndpoint(identifier);
        Invocation.Builder builder = dropUnitClientBuilder(host, deleteIdentifierDropPath);
        Response response
                = builder.delete();
        int responseCode = response.getStatus();
        if (responseCode != 200) {
            log.error("Error occurred while deleting all the respective identifier : %s drop", identifier);
        }
    }

    /**
     * Method to form the POST Endpoint
     */
    private void formPostEndpoint() {
        postPath = DropUnitContants.POST_URL + deliveryCount.incrementAndGet();
    }

    /**
     * Method to form the DELETE with Identifier endpoint
     * @param identifier - to identify the specific drop
     */
    private void formDeleteIdentifierDropEndpoint(String identifier) {
        deleteIdentifierDropPath = DropUnitContants.DELETE_IDENTIFIER_DROP_URL + identifier;
    }
}
