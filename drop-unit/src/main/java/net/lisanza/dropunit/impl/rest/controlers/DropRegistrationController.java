package net.lisanza.dropunit.impl.rest.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.impl.rest.constants.RequestMappings;
import net.lisanza.dropunit.impl.rest.DropUnitCount;
import net.lisanza.dropunit.impl.rest.services.DropUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Path(RequestMappings.DROP_UNIT_SERVICE)
public class DropRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropRegistrationController.class);

    private final DropUnitCount dropUnitCount;
    private final DropUnitService dropUnitService;

    public DropRegistrationController(DropUnitService dropUnitService,
                                      DropUnitCount dropUnitCount) {
        this.dropUnitService = dropUnitService;
        this.dropUnitCount = dropUnitCount;
    }

    @POST
    @Path("/delivery/{dropId}")
    public String postDropUnit(@PathParam("dropId") String dropId,
                               @Valid DropUnitDto dto) {
        LOGGER.info("Called postDropUnit");
        return dropUnitService.register(dropId, dto);
    }

    @GET
    @Path("/getAllDrop")
    public String getAllDrop() {
        try {
            LOGGER.info("Called getAllDrop");
            return new ObjectMapper().writeValueAsString(dropUnitService.getAll());
        } catch (Exception e) {
            LOGGER.info("Error occurred while generating DropUnitDto list", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @GET
    @Path("/getDropCount")
    public String getDropCount() {
        try {
            LOGGER.info("Called getDropCount");
            return new ObjectMapper().writeValueAsString(dropUnitCount);
        } catch (Exception e) {
            LOGGER.info("Error occurred while generating DropUnitDto list", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @DELETE
    @Path("/clearAllIdentifierDrop/{identifier}")
    public String clearAllIdentifierDrop(@PathParam("identifier") String identifier) {
        try {
            LOGGER.info("Called clearAllIdentifierDrop" + identifier);
            return dropUnitService.clearDropWithIdentifier(identifier);
        } catch (Exception e) {
            LOGGER.info("Error occurred while deleting the identifier drop", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @DELETE
    @Path("/clearAllDrop")
    public String clearAllDrop() {
        try {
            LOGGER.info("Called clearAllDrop");
            return dropUnitService.dropAll();
        } catch (Exception e) {
            LOGGER.info("Error occurred while deleting all the drops", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }


}
