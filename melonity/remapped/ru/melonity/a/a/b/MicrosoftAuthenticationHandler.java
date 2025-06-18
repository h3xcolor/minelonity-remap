// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a.a.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_320;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JOptionPane;
import ru.melonity.a.IIllIIIllIll极长的包名;
import ru.melonity.a.IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll;
import ru.melonity.a.a.b.IlIIIlIIllIlllIlIIIlIIlllIIlIllIIlIIllIIIlllIIlllIIIlIIIlIIllIlllIIllIllIIIlllIlIllIIIlllIIlllIl极长的包名;

@Environment(value=EnvType.CLIENT)
public class MicrosoftAuthenticationHandler implements ru.melonity.a.MicrosoftAuthenticator {
    private static final String CLIENT_ID = "907a248d-3eb5-4d01-99d2-ff72极长的字符串";
    private static final String MICROSOFT_TOKEN_URL = "https:
    private static final String XBOX_LIVE_AUTH_URL = "https:
    private static final String XSTS_AUTH_URL = "https:
    private static final String MINECRAFT_PROFILE_URL = "https:
    private static final String MINECRAFT_LOGIN_WITH_XBOX_URL = "https:
    private static final String REDIRECT_URI = "http:
    private static final String AUTH_URL = "https:
    private static final String SUCCESS_HTML_RESPONSE = "<!DOCTYPE HTML><html><body><style>html { background-color: rgb(20, 20, 20); }  h2 { text-align: center; color: white; font-family: Arial; } </style><h2>\u0412\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u0437\u0430\u043a\u0440\u044b\u0442\u044c \u0441\u0432\u043e\u0439 \u0431\u0440\u0430\u0443\u0437\u0435\u0440</h2><h2 style=\"font-size: 14px; color: rgb(211, 249, 66);\">Melonity Minecraft</h2></body></html>";
    private CloseableHttpClient httpClient;
    private HttpServer httpServer;

