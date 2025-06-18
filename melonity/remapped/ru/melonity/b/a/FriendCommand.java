// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.b.a;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import ru.melonity.Melonity;
import ru.melonity.b.Command;
import ru.melonity.b.CommandContext;
import ru.melonity.b.CommandHandler;
import ru.melonity.b.CommandSender;
import ru.melonity.b.FriendManager;

@Environment(EnvType.CLIENT)
public class FriendCommand implements CommandHandler, CommandHandler.Descriptor {

    @Override
    public List<Command> getCommands() {
        return List.of(
            new Command("friend clear", "Очистить список друзей"),
            new Command("friend add <name>", "Добавить друга по нику"),
            new Command("friend remove <name>", "Удалить друга по нику"),
            new Command("friend raycast", "Добавить друга на которого смотришь")
        );
    }

    @Override
    public void handleCommand(CommandContext context, CommandSender sender) {
        FriendManager friendManager = Melonity.getFriendManager();
        String subCommand = context.getArgument(0).getValue();
        switch (subCommand) {
            case "clear":
                List<String> friends = friendManager.getFriends();
                if (friends.isEmpty()) {
                    sender.sendMessage("§cУ вас и не было друзей. Держите банан сочуствия 🍌");
                } else {
                    friendManager.clearFriends();
                    sender.sendMessage("§aУ вас больше нету друзей");
                }
                break;
            case "add":
                String addName = context.getArgument(1).getValue();
                friendManager.addFriend(addName);
                sender.sendMessage("§aИгрок " + addName + " добавлен в друзья");
                break;
            case "remove":
                String removeName = context.getArgument(1).getValue();
                friendManager.removeFriend(removeName);
                sender.sendMessage("§aИгрок " + removeName + " удалён из друзей");
                break;
            case "raycast":
                MinecraftClient client = MinecraftClient.getInstance();
                HitResult hitResult = client.crosshairTarget;
                if (!(hitResult instanceof EntityHitResult)) {
                    sender.sendMessage("§cНаведитесь на игрока");
                    return;
                }
                EntityHitResult entityHit = (EntityHitResult) hitResult;
                Entity entity = entityHit.getEntity();
                if (!(entity instanceof PlayerEntity)) {
                    String entityName = entity.getType().getName().getString();
                    sender.sendMessage("§cСущность " + entityName + " это не игрок");
                    return;
                }
                PlayerEntity player = (PlayerEntity) entity;
                String playerName = player.getName().getString();
                friendManager.addFriend(playerName);
                sender.sendMessage("§aИгрок " + playerName + " добавлен в друзья");
                break;
            default:
                throw new IllegalArgumentException("Такой опции нет");
        }
    }

    @Override
    public String getCommandName() {
        return "friend";
    }

    @Override
    public String getDescription() {
        return "Взаимодействие с друзьями";
    }
}