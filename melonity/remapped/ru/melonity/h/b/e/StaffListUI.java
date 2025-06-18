// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.h.b.e;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Component;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.math.MatrixStack;
import ru.melonity.Melonity;
import ru.melonity.animation.FrameWeightCalculator;
import ru.melonity.o.a.a.ThemeManager;
import ru.melonity.s.a.ThemeColors;
import ru.melonity.s.e.RenderHelper;

@Environment(EnvType.CLIENT)
public class StaffListUI extends RenderHelper {
    private final Pattern validUsernamePattern = Pattern.compile("^\\w{3,16}$");
    private final Pattern staffUsernamePattern = Pattern.compile(".*(mod|der|adm|help|wne|\u043c\u043e\u0434|\u0445\u0435\u043b\u043f|\u043f\u043e\u043c\u043e|\u0430\u0434\u043c|\u0432\u043b\u0430\u0434\u0435|\u043e\u0442\u0440\u0438|\u0442\u0430\u0444|taf|curat|\u043a\u0443\u0440\u0430\u0442\u043e|dev|\u0440\u0430\u0437|supp|\u0441\u0430\u043f\u043f|\u044e\u0442\u0443\u0431).*");
    private float targetHeight = 0.0f;
    private float currentHeight = 0.0f;
    private boolean isHeightAdjusting = false;
    private final FrameWeightCalculator frameWeightCalculator = FrameWeightCalculator.milliseconds(300L);
    private final Map<Component, String> staffEntries = Maps.newLinkedHashMap();

    public StaffListUI() {
        super(93.0f, 1.0f);
    }

    public void reset() {
        this.staffEntries.clear();
        this.targetHeight = 20 + this.staffEntries.size() * 18;
        this.isHeightAdjusting = false;
    }

    @Override
    public void render(float x, float y, double mouseX, double mouseY, ThemeManager themeManager, MatrixStack matrices) {
        ThemeManager currentTheme = Melonity.themeManager.getCurrentTheme();
        ThemeColors themeColors = currentTheme.getThemeColors();
        if (!this.isHeightAdjusting) {
            this.currentHeight = this.targetHeight;
            this.isHeightAdjusting = true;
        }
        float frameWeight = this.frameWeightCalculator.elapsedUnits();
        this.currentHeight += (this.targetHeight - this.currentHeight) * 12.0f * frameWeight;
        Color backgroundColor = themeColors.getBackgroundColor();
        renderBackground(x, y, this.getWidth(), this.currentHeight, backgroundColor, matrices);
        Color iconColor = themeColors.getIconColor();
        Identifier iconTexture = new Identifier("melonity", "images/ui/icons/stafflist.png");
        renderTexture(iconTexture, x + 4.0f, y + 5.0f, 11.0f, 11.0f, iconColor, matrices);
        Color textColor = themeColors.getTextColor();
        renderText(RenderHelper.TextType.NORMAL, "StaffList", x + 15.0f, y + 9.0f, textColor, matrices);
        boolean isEmpty = this.staffEntries.isEmpty();
        if (!isEmpty) {
            Color separatorColor = themeColors.getSeparatorColor();
            renderSeparator(x, y + 19.0f, this.getWidth(), 1.0f, separatorColor, matrices);
        }
        float entryY = y + 21.0f;
        RenderHelper.TextType textType = RenderHelper.TextType.NORMAL;
        Set<Map.Entry<Component, String>> entrySet = this.staffEntries.entrySet();
        Iterator<Map.Entry<Component, String>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Component, String> entry = iterator.next();
            AtomicReference<Color> nameColorRef = new AtomicReference<>(null);
            if (nameColorRef.get() == null) {
                nameColorRef.set(Color.WHITE);
            }
            Component component = entry.getKey();
            String username = component.getName();
            Pattern colorPattern = Pattern.compile("\\\u00a7.");
            Matcher matcher = colorPattern.matcher(username);
            String cleanUsername = matcher.replaceAll("").trim();
            username = cleanUsername;
            String prefix = entry.getValue();
            float prefixWidth = textType.getTextWidth(prefix);
            float usernameWidth = textType.getTextWidth(username) + 6.0f;
            ThemeManager.ThemeComponent themeComponent = themeColors.getComponent();
            Color roleBackground = themeComponent.getBackgroundColor();
            renderRoundedRect(x + 3.0f, entryY + 1.0f, this.getWidth() - 6.0f, 14.0f, 4.0f, roleBackground, matrices);
            renderText(textType, prefix, x + 5.0f, entryY + 7.0f, matrices);
            Color nameColor = nameColorRef.get();
            int red = nameColor.getRed();
            int green = nameColor.getGreen();
            int blue = nameColor.getBlue();
            Color nameBackground = new Color(red, green, blue, 150);
            renderRoundedRect(x + this.getWidth() - usernameWidth - 3.0f, entryY + 5.0f, usernameWidth, 10.0f, 4.0f, nameBackground, matrices);
            renderText(textType, username, x + this.getWidth() - usernameWidth - 3.0f + 1.0f, entryY + 9.0f, nameColor, matrices);
            List<Map.Entry<Component, String>> entryList = List.copyOf(entrySet);
            int index = entryList.indexOf(entry);
            int totalEntries = entrySet.size();
            if (index != totalEntries - 1) {
                Color entrySeparatorColor = Color.decode("#222222");
                renderSeparator(x, entryY + 17.0f, this.getWidth(), 1.0f, entrySeparatorColor, matrices);
            }
            entryY += 16.0f;
        }
    }

    @Override
    public float getHeight() {
        return 20 + this.staffEntries.size() * 18;
    }

    @Override
    public void mouseClicked(float x, float y, double mouseX, double mouseY, int button) {
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void mouseReleased() {
    }

    @Environment(EnvType.CLIENT)
    private record StaffEntry(String name, String prefix, Color color) {
    }
}