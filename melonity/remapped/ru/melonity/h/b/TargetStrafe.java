// ремапили ребята из https://t.me/dno_rumine
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.timer.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.InputUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.melonity.Melonity;
import ru.melonity.fabric.client.model.ILocalPlayer;
import ru.melonity.h.c.Module;
import ru.melonity.o.Setting;
import ru.melonity.o.b.a.b.SettingCallback;
import ru.melonity.w.KeyboardHelper;

@Environment(EnvType.CLIENT)
public class TargetStrafe extends Module {
    private LivingEntity target;
    private float direction = 1.0f;
    private final StrafeHelper strafeHelper = new StrafeHelper();
    private final Setting<Integer> rangeSetting = new Setting<>("global.range", number -> String.format("%d", number), 1, 6, 3);
    private final Setting<Boolean> shouldMove = makeBooleanSetting("shouldMove", event -> {
        if (!rangeSetting.getBooleanValue()) {
            return;
        }
        if (this.target != null && !this.target.isDead()) {
            if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.isOnGround() && !MinecraftClient.getInstance().options.jumpKey.isPressed()) {
                MinecraftClient.getInstance().player.setSprinting(true);
            }
            strafeHelper.method();
        }
    });
    private final Setting<Boolean> shouldUpdate = makeBooleanSetting("shouldUpdate", event -> {
        if (!rangeSetting.getBooleanValue()) {
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.player.age < 10) {
            return;
        }
        Timer timer = mc.timer;
        long window = mc.getWindow().getHandle();
        boolean leftKey = InputUtil.isKeyPressed(window, 65);
        boolean rightKey = InputUtil.isKeyPressed(window, 68);
        Melonity mod = Melonity.getMod();
        Optional<TargetStrafeModule> strafeModule = mod.getModule(TargetStrafeModule.class);
        if (!strafeModule.isPresent()) {
            return;
        }
        Entity entity = strafeModule.get().getTarget();
        if (entity instanceof LivingEntity livingTarget) {
            this.target = livingTarget;
            if (livingTarget.isDead()) {
                return;
            }
            float entityYaw = mc.player.getYaw();
            float targetYaw = mc.player.getYaw(livingTarget);
            this.direction = determineMovementDirection(mc, entityYaw, targetYaw);
            setTargetMovementPosition(mc, livingTarget);
        }
    });
    public static int staticInt = 173444183;

    public TargetStrafe() {
        super("TargetStrafe", Module.Category.MOVEMENT, true);
        this.addSetting(rangeSetting);
        this.addSetting(shouldMove);
        this.addSetting(shouldUpdate);
    }

    private float determineMovementDirection(MinecraftClient mc, float playerYaw, float targetYaw) {
        PlayerEntity player = mc.player;
        if (player == null) {
            return direction;
        }
        if (player.isSprinting()) {
            direction = (player.forwardSpeed > 0) ? -direction : direction;
        }
        if (shouldUpdate.getBooleanValue()) {
            direction = 1.0f;
        }
        if (shouldMove.getBooleanValue()) {
            direction = -1.0f;
        }
        return direction;
    }

    private void setTargetMovementPosition(MinecraftClient mc, LivingEntity target) {
        PlayerEntity player = mc.player;
        if (player == null || player.age < 10 || !player.isAlive() || target == null || player.distanceTo(target) > rangeSetting.getValue()) {
            return;
        }
        double playerX = player.getX();
        double targetX = target.getX();
        double playerZ = player.getZ();
        double targetZ = target.getZ();
        double deltaX = targetX - playerX;
        double deltaZ = targetZ - playerZ;
        double angle = Math.atan2(deltaZ, deltaX) - Math.toRadians(90);
        float distance = Math.max(player.distanceTo(target), rangeSetting.getValue().floatValue());
        double strafeX = Math.cos(angle) * distance * direction;
        double strafeZ = Math.sin(angle) * distance * direction;
        strafeHelper.setPosition(target.getX() + strafeX, target.getY(), target.getZ() + strafeZ);
    }

    private static class StrafeHelper {
        private double x;
        private double y;
        private double z;
        private boolean flag1;
        private boolean flag2;
        private int counter1;
        private int counter2;

        public void setPosition(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void method() {
            
        }
    }

    private static class TargetStrafeModule {
        public Entity getTarget() {
            return null; 
        }
    }
}