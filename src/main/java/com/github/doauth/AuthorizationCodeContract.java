/*
 * SPDX-License-Identifier: Apache-2.0
 */
package com.github.doauth;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "AuthorizationCodeContract",
    info = @Info(title = "AuthorizationCode contract",
                description = "My Smart Contract",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "authorization-blockchain@example.com",
                                                name = "authorization-blockchain",
                                                url = "http://authorization-blockchain.me")))
@Default
public class AuthorizationCodeContract implements ContractInterface {
    public AuthorizationCodeContract() {

    }
    @Transaction()
    public boolean authorizationCodeExists(Context ctx, String authorizationCodeId) {
        byte[] buffer = ctx.getStub().getState(authorizationCodeId);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createAuthorizationCode(Context ctx, String authorizationCodeId, String value) {
        boolean exists = authorizationCodeExists(ctx,authorizationCodeId);
        if (exists) {
            throw new RuntimeException("The asset "+authorizationCodeId+" already exists");
        }
        AuthorizationCode asset = new AuthorizationCode();
        asset.setValue(value);
        ctx.getStub().putState(authorizationCodeId, asset.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public AuthorizationCode readAuthorizationCode(Context ctx, String authorizationCodeId) {
        boolean exists = authorizationCodeExists(ctx,authorizationCodeId);
        if (!exists) {
            throw new RuntimeException("The asset "+authorizationCodeId+" does not exist");
        }

        AuthorizationCode newAsset = AuthorizationCode.fromJSONString(new String(ctx.getStub().getState(authorizationCodeId),UTF_8));
        return newAsset;
    }

    @Transaction()
    public void updateAuthorizationCode(Context ctx, String authorizationCodeId, String newValue) {
        boolean exists = authorizationCodeExists(ctx,authorizationCodeId);
        if (!exists) {
            throw new RuntimeException("The asset "+authorizationCodeId+" does not exist");
        }
        AuthorizationCode asset = new AuthorizationCode();
        asset.setValue(newValue);

        ctx.getStub().putState(authorizationCodeId, asset.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public void deleteAuthorizationCode(Context ctx, String authorizationCodeId) {
        boolean exists = authorizationCodeExists(ctx,authorizationCodeId);
        if (!exists) {
            throw new RuntimeException("The asset "+authorizationCodeId+" does not exist");
        }
        ctx.getStub().delState(authorizationCodeId);
    }

}
