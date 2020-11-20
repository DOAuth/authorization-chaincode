/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.github.doauth.authorizationchaincode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AccessToken {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Property()
    private String value;

    public AccessToken(){
    }

    public String toJSONString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public static AccessToken fromJSONString(String json) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.readValue(json, AccessToken.class);
    }
}
