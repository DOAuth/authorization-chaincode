/*
 * SPDX-License-Identifier: Apache-2.0
 */
package com.github.doauth.authorizationchaincode;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "AccessTokenContract",
    info = @Info(title = "AccessToken contract",
                description = "My Smart Contract",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "authorization-blockchain@example.com",
                                                name = "authorization-blockchain",
                                                url = "http://authorization-blockchain.me")))
@Default
public class AccessTokenContract implements ContractInterface {
    public AccessTokenContract() {

    }
    @Transaction()
    public boolean accessTokenExists(Context ctx, String id) {
        byte[] buffer = ctx.getStub().getState(id);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createAccessToken(Context ctx, String id, String value) {
        boolean exists = accessTokenExists(ctx,id);
        if (exists) {
            throw new RuntimeException("The asset "+id+" already exists");
        }
        AccessToken asset = new AccessToken();
        asset.setValue(value);

        try {
            ctx.getStub().putState(id, asset.toJSONString().getBytes(UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Transaction()
    public AccessToken readAccessToken(Context ctx, String id) {
        boolean exists = accessTokenExists(ctx,id);
        if (!exists) {
            throw new RuntimeException("The asset "+id+" does not exist");
        }

        AccessToken accessToken = null;
        try {
            accessToken = AccessToken.fromJSONString(new String(ctx.getStub().getState(id),UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    @Transaction()
    public void updateAccessToken(Context ctx, String id, String newValue) {
        boolean exists = accessTokenExists(ctx,id);
        if (!exists) {
            throw new RuntimeException("The asset "+id+" does not exist");
        }
        AccessToken asset = new AccessToken();
        asset.setValue(newValue);

        try {
            ctx.getStub().putState(id, asset.toJSONString().getBytes(UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transaction()
    public void deleteAccessToken(Context ctx, String id) {
        boolean exists = accessTokenExists(ctx,id);
        if (!exists) {
            throw new RuntimeException("The asset "+id+" does not exist");
        }
        ctx.getStub().delState(id);
    }

}
