package net.lisanza.dropunit.impl.rest.services;

import net.lisanza.dropunit.impl.rest.DropUnitDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.xml.sax.SAXException;

public class DropUnitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropUnitService.class);

    private ConcurrentHashMap<String, DropUnitDto> md5Registrations = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, List<DropUnitDto>> patternRegistrations = new ConcurrentHashMap<>();

    /**
     * Method to clear all the stored drops
     * @return
     */
    public String dropAll() {
        md5Registrations.clear();
        patternRegistrations.clear();
        return "droppy dropped";
    }

    /**
     * Method to delete the specfic identifier drop
     * @param identifier
     * @return
     */
    public String clearDropWithIdentifier(String identifier) {

        md5Registrations.entrySet().removeIf(e -> (identifier).equalsIgnoreCase(e.getValue().getIdentifier()));

        Iterator<Map.Entry<String,List<DropUnitDto> >> entryIt = patternRegistrations.entrySet().iterator();
        while (entryIt.hasNext()) {
            Map.Entry<String, List<DropUnitDto>> entry = entryIt.next();
            for(DropUnitDto dto:entry.getValue()) {
                if (dto.getIdentifier().equalsIgnoreCase(identifier)) {
                    // Remove the element
                    entryIt.remove();
                    break;
                }
            }
        }
        return "Droppies with " + identifier + " are dropped";
    }

    /**
     * Method to get all the stored drops
     * @return List of stored drops
     */
    public List<DropUnitDto> getAll() {

        List<DropUnitDto> list = new ArrayList<>();
        for (DropUnitDto droppy : md5Registrations.values()) {
            list.add(droppy);
        }
        for (List<DropUnitDto> droppy : patternRegistrations.values()) {
            list.addAll(droppy);
        }
        return list;
    }

    /**
     * Method to store/register the incoming dto in DropUnit
     * @param dropId
     * @param dropUnitDto
     * @return
     */
    public String register(String dropId, DropUnitDto dropUnitDto) {

        if (dropUnitDto == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }
        if (!dropUnitDto.getUrl().startsWith("/")) {
            dropUnitDto.setUrl("/" + dropUnitDto.getUrl());
        }
        LOGGER.debug("register {} - url: {} - method: {} - req-type: {} - req-body: {}  - resp-delay: {} - resp-code: {} - resp-type: {} - resp-body: {} - identifier: {}", dropId, dropUnitDto.getUrl()
                , dropUnitDto.getMethod(), dropUnitDto.getRequestContentType(), dropUnitDto.getRequestBody(), dropUnitDto.getResponseDelay(), dropUnitDto.getResponseCode(), dropUnitDto.getResponseContentType(), dropUnitDto.getResponseBody()
                , dropUnitDto.getIdentifier());

        if (dropUnitDto.getPattern() == null) {
            md5Registrations.put(md5(dropUnitDto), dropUnitDto);
        } else {
            String patternKey = md5ForPattern(dropUnitDto);
            List<DropUnitDto> patternRegisterDto = patternRegistrations.get(patternKey);
            if (patternRegisterDto != null) {
                patternRegisterDto.add(dropUnitDto);
            } else {
                patternRegisterDto = new ArrayList<>();
                patternRegisterDto.add(dropUnitDto);
                patternRegistrations.put(patternKey, patternRegisterDto);
            }
        }

        return "droppy registered";
    }

    /**
     * Method to find/search the incoming request in the stored dto's
     * @param dropUnitDto
     * @return dto
     */
    public DropUnitDto lookup(DropUnitDto dropUnitDto) {

        if (dropUnitDto == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }

        LOGGER.debug("lookUp - url: {} - method: {} - req-type: {} - req-body: {}", dropUnitDto.getUrl()
                , dropUnitDto.getMethod(), dropUnitDto.getRequestContentType(), dropUnitDto.getRequestBody());

        DropUnitDto dto = md5Registrations.get(md5(dropUnitDto));
        if (dto == null) {
            dto = getPatternRegistration(dropUnitDto);
        }
        return dto;
    }

    /**
     * Method to get the incoming request in the Map with pattern
     * @param dropUnitDto
     * @return
     */
    private DropUnitDto getPatternRegistration(DropUnitDto dropUnitDto) {

        List<DropUnitDto> storedDtos = patternRegistrations.get(md5ForPattern(dropUnitDto));

        if (storedDtos != null) {

            try {
                for (DropUnitDto dto : storedDtos) {
                    if (checkPatternRegistration(dropUnitDto, dto)) {
                        return dto;
                    }
                }
            } catch (SAXException | IOException e) {
                LOGGER.error("Error occurred while comparing the XML :{}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * Method to find /search the incoming request is stored pattern map
     * @param requestDto
     * @param dropUnitDto
     * @return
     * @throws org.xml.sax.SAXException
     * @throws IOException
     */
    private Boolean checkPatternRegistration(DropUnitDto requestDto, DropUnitDto dropUnitDto) throws
            org.xml.sax.SAXException, IOException {

        List<Difference> differenceList = compareXML( requestDto.getRequestBody(),dropUnitDto.getRequestBody());
        List<String> patternList = dropUnitDto.getPattern();
        if (differenceList.size() != patternList.size()) {
            return false;
        } else return comparePattern(differenceList, patternList);

    }

    /**
     * Method to compare the incoming request with the stored request using pattern sand differences
     * @param differenceList
     * @param patternList
     * @return
     */
    private Boolean comparePattern(List<Difference> differenceList, List<String> patternList) {
        for (Difference difference : differenceList) {
            String diffInXml = difference.getTestNodeDetail().getXpathLocation();
            for (String pattern : patternList) {
                if (!diffInXml.contains(pattern)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method to compare the two XML data
     * @param request
     * @param dropUnitRequest
     * @return
     * @throws IOException
     * @throws SAXException
     */
    private List<Difference> compareXML(String request, String dropUnitRequest) throws IOException, SAXException {

        //creating Diff instance to compare two XML files
        Diff xmlDiff = new Diff(request, dropUnitRequest);

        /* for getting detailed differences between two xml files */
        DetailedDiff detailXmlDiff = new DetailedDiff(xmlDiff);

        return detailXmlDiff.getAllDifferences();
    }

    /**
     * Method to create a unique MD5 key with url,method and request
     * @param dropUnitDto
     * @return
     * @throws InternalServerErrorException
     */
    private String md5(DropUnitDto dropUnitDto)
            throws InternalServerErrorException {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(dropUnitDto.getUrl().getBytes());
            md.update(dropUnitDto.getMethod().getBytes());
            if (dropUnitDto.getRequestBody() != null) {
                md.update(dropUnitDto.getRequestBody().replaceAll("\\s+", "").getBytes());
            }
            byte[] digest = md.digest();
            String md5 = DatatypeConverter.printHexBinary(digest).toUpperCase();
            LOGGER.info("md5 : {}", md5);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            String msg = "'drop unit' md5 failed!";
            LOGGER.error(msg);
            throw new InternalServerErrorException(msg);
        }
    }

    /**
     * Method to create a unique MD5 key with URL and method.
     * @param dropUnitDto
     * @return
     * @throws InternalServerErrorException
     */
    private String md5ForPattern(DropUnitDto dropUnitDto)
            throws InternalServerErrorException {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(dropUnitDto.getUrl().getBytes());
            md.update(dropUnitDto.getMethod().getBytes());
            byte[] digest = md.digest();
            String md5 = DatatypeConverter.printHexBinary(digest).toUpperCase();
            LOGGER.info("md5 : {}", md5);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            String msg = "'drop unit' md5Pattern failed!";
            LOGGER.error(msg);
            throw new InternalServerErrorException(msg);
        }
    }

}
