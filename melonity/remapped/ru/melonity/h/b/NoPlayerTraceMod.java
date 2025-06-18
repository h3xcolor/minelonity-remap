// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b;

import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import ru.melonity.Melonity;
import ru.melonity.f.EventListener;
import ru.melonity.f.b.Event;
import ru.melonity.o.Category;
import ru.melonity.s.c.EventBus;

@Environment(value=EnvType.CLIENT)
public class NoPlayerTraceMod extends ru.melonity.h.c.Module {
    private final EventListener<Event> eventListener = event -> {
        if (!isEnabled()) {
            return;
        }
        EventBus bus = Melonity.getEventBus();
        handleEvent();
    };
    
    public NoPlayerTraceMod() {
        super("NoPlayerTrace", Category.PLAYER);
    }

    private void handleEvent() {
        World world = Melonity.getWorld();
        List<Entity> entities = world.getEntities();
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (!(entity instanceof PlayerEntity)) {
                continue;
            }
            PlayerEntity player = (PlayerEntity) entity;
            if (shouldIgnorePlayer(player)) {
                continue;
            }
            setBoundingBox(player, 0.0f);
        }
    }

    private boolean shouldIgnorePlayer(PlayerEntity player) {
        if (player == Melonity.getPlayer()) {
            return true;
        }
        return player.isDead();
    }

    private void setBoundingBox(Entity entity, float shrink) {
        Box box = getAdjustedBoundingBox(entity, shrink);
        entity.setBoundingBox(box);
    }

    private Box getAdjustedBoundingBox(Entity entity, float shrink) {
        double minX = entity.getX() - shrink;
        double minY = entity.getY();
        double minZ = entity.getZ() - shrink;
        double maxX = entity.getX() + shrink;
        double maxY = entity.getY() + entity.getHeight();
        double maxZ = entity.getZ() + shrink;
        return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private boolean isEnabled() {
        return this.isEnabled();
    }
}