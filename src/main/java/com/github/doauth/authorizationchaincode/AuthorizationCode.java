/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.github.doauth.authorizationchaincode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;

@DataType()
@Getter
@ToString
@EqualsAndHashCode
public class AuthorizationCode {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Property()
    @JsonProperty("client_id")
    private String clientId;

    @Property()
    @JsonProperty("redirect_uri")
    private URI redirectUri;

    @Property()
    @JsonProperty("scopes")
    private Set<String> scopes;

    @Property()
    @JsonProperty("code")
    private String code;

    @Property()
    @JsonProperty("subject")
    private String subject;

    @Property()
    @JsonProperty("nonce")
    private String nonce;

    @Property()
    @JsonProperty("code_challenge")
    private String codeChallenge;

    @Property()
    @JsonProperty("code_challenge_method")
    private String codeChallengeMethod;

    @Property()
    @JsonProperty("expiry")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime expiry;

    public AuthorizationCode() {}

    public AuthorizationCode(
            String clientId,
            URI redirectUri,
            Set<String> scopes,
            String code,
            String subject,
            String nonce,
            String codeChallenge,
            String codeChallengeMethod) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
        this.code = code;
        this.subject = subject;
        this.nonce = nonce;
        this.codeChallenge = codeChallenge;
        this.codeChallengeMethod = codeChallengeMethod;
        this.expiry = LocalDateTime.now().plusMinutes(2);
    }

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.getExpiry());
    }

    public String toJSONString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public static AuthorizationCode fromJSONString(String json) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.readValue(json, AuthorizationCode.class);
    }
}
