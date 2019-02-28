package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DropUnitDto {

    @JsonProperty("url")
    private String url;

    @JsonProperty("method")
    private String method;

    @JsonProperty("requestContentType")
    private String requestContentType;

    @JsonProperty("requestBody")
    private String requestBody;

    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("responseContentType")
    private String responseContentType;

    @JsonProperty("responseBody")
    private String responseBody;

    @JsonProperty("responseDelay")
    private int responseDelay;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("pattern")
    private List<String> pattern;


}