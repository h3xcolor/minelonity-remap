// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.melonity.b.Command;
import ru.melonity.b.CommandResponse;
import ru.melonity.b.CommandSender;
import ru.melonity.b.CommandOutput;
import ru.melonity.b.CommandArgument;
import ru.melonity.b.CommandContext;
import ru.melonity.b.AliasCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class CommandManager implements CommandDispatcher, CommandRegistry {
    private final Map<String, Command> commands;
    private final CommandSender commandSender;
    private final CommandOutput commandOutput;
    private final List<Command> commandList;

    public CommandManager(CommandSender commandSender, CommandOutput commandOutput) {
        List<Command> commandObjects = new ArrayList<>();
        commandObjects.add(new HelpCommand(this, commandSender));
        commandObjects.add(new AnotherCommand(commandSender));
        commandObjects.add(new ThirdCommand());
        commandObjects.add(new FourthCommand());

        this.commands = commandObjects.stream()
            .flatMap(CommandManager::getCommandEntries)
            .collect(Collectors.toMap(CommandEntry::getCommandName, CommandEntry::getCommand));

        this.commandSender = commandSender;
        this.commandOutput = commandOutput;
        this.commandList = commandObjects;
    }

    @Override
    public CommandResponse executeCommand(String commandString) {
        String prefix = this.commandSender.getCommandPrefix();
        if (commandString.startsWith(prefix)) {
            String withoutPrefix = commandString.substring(prefix.length());
            String[] parts = withoutPrefix.split(" ");
            String commandName = parts[0];
            Command command = this.getCommand(commandName);
            String[] argsArray = new String[parts.length - 1];
            System.arraycopy(parts, 1, argsArray, 0, argsArray.length);
            List<CommandArgument> arguments = Arrays.stream(argsArray)
                .map(CommandArgument::new)
                .toList();
            CommandContext context = new CommandContext(arguments);
            try {
                command.execute(context, this.commandOutput);
                return CommandResponse.SUCCESS;
            } catch (Exception e) {
                this.commandOutput.output("§cПроизошла ошибка во время выполнения команды");
                this.commandOutput.output("§aЭто не значит что команды не существует!");
                String helpListMessage = "§cНапишите %shelp list, чтобы увидеть список команд".formatted(prefix);
                this.commandOutput.output(helpListMessage);
                if (command instanceof HelpCommand) {
                    String helpExampleMessage = "§cНапишите '%shelp with %s' для примера использования команды".formatted(prefix, command.getCommandName());
                    this.commandOutput.output(helpExampleMessage);
                }
            }
        }
        return CommandResponse.FAILURE;
    }

    @Override
    public Command getCommand(String name) {
        try {
            return this.commands.get(name);
        } catch (Exception e) {
            throw new IllegalArgumentException("Команда %s не найдена".formatted(name));
        }
    }

    @Override
    public List<Command> getCommands() {
        return this.commandList;
    }

    private static Stream<CommandEntry> getCommandEntries(Command command) {
        String primaryName = command.getCommandName();
        CommandEntry primaryEntry = new CommandEntry(primaryName, command);
        Stream<CommandEntry> stream = Stream.of(primaryEntry);
        if (command instanceof AliasCommand) {
            AliasCommand aliasCommand = (AliasCommand) command;
            List<String> aliases = aliasCommand.getAliases();
            Stream<CommandEntry> aliasStream = aliases.stream()
                .map(alias -> new CommandEntry(alias, command));
            stream = Stream.concat(stream, aliasStream);
        }
        return stream;
    }

    private static class CommandEntry {
        private final String commandName;
        private final Command command;

        public CommandEntry(String commandName, Command command) {
            this.commandName = commandName;
            this.command = command;
        }

        public String getCommandName() {
            return commandName;
        }

        public Command getCommand() {
            return command;
        }
    }
}