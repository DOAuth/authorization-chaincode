/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.github.doauth;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.Genson;

@DataType()
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AuthorizationCode {

    private final static Genson genson = new Genson();

    @Property()
    private String value;

    public AuthorizationCode(){
    }

    public String toJSONString() {
        return genson.serialize(this);
    }

    public static AuthorizationCode fromJSONString(String json) {
        AuthorizationCode asset = genson.deserialize(json, AuthorizationCode.class);
        return asset;
    }
}
