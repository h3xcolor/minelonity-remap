// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b.a;

import java.util.List;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.b.CommandHandler;
import ru.melonity.b.CommandInfo;
import ru.melonity.b.GlobalPrefixService;
import ru.melonity.b.CommandContext;
import ru.melonity.d.CommandArguments;
import ru.melonity.b.CommandArgument;

@Environment(value = EnvType.CLIENT)
public class GlobalPrefixCommand implements CommandHandler, CommandInfo {
    private final GlobalPrefixService prefixService;
    public static int COMMAND_ID = 911060364;

    @Override
    public void executeCommand(CommandContext context, CommandArguments arguments) {
        String prefix = context.getCommandArgument(1).getStringValue();
        this.prefixService.setGlobalPrefix(prefix);
    }

    @Override
    public String getCommandName() {
        return "prefix";
    }

    @Override
    public String getDescription() {
        return "Изменение префикса для всех команд";
    }

    @Override
    public List<CommandArgument> getArguments() {
        return List.of(new CommandArgument("prefix set <prefix>", "Установить префикс для всех команд"));
    }

    @Generated
    public GlobalPrefixCommand(GlobalPrefixService prefixService) {
        this.prefixService = prefixService;
    }
}