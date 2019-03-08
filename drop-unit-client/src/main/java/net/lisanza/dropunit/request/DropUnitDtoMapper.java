package net.lisanza.dropunit.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
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
     * Method to map the incoming parameters to POST-XML DropUnitDto object
     * @param - Enum
     * @return DropUnitDto - mapped object
     */
    public DropUnitDto formPostXmlDropUnitRequest() {

        return DropUnitDto.builder()
                .responseDelay(delay)
                .method(DropUnitContants.POST)
                .requestBody(requestPath)
                .responseBody(responsePath)
                .requestContentType(DropUnitContants.XML_TYPE)
                .responseContentType(DropUnitContants.XML_TYPE)
                .responseCode(DropUnitContants.SUCCESS_CODE)
                .url(url)
                .pattern(pattern)
                .identifier(DropUnitContants.SOFT)
                .build();
    }

    /**
     * Method to map the incoming parameters to  GET-XML DropUnitDto object
     *
     * @return DropUnitDto - mapped object
     */
    public DropUnitDto formGetXmlDropUnitRequest() {

        return DropUnitDto.builder().identifier(identifier)
                .responseDelay(delay)
                .method(DropUnitContants.GET)
                .requestBody(requestPath)
                .responseBody(responsePath)
                .requestContentType(DropUnitContants.XML_TYPE)
                .responseContentType(DropUnitContants.XML_TYPE)
                .responseCode(DropUnitContants.SUCCESS_CODE)
                .url(url)
                .pattern(pattern)
                .identifier(DropUnitContants.SOFT)
                .build();
    }



}
