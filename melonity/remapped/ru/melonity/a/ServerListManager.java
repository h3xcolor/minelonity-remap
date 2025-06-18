// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public interface ServerListManager {
    public static int DEFAULT_SERVER_LIST_ID = 2103775096;

    public List<ServerInfo> getServers();
    public void addServer(ServerInfo server);
    public void registerServerEntry(NetworkState state, ServerListEntry entry);
    public Optional<ServerListEntry> getServerListEntry(NetworkState state);
    public ServerInfo createServerEntry(NetworkState state, String name, String icon);
    public void saveList();
    public void loadList();
}