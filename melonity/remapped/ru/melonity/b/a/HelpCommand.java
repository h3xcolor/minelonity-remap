// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.b.Command;
import ru.melonity.b.CommandHelpProvider;
import ru.melonity.b.CommandRegistry;
import ru.melonity.b.CommandSource;
import ru.melonity.b.CommandUsage;
import ru.melonity.b.HelpFormatter;

import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class HelpCommand implements Command, CommandHelpProvider {
    private final CommandRegistry commandRegistry;
    private final HelpFormatter helpFormatter;

    @Override
    public void execute(Command context, CommandSource source) {
        String subCommand = context.getArgument(0);
        if (subCommand == null) {
            throw new IllegalArgumentException("Subcommand is missing");
        }
        String commandName = subCommand;
        int action = -1;
        int nameHash = commandName.hashCode();
        switch (nameHash) {
            case 3322014:
                if (commandName.equals("list")) {
                    action = 0;
                }
                break;
            case 3649734:
                if (commandName.equals("with")) {
                    action = 1;
                }
        }
        switch (action) {
            case 0:
                List<Command> commands = this.commandRegistry.getAllCommands();
                Iterator<Command> commandIterator = commands.iterator();
                while (commandIterator.hasNext()) {
                    Command cmd = commandIterator.next();
                    String formatted = String.format("%s%s - §7%s", 
                        this.helpFormatter.getCommandPrefix(), 
                        cmd.getName(), 
                        cmd.getDescription());
                    source.sendMessage(formatted);
                }
                break;
            case 1:
                String targetCommandName = context.getArgument(1);
                if (targetCommandName == null) {
                    throw new IllegalArgumentException("Command name is missing");
                }
                Command targetCommand = this.commandRegistry.getCommand(targetCommandName);
                if (targetCommand == null) {
                    throw new IllegalArgumentException("Command not found: " + targetCommandName);
                }
                source.sendMessage(targetCommand.getDescription());
                if (targetCommand instanceof CommandHelpProvider) {
                    CommandHelpProvider helpProvider = (CommandHelpProvider) targetCommand;
                    List<CommandUsage> usages = helpProvider.getUsageExamples();
                    source.sendMessage("§aПримеры использования:");
                    for (CommandUsage usage : usages) {
                        String usageFormatted = String.format("%s%s - §7%s", 
                            this.helpFormatter.getCommandPrefix(), 
                            usage.getUsage(), 
                            usage.getDescription());
                        source.sendMessage(usageFormatted);
                    }
                } else {
                    source.sendMessage("§cКоманда не содержит примеров использования");
                }
                break;
            default:
                throw new IllegalArgumentException("Такой опции нет");
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Помощь с командами";
    }

    @Override
    public List<CommandUsage> getUsageExamples() {
        return List.of(
            new CommandUsage("help with <Название команды>", "Примеры использования команды"),
            new CommandUsage("help list", "Список всех команд")
        );
    }

    public HelpCommand(CommandRegistry commandRegistry, HelpFormatter helpFormatter) {
        this.commandRegistry = commandRegistry;
        this.helpFormatter = helpFormatter;
    }
}