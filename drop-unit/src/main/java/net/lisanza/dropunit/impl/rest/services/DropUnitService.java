package net.lisanza.dropunit.impl.rest.services;

import net.lisanza.dropunit.impl.rest.DropUnitDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DropUnitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropUnitService.class);

    private ConcurrentHashMap<String, DropUnitDto> md5Registrations = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, DropUnitDto> patternRegistrations = new ConcurrentHashMap<>();

    public String dropAll() {
        md5Registrations.clear();
        return "droppy dropped";
    }

    public String clearDropWithIdentifier(String identifier) {

        md5Registrations.entrySet().removeIf(e -> (identifier).equalsIgnoreCase(e.getValue().getIdentifier()));
        patternRegistrations.entrySet().removeIf(e -> (identifier).equalsIgnoreCase(e.getValue().getIdentifier()));

        return "Droppies with " + identifier + " are dropped";
    }

    public List<DropUnitDto> getAll() {

        List<DropUnitDto> list = new ArrayList<>();
        for (DropUnitDto droppy : md5Registrations.values()) {
            list.add(droppy);
        }
        for (DropUnitDto droppy : patternRegistrations.values()) {
            list.add(droppy);
        }
        return list;
    }

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
            patternRegistrations.put(md5ForPattern(dropUnitDto), dropUnitDto);
        }

        return "droppy registered";
    }

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
            LOGGER.debug("Checking pattern registration");
            dto = getPatternRegistration(dropUnitDto);
        }
        return dto;
    }

    public DropUnitDto getPatternRegistration(DropUnitDto dropUnitDto) {
        return patternRegistrations.get(md5ForPattern(dropUnitDto));
    }

    public String md5(DropUnitDto dropUnitDto)
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

    public String md5ForPattern(DropUnitDto dropUnitDto)
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
