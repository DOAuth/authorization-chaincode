/*
 * SPDX-License-Identifier: Apache-2.0
 */
package com.github.doauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;

import java.net.URI;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "AuthorizationCodeContract",
    info = @Info(title = "AuthorizationCode contract",
                description = "smart contract to issue and manage authorization code",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "authorization-blockchain@example.com",
                                                name = "authorization-blockchain",
                                                url = "http://authorization-blockchain.me")))
@Default
public class AuthorizationCodeContract implements ContractInterface {

    @Transaction()
    public boolean authorizationCodeExists(Context ctx, String id) {
        byte[] buffer = ctx.getStub().getState(id);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public String createAuthorizationCode(
            Context ctx,
            String clientId,
            String paramRedirectUri,
            String paramScopes,
            String subject,
            String nonce,
            String codeChallenge,
            String codeChallengeMethod) {

        URI redirectUri = URI.create(paramRedirectUri);
        Set<String> scopes
                = new HashSet<>(this.toList(paramScopes));

        String code = RandomStringUtils.random(32, true, true);

        AuthorizationCode authorizationCode
                = new AuthorizationCode(clientId, redirectUri, scopes, code, subject, nonce, codeChallenge, codeChallengeMethod);

        try {
            ctx.getStub().putState(code, authorizationCode.toJSONString().getBytes(UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return code;
    }

    @Transaction()
    public AuthorizationCode readAuthorizationCode(Context ctx, String code) {

        if (!authorizationCodeExists(ctx, code)) {
            throw new RuntimeException("The authorization code " + code + " does not exist");
        }

        try {
            return AuthorizationCode.fromJSONString(new String(ctx.getStub().getState(code), UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Transaction()
    public String deleteAuthorizationCode(Context ctx, String code) {

        if (!authorizationCodeExists(ctx, code)) {
            throw new RuntimeException("The authorization code "+ code + " does not exist");
        }

        ctx.getStub().delState(code);
        return code;
    }

    private List<String> toList(String s) {
        List<String> list = Arrays.asList(s.substring(1, s.length() - 1).split(", "));
        return list.get(0).length() == 0 ? new ArrayList<>() : list;
    }
}
