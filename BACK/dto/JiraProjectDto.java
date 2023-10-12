package com.itso.occupancy.occupancy.dto.model.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itso.occupancy.occupancy.model.Client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraProjectDto {
    private String expand;
    private String self;
    private String id;
    private String key;
    private String name;
    private AvatarUrlsDto avatarUrls;
    private String projectTypeKey;
    private boolean simplified;
    private String style;

    @JsonProperty("isPrivate")
    private boolean isPrivate;

    private Object properties; // As properties is an empty object in the JSON
}