    private TokenResponse exchangeCodeForToken(String code) throws Exception {
        HttpPost httpPost = new HttpPost(MICROSOFT_TOKEN_URL);
        List<BasicNameValuePair> params = Arrays.asList(
            new BasicNameValuePair("client_id", CLIENT_ID),
            new BasicNameValuePair("scope", "xboxlive.signin"),
            new BasicNameValuePair("code", code),
            new BasicNameValuePair("grant_type", "authorization_code"),
            new BasicNameValuePair("redirect_uri", REDIRECT_URI)
        );
        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8.name()));
        httpPost.setHeader("Accept", "application/x-www-form-urlencoded");
        http极长的行尾
        CloseableHttpResponse response = httpClient.execute(httpPost);
        JSONObject json = new JSONObject(readInputStream(response.getEntity().getContent()));
        TokenResponse tokenResponse = new TokenResponse(json.getString("access_token"), json.getString("refresh_token"));
        response.close();
        return tokenResponse;
    }

    private String getAuthCode() throws Exception {
        AtomicReference<String> codeRef = new AtomicReference<>(null);
        if (httpServer != null) {
            httpServer.stop(0);
        }
        Thread.sleep(500);
        InetSocketAddress address = new InetSocketAddress(13378);
        httpServer = HttpServer.create(address, 0);
        httpServer.createContext("/relogin", httpExchange -> {
            String query = httpExchange.getRequestURI().getQuery();
            if (query != null && query.startsWith("code=")) {
                codeRef.set(query.substring("code=".length()));
            }
            httpExchange.sendResponseHeaders(200, SUCCESS_HTML_RESPONSE.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(SUCCESS_HTML_RESPONSE.getBytes(StandardCharsets.UTF_8));
            os.close();
        });
        httpServer.setExecutor(null);
        httpServer.start();
        while (codeRef.get() == null) {
            Thread.sleep(100);
        }
        return codeRef.get();
    }

    private XboxLiveAuthResponse authenticateWithXboxLive(String accessToken) throws Exception {
        HttpPost httpPost = new HttpPost(XBOX_LIVE_AUTH_URL);
        JSONObject properties = new JSONObject();
        properties.put("AuthMethod", "RPS");
        properties.put("SiteName", "user.auth.xboxlive.com");
        properties.put("RpsTicket", "d=" + accessToken);
        JSONObject requestBody = new JSONObject();
        requestBody.put("Properties", properties);
        requestBody.put("RelyingParty", "http:
        requestBody.put("TokenType", "JWT");
        httpPost.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        JSONObject json = new JSONObject(readInputStream(response.getEntity().getContent()));
        response.close();
        JSONArray xuiArray = json.getJSONObject("DisplayClaims").getJSONArray("xui");
        return new XboxLiveAuthResponse(json.getString("Token"), xuiArray.getJSONObject(0).getString("uhs"));
    }

    private XSTSAuthResponse authorizeWithXSTS(XboxLiveAuthResponse xboxLiveAuth) throws Exception {
        HttpPost httpPost = new HttpPost(XSTS_AUTH_URL);
        JSONArray userTokens = new JSONArray();
        userTokens.put(xboxLiveAuth.token);
        JSONObject properties = new JSONObject();
        properties.put("SandboxId", "RETAIL");
        properties.put("UserTokens", userTokens);
        JSONObject requestBody = new JSONObject();
        requestBody.put("Properties", properties);
        requestBody.put("RelyingParty", "rp:
        requestBody.put("TokenType", "JWT");
        httpPost.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        JSONObject json = new JSONObject(readInputStream(response.getEntity().getContent()));
        response.close();
        return new XSTSAuthResponse(json.getString("Token"));
    }

    private MinecraftAuthResponse loginWithXbox(XSTSAuthResponse xstsAuth, XboxLiveAuthResponse xboxLiveAuth) throws Exception {
        HttpPost httpPost = new HttpPost(MINECRAFT_LOGIN_WITH_XBOX_URL);
        JSONObject requestBody = new JSONObject();
        requestBody.put("identityToken", "XBL3.0 x=" + xboxLiveAuth.userHash + ";" + xstsAuth.token);
        httpPost.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        JSONObject json = new JSONObject(readInputStream(response.getEntity().getContent()));
        response.close();
        return new MinecraftAuthResponse(json.getString("access_token"));
    }

    private MinecraftProfile getMinecraftProfile(MinecraftAuthResponse minecraftAuth) throws Exception {
        HttpGet httpGet = new HttpGet(MINECRAFT_PROFILE_URL);
        httpGet.setHeader("Authorization", "Bearer " + minecraftAuth.accessToken);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String responseString = readInputStream(response.getEntity().getContent());
        JSONObject json = new JSONObject(responseString);
        response.close();
        if (!json.has("name")) {
            JOptionPane.showMessageDialog(null, "У вас нет лицензии");
        }
        return new MinecraftProfile(json.getString("name"), json.getString("id"));
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    @Override
    public IIllIllIIllIlIIlIlIIlllIllIIllIIllIlIIllIIIllIIIllIIllIIIlIllIIlIIlllIllIIlIllIllIIlIIlIlIIIlIllIIllIlllIllIIIllIIlIIlllIllIIlIIIlIIllIIllIIlIlIIlIllIIlllIlIIlllIIlIlIIlIIlIlllIIlIIIlIIllIllIlllIIlllIIIlIIllIIIlll login(String username, String password) {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(30000)
            .setSocketTimeout(30000)
            .setConnectionRequestTimeout(30000)
            .build();
        httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig)
            .build();
        try {
            URL authUrl = new URL(AUTH_URL);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(authUrl.toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", authUrl.toString()});
            }
            String authCode = getAuthCode();
            if (authCode == null) {
                System.err.println("Failed to get microsoft auth code");
                return null;
            }
            TokenResponse tokenResponse = exchangeCodeForToken(authCode);
            String microsoftAccessToken = tokenResponse.accessToken;
            XboxLiveAuthResponse xboxLiveAuth = authenticateWithXboxLive(microsoftAccessToken);
            XSTSAuthResponse xstsAuth = authorizeWithXSTS(xboxLiveAuth);
            MinecraftAuthResponse minecraftAuth = loginWithXbox(xstsAuth, xboxLiveAuth);
            MinecraftProfile profile = getMinecraftProfile(minecraftAuth);
            String profileName = profile.name();
            String profileId = profile.id();
            UUID uuid = UUID.fromString(profileId);
            String minecraftAccessToken = minecraftAuth.accessToken;
            return new ru.melonity.a.MinecraftAccount(profileName, uuid, minecraftAccessToken, class_320.class_321.field_1988, IIllIIIllIll极长的包名.AuthType.MICROSOFT);
        } catch (Throwable e) {
            System.err.println("Failed to get microsoft bearer token");
            e.printStackTrace();
            if (httpServer != null) {
                httpServer.stop(1);
            }
            return null;
        }
    }

    private record TokenResponse(String accessToken, String refreshToken) {}
    private record XboxLiveAuthResponse(String token, String userHash) {}
    private record XSTSAuthResponse(String token) {}
    private record MinecraftAuthResponse(String accessToken) {}
    private record MinecraftProfile(String name, String id) {}
}