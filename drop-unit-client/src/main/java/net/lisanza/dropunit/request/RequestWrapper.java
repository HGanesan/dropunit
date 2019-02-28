package net.lisanza.dropunit.request;

import lombok.SneakyThrows;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * DropUnit Request Wrapper Class
 */
public class RequestWrapper {

    /**
     * Method to refine and validate the incoming POST request list
     *
     * @param requestList -DropunitDto List
     * @return dtoList - processed DropUnitDto List
     */
    public List<DropUnitDto> refineAndValidateRequestList(List<DropUnitDto> requestList) {
        List<DropUnitDto> dtoList = new ArrayList<>();
        for (DropUnitDto dto : requestList) {
            processDataMapping(dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * Method to refine and validate the incoming POST request
     *
     * @param request - DropUnitDto
     * @return request - processed DropUnitDto
     */
    public DropUnitDto refineAndValidateRequest(DropUnitDto request) {
        return processDataMapping(request);
    }

    /**
     * Method to process and map the request/response path to the actual content
     *
     * @param request - DropUnitDto
     * @return request - processed & mapped dto
     */
    private DropUnitDto processDataMapping(DropUnitDto request) {

        if (request.getRequestBody() != null) {
            String requestBody = dataMapper(request.getRequestBody());
            request.setRequestBody(requestBody);
        }
        if (request.getResponseBody() != null) {
            String responseBody = dataMapper(request.getResponseBody());
            request.setResponseBody(responseBody);
        }

        return request;
    }

    /**
     * Method to read the data from the given path
     *
     * @param path - request/response file path
     * @return IOUtil-String
     */
    @SneakyThrows
    private String dataMapper(String path) {
        String data;
        try (InputStream inputStream = new FileInputStream(new File(path))) {
            data = IOUtils.toString(inputStream,"UTF-8");
        }
        return data;

    }
}
