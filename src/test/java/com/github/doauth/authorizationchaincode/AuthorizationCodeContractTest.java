/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package com.github.doauth.authorizationchaincode;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class AuthorizationCodeContractTest {

    @Nested
    class AuthorizationCodeExists {

        @Test
        public void noProperAuthorizationCode() {

            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] {});

            AuthorizationCodeContract contract = new AuthorizationCodeContract();
            boolean result = contract.authorizationCodeExists(ctx,"10001");

            assertFalse(result);
        }

        @Test
        public void authorizationCodeExists() {

            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] {42});

            AuthorizationCodeContract contract = new AuthorizationCodeContract();
            boolean result = contract.authorizationCodeExists(ctx,"10001");

            assertTrue(result);

        }

        @Test
        public void noKey() {
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10002")).thenReturn(null);

            AuthorizationCodeContract contract = new AuthorizationCodeContract();
            boolean result = contract.authorizationCodeExists(ctx,"10002");

            assertFalse(result);
        }

    }

    @Nested
    class AuthorizationCodeCreates {

        @Test
        public void newAuthorizationCodeCreate() {

            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            AuthorizationCodeContract contract = new AuthorizationCodeContract();

            String clientId = "clientId";
            String paramRedirectUri = "https://doauth.com";
            String paramScopes = Arrays.asList("profile", "friends").toString();
            String subject = "userId";
            String nonce = "1010";
            String codeChallenge = "1010";
            String codeChallengeMethod = "method";

            String code = contract.createAuthorizationCode(
                    ctx, clientId, paramRedirectUri, paramScopes, subject, nonce, codeChallenge, codeChallengeMethod);

            System.out.println(code);
        }

        @Test
        public void AuthorizationCodeRead() throws Exception {
            AuthorizationCodeContract contract = new AuthorizationCodeContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            String clientId = "client_id";
            URI redirectUri = URI.create("https://doauth.com");
            Set<String> scopes = new HashSet<>(2);
            scopes.add("profile");
            scopes.add("friends");
            String code = RandomStringUtils.random(32, true, true);
            String subject = "user_id";
            String nonce = "1010";
            String codeChallenge = "1010";
            String codeChallengeMethod = "method";
            AuthorizationCode authorizationCode
                    = new AuthorizationCode(clientId, redirectUri, scopes, code, subject, nonce, codeChallenge, codeChallengeMethod);

            String json;
            try {
                json = authorizationCode.toJSONString();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            when(stub.getState(code)).thenReturn(json.getBytes(UTF_8));
            System.out.println("Expected: " + json);

            AuthorizationCode returnedCode = contract.readAuthorizationCode(ctx, code);
            System.out.println("Actual: " + returnedCode.toJSONString());
            assertEquals(returnedCode.getCode(), authorizationCode.getCode());
        }
    }
}
