package net.lisanza.dropunit.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.utilities.DropUnit;
import net.lisanza.dropunit.utilities.DropUnitContants;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DropUnitDtoMapper {

    private String requestPath;
    private String requestContentType;
    private String responsePath;
    private String responseContentType;
    private String url;
    private int delay;
    private int responseCode;
    private String identifier;
    private List<String> pattern;

    /**
     * Method to map the incoming parameters to POST DropUnitDto object
     *
     * @return DropUnitDto - mapped object
     */
    public DropUnitDto formPostDropUnitRequest() {

        return DropUnitDto.builder().identifier(identifier)
                .responseDelay(delay)
                .method(DropUnitContants.POST)
                .requestBody(requestPath)
                .responseBody(responsePath)
                .requestContentType(requestContentType)
                .responseContentType(responseContentType)
                .responseCode(responseCode)
                .url(url)
                .pattern(pattern)
                .identifier(identifier)
                .build();
    }

    /**
     * Method to map the incoming parameters to POST DropUnitDto object
     * @param - Enum
     * @return DropUnitDto - mapped object
     */
    public DropUnitDto formPostDropUnitRequest(DropUnit drop) {

        return DropUnitDto.builder()
                .responseDelay(drop.getDelay())
                .method(drop.getMethod())
                .requestBody(requestPath)
                .responseBody(responsePath)
                .requestContentType(drop.getReqContentType())
                .responseContentType(drop.getResContentType())
                .responseCode(drop.getCode())
                .url(drop.getUrl())
                .pattern(pattern)
                .identifier(drop.getIdentifier())
                .build();
    }

    /**
     * Method to map the incoming parameters to  GET DropUnitDto object
     *
     * @return DropUnitDto - mapped object
     */
    public DropUnitDto formGetDropUnitRequest(DropUnit drop) {

        return DropUnitDto.builder()
                .responseDelay(drop.getDelay())
                .method(drop.getMethod())
                .requestBody(requestPath)
                .responseBody(responsePath)
                .requestContentType(drop.getReqContentType())
                .responseContentType(drop.getResContentType())
                .responseCode(drop.getCode())
                .url(drop.getUrl())
                .pattern(pattern)
                .identifier(drop.getIdentifier())
                .build();
    }



}
