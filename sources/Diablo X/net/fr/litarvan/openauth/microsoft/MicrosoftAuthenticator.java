/*
 * Copyright 2015-2021 Adrien 'Litarvan' Navratil
 *
 * This file is part of OpenAuth.

 * OpenAuth is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenAuth is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenAuth.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.fr.litarvan.openauth.microsoft;

/*
 * Ported from the amazing work of Alexis Bize on @xboxreplay/xboxlive-auth
 *
 * https://github.com/Alexis-Bize
 * https://github.com/XboxReplay/xboxlive-auth
 */

import fr.litarvan.openauth.microsoft.AuthTokens;
import fr.litarvan.openauth.microsoft.model.request.MinecraftLoginRequest;
import fr.litarvan.openauth.microsoft.model.request.XSTSAuthorizationProperties;
import fr.litarvan.openauth.microsoft.model.request.XboxLiveLoginProperties;
import fr.litarvan.openauth.microsoft.model.request.XboxLoginRequest;
import fr.litarvan.openauth.microsoft.model.response.MicrosoftRefreshResponse;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import fr.litarvan.openauth.microsoft.model.response.MinecraftStoreResponse;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Microsoft authenticator
 *
 * <p>
 *     This class can be used to authenticate a player using its Microsoft account.
 *     Use {@link #loginWithCredentials} to retrieve a player profile from his Microsoft credentials,
 * </p>
 *
 * @version 1.1.0
 * @author Litarvan
 */
public class MicrosoftAuthenticator
{
    public static final String MICROSOFT_AUTHORIZATION_ENDPOINT = "https://login.live.com/oauth20_authorize.srf";
    public static final String MICROSOFT_TOKEN_ENDPOINT = "https://login.live.com/oauth20_token.srf";
    public static final String MICROSOFT_REDIRECTION_ENDPOINT = "https://login.live.com/oauth20_desktop.srf";

    public static final String XBOX_LIVE_AUTH_HOST = "user.auth.xboxlive.com";
    public static final String XBOX_LIVE_CLIENT_ID = "000000004C12AE6F";
    public static final String XBOX_LIVE_SERVICE_SCOPE = "service::user.auth.xboxlive.com::MBI_SSL";

    public static final String XBOX_LIVE_AUTHORIZATION_ENDPOINT = "https://user.auth.xboxlive.com/user/authenticate";
    public static final String XSTS_AUTHORIZATION_ENDPOINT = "https://xsts.auth.xboxlive.com/xsts/authorize";
    public static final String MINECRAFT_AUTH_ENDPOINT = "https://api.minecraftservices.com/authentication/login_with_xbox";
    public static final String CHANGE_NAME_ENDPOINT = "https://api.minecraftservices.com/minecraft/profile/name/";

    public static final String XBOX_LIVE_AUTH_RELAY = "http://auth.xboxlive.com";
    public static final String MINECRAFT_AUTH_RELAY = "rp://api.minecraftservices.com/";

    public static final String MINECRAFT_STORE_ENDPOINT = "https://api.minecraftservices.com/entitlements/mcstore";
    public static final String MINECRAFT_PROFILE_ENDPOINT = "https://api.minecraftservices.com/minecraft/profile";

    public static final String MINECRAFT_STORE_IDENTIFIER = "game_minecraft";


    private final net.fr.litarvan.openauth.microsoft.HttpClient http;

    public MicrosoftAuthenticator()
    {
        this.http = new HttpClient();
    }


    /**
     * Logs in a player using its Microsoft account credentials, and retrieve its Minecraft profile
     *
     * @param email Player Microsoft account e-mail
     * @param password Player Microsoft account password
     *
     * @return The player Minecraft profile
     *
     * @throws MicrosoftAuthenticationException Thrown if one of the several HTTP requests failed at some point
     */
    public net.fr.litarvan.openauth.microsoft.MicrosoftAuthResult loginWithCredentials(String email, String password) throws MicrosoftAuthenticationException
    {
        CookieHandler currentHandler = CookieHandler.getDefault();
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        Map<String, String> params = new HashMap<>();
        params.put("login", email);
        params.put("loginfmt", email);
        params.put("passwd", password);

        HttpURLConnection result;

        try {
            PreAuthData authData = preAuthRequest();
            params.put("PPFT", authData.getPPFT());

            result = http.followRedirects(http.postForm(authData.getUrlPost(), params));
        } finally {
            CookieHandler.setDefault(currentHandler);
        }

        try {
            return loginWithTokens(extractTokens(result.getURL().toString()));
        } catch (MicrosoftAuthenticationException e) {
            if (match("identity/confirm", http.readResponse(result)) != null) {
                throw new MicrosoftAuthenticationException(
                        "User has enabled double-authentication or must allow sign-in on https://account.live.com/activity"
                );
            }

            throw e;
        }
    }

    /**
     * Logs in a player using a Microsoft account refresh token retrieved earlier.
     *
     * @param refreshToken Player Microsoft account refresh token
     *
     * @return The player Minecraft profile
     *
     * @throws MicrosoftAuthenticationException Thrown if one of the several HTTP requests failed at some point
     */
    public net.fr.litarvan.openauth.microsoft.MicrosoftAuthResult loginWithRefreshToken(String refreshToken) throws MicrosoftAuthenticationException
    {
        Map<String, String> params = getLoginParams();
        params.put("refresh_token", refreshToken);
        params.put("grant_type", "refresh_token");

        MicrosoftRefreshResponse response = http.postFormGetJson(
                MICROSOFT_TOKEN_ENDPOINT,
                params, MicrosoftRefreshResponse.class
        );

        return loginWithTokens(new AuthTokens(response.getAccessToken() , response.getRefreshToken()));
    }

    /**
     * Logs in a player using a Microsoft account tokens retrieved earlier.
     * <b>If the token was retrieved using Azure AAD/MSAL, it should be prefixed with d=</b>
     *
     * @param tokens Player Microsoft account tokens pair
     *
     * @return The player Minecraft profile
     *
     * @throws MicrosoftAuthenticationException Thrown if one of the several HTTP requests failed at some point
     */
    public net.fr.litarvan.openauth.microsoft.MicrosoftAuthResult loginWithTokens(AuthTokens tokens) throws MicrosoftAuthenticationException
    {
        net.fr.litarvan.openauth.microsoft.model.response.XboxLoginResponse xboxLiveResponse = xboxLiveLogin(tokens.getAccessToken());
        net.fr.litarvan.openauth.microsoft.model.response.XboxLoginResponse xstsResponse = xstsLogin(xboxLiveResponse.getToken());

        String userHash = xstsResponse.getDisplayClaims().getUsers()[0].getUserHash();
        net.fr.litarvan.openauth.microsoft.model.response.MinecraftLoginResponse minecraftResponse = minecraftLogin(userHash, xstsResponse.getToken());
        MinecraftStoreResponse storeResponse = http.getJson(
                MINECRAFT_STORE_ENDPOINT,
                minecraftResponse.getAccessToken(),
                MinecraftStoreResponse.class
        );

        if (Arrays.stream(storeResponse.getItems()).noneMatch(item -> item.getName().equals(MINECRAFT_STORE_IDENTIFIER))) {
            throw new MicrosoftAuthenticationException("Player didn't buy Minecraft Java Edition or did not migrate its account");
        }

        /*HttpURLConnection mcstoreconn = http.createConnection("https://api.minecraftservices.com/entitlements/mcstore");
        mcstoreconn.addRequestProperty("Authorization", "Bearer " + minecraftResponse.getAccessToken());
        mcstoreconn.addRequestProperty("Accept", "application/json");
        http.readResponse(mcstoreconn);

        HttpURLConnection certconn = http.createConnection("https://api.minecraftservices.com/player/certificate");
        certconn.addRequestProperty("Authorization", "Bearer " + minecraftResponse.getAccessToken());
        certconn.addRequestProperty("Accept", "application/json");
        http.readResponse(certconn);*/

        /*HttpURLConnection skinConn = http.createConnection("https://api.minecraftservices.com/minecraft/profile/skins");
        skinConn.addRequestProperty("Authorization", "Bearer " + minecraftResponse.getAccessToken());
        try {
            skinConn.setRequestMethod("POST");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("variant", "slim");
            jsonObject.put("url", "https://cdn.discordapp.com/attachments/1054558221832032347/1059190048555290704/3cb2c549a2a88d9f.png");

            skinConn.getOutputStream().write(jsonObject.toString().getBytes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        System.out.println(http.readResponse(skinConn));*/

        /*try {
            HttpURLConnection nameConn = http.createConnection("https://api.minecraftservices.com/minecraft/profile");
            nameConn.addRequestProperty("Authorization", "Bearer " + minecraftResponse.getAccessToken());
            nameConn.setDoOutput(true);
            nameConn.addRequestProperty("Content-Type", "application/json");
            nameConn.setRequestMethod("POST");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("profileName", StringUtil.getValidUsername());

            nameConn.getOutputStream().write(jsonObject.toString().getBytes());
            System.out.println(http.readResponse(nameConn));
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/

        /*try {
            HttpURLConnection skinConn = http.createConnection("https://api.minecraftservices.com/minecraft/profile/skins");
            skinConn.setDoOutput(true);
            skinConn.addRequestProperty("Authorization", "Bearer " + minecraftResponse.getAccessToken());
            skinConn.setRequestMethod("POST");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("variant", "slim");
            jsonObject.put("url", "https://cdn.discordapp.com/attachments/1054558221832032347/1059190048555290704/3cb2c549a2a88d9f.png");

            skinConn.getOutputStream().write(jsonObject.toString().getBytes());
            System.out.println(http.readResponse(skinConn));
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/

        MinecraftProfile profile = http.getJson(
                MINECRAFT_PROFILE_ENDPOINT,
                minecraftResponse.getAccessToken(),
                MinecraftProfile.class
        );

        return new net.fr.litarvan.openauth.microsoft.MicrosoftAuthResult(profile, minecraftResponse.getAccessToken(), tokens.getRefreshToken());
    }


    protected net.fr.litarvan.openauth.microsoft.PreAuthData preAuthRequest() throws MicrosoftAuthenticationException
    {
        Map<String, String> params = getLoginParams();
        params.put("display", "touch");
        params.put("locale", "en");

        String result = http.getText(MICROSOFT_AUTHORIZATION_ENDPOINT, params);

        String ppft = match("sFTTag:'.*value=\"([^\"]*)\"", result);
        String urlPost = match("urlPost: ?'(.+?(?='))", result);

        return new net.fr.litarvan.openauth.microsoft.PreAuthData(ppft, urlPost);
    }

    protected net.fr.litarvan.openauth.microsoft.model.response.XboxLoginResponse xboxLiveLogin(String accessToken) throws MicrosoftAuthenticationException
    {
        XboxLiveLoginProperties properties = new XboxLiveLoginProperties("RPS", XBOX_LIVE_AUTH_HOST, accessToken);
        XboxLoginRequest<XboxLiveLoginProperties> request = new XboxLoginRequest<>(
                properties, XBOX_LIVE_AUTH_RELAY, "JWT"
        );

        return http.postJson(XBOX_LIVE_AUTHORIZATION_ENDPOINT, request, net.fr.litarvan.openauth.microsoft.model.response.XboxLoginResponse.class);
    }

    protected net.fr.litarvan.openauth.microsoft.model.response.XboxLoginResponse xstsLogin(String xboxLiveToken) throws MicrosoftAuthenticationException
    {
        XSTSAuthorizationProperties properties = new XSTSAuthorizationProperties("RETAIL", new String[] { xboxLiveToken });
        XboxLoginRequest<XSTSAuthorizationProperties> request = new XboxLoginRequest<>(
                properties, MINECRAFT_AUTH_RELAY, "JWT"
        );

        return http.postJson(XSTS_AUTHORIZATION_ENDPOINT, request, net.fr.litarvan.openauth.microsoft.model.response.XboxLoginResponse.class);
    }

    protected net.fr.litarvan.openauth.microsoft.model.response.MinecraftLoginResponse minecraftLogin(String userHash, String xstsToken) throws MicrosoftAuthenticationException
    {
        MinecraftLoginRequest request = new MinecraftLoginRequest(String.format("XBL3.0 x=%s;%s", userHash, xstsToken));
        return http.postJson(MINECRAFT_AUTH_ENDPOINT, request, net.fr.litarvan.openauth.microsoft.model.response.MinecraftLoginResponse.class);
    }


    protected Map<String, String> getLoginParams()
    {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", XBOX_LIVE_CLIENT_ID);
        params.put("redirect_uri", MICROSOFT_REDIRECTION_ENDPOINT);
        params.put("scope", XBOX_LIVE_SERVICE_SCOPE);
        params.put("response_type", "token");

        return params;
    }

    protected AuthTokens extractTokens(String url) throws MicrosoftAuthenticationException
    {
        return new AuthTokens(extractValue(url, "access_token"), extractValue(url, "refresh_token"));
    }

    protected String extractValue(String url, String key) throws MicrosoftAuthenticationException {
        String matched = match(key + "=([^&]*)", url);
        if (matched == null) {
            throw new MicrosoftAuthenticationException("Invalid credentials or tokens " + key);
        }

        try {
            return URLDecoder.decode(matched, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new MicrosoftAuthenticationException(e);
        }
    }

    protected String match(String regex, String content)
    {
        Matcher matcher = Pattern.compile(regex).matcher(content);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }
}
