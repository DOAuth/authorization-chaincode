/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.github.doauth;

import com.owlike.genson.Genson;
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

    private final static Genson genson = new Genson();

    @Property()
    private String value;

    public AccessToken(){
    }

    public String toJSONString() {
        return genson.serialize(this);
    }

    public static AccessToken fromJSONString(String json) {
        AccessToken asset = genson.deserialize(json, AccessToken.class);
        return asset;
    }
}
