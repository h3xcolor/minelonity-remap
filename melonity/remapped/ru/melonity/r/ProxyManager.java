// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.r;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import lombok.Generated;

@Environment(value = EnvType.CLIENT)
public class ProxyManager {
    private final List<ProxyEntry> allProxies = Lists.newCopyOnWriteArrayList();
    private final List<ProxyEntry> premiumProxies = Lists.newCopyOnWriteArrayList();
    private ProxyEntry currentProxy = null;

    private static List<String> fetchProxiesFromUrl(String url) throws Exception {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
        List<String> proxies = Arrays.stream(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).split("\n"))
                .map(line -> line.replace("socks5:
                .toList();
        inputStream.close();
        connection.disconnect();
        LinkedList<String> proxyList = new LinkedList<>(proxies);
        return Collections.synchronizedList(proxyList);
    }

    private static long testProxy(String host, int port) {
        InetSocketAddress address = new InetSocketAddress(host, port);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);
        try {
            long startTime = System.currentTimeMillis();
            URL testUrl = new URL("http:
            URLConnection connection = testUrl.openConnection(proxy);
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setConnectTimeout(2000);
            httpConnection.setRequestMethod("GET");
            httpConnection.setReadTimeout(2000);
            InputStream responseStream = httpConnection.getInputStream();
            String response = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);
            long endTime = System.currentTimeMillis();
            if (response.contains("Example Domain")) {
                return endTime - startTime;
            }
            return -1L;
        } catch (Exception e) {
            return -1L;
        }
    }

    public ProxyManager() {
        LinkedList<String> proxySources = Lists.newLinkedList();
        try {
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
            proxySources.addAll(fetchProxiesFromUrl("https:
        } catch (Throwable t) {
            t.printStackTrace();
        }
        for (int i = 0; i < 5; i++) {
            try {
                addProxy(proxySources.get(i), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new Thread(() -> {
            int count = 0;
            Iterator<String> iterator = proxySources.iterator();
            while (iterator.hasNext()) {
                if (count > 50) break;
                MinecraftClient client = MinecraftClient.getInstance();
                if (!(client.currentScreen instanceof TitleScreen)) break;
                String proxyAddress = iterator.next();
                try {
                    addProxy(proxyAddress, false);
                    count++;
                } catch (Exception ignored) {}
            }
        }).start();
    }

    public void addProxy(String proxyAddress, boolean isPremium) {
        ProxyType type;
        if (proxyAddress.startsWith("socks4:
            type = ProxyType.SOCKS4;
        } else if (proxyAddress.startsWith("socks5:
            type = ProxyType.SOCKS5;
        } else if (proxyAddress.startsWith("http")) {
            throw new RuntimeException("HTTP proxies are not allowed");
        } else {
            proxyAddress = "socks5:
            type = ProxyType.SOCKS5;
        }
        int prefixLength = "socks5:
        String cleanAddress = proxyAddress.substring(prefixLength);
        String[] parts = cleanAddress.split(":");
        String host = parts[0];
        int port;
        try {
            port = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            throw new RuntimeException("Invalid port");
        }
        long ping = testProxy(host, port);
        if (ping == -1L) {
            throw new RuntimeException("Proxy not working");
        }
        ProxyEntry entry = new ProxyEntry(type, proxyAddress, host, port, ping);
        System.out.println("Proxy added");
        if (isPremium) {
            premiumProxies.add(entry);
        } else {
            allProxies.add(entry);
        }
    }

    @Generated
    public List<ProxyEntry> getAllProxies() {
        return allProxies;
    }

    @Generated
    public List<ProxyEntry> getPremiumProxies() {
        return premiumProxies;
    }

    @Generated
    public ProxyEntry getCurrentProxy() {
        return currentProxy;
    }

    @Generated
    public void setCurrentProxy(ProxyEntry proxy) {
        currentProxy = proxy;
    }

    public enum ProxyType {
        SOCKS4,
        SOCKS5
    }

    public static class ProxyEntry {
        private final ProxyType type;
        private final String fullAddress;
        private final String host;
        private final int port;
        private final long ping;

        public ProxyEntry(ProxyType type, String fullAddress, String host, int port, long ping) {
            this.type = type;
            this.fullAddress = fullAddress;
            this.host = host;
            this.port = port;
            this.ping = ping;
        }

        public ProxyType getType() {
            return type;
        }

        public String getFullAddress() {
            return fullAddress;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public long getPing() {
            return ping;
        }
    }
}