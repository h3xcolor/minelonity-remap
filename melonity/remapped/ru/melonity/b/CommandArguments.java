// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b;

import java.util.List;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public class CommandArguments {
    private final List<CommandArgument> arguments;

    public CommandArgument getArgument(int index) {
        int size = this.arguments.size();
        if (index >= size) {
            Object[] formatArgs = new Object[2];
            formatArgs[0] = index + 1;
            formatArgs[1] = size;
            String message = "\u041d\u0435 \u0445\u0432\u0430\u0442\u0430\u0435\u0442 \u0430\u0440\u0433\u0443\u043c\u0435\u043d\u0442\u043e\u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u044b [%d/%d]".formatted(formatArgs);
            throw new IndexOutOfBoundsException(message);
        }
        return this.arguments.get(index);
    }

    @Generated
    public CommandArguments(List<CommandArgument> arguments) {
        this.arguments = arguments;
    }
}