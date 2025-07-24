package oreon.src;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Key binding definitions for Oreon mod
 */
public class KeyBinds {
    
    // Main GUI key
    public static final KeyBinding OPEN_OREON_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.oreon.main"
    ));

    // Old GUI key (compatibility)
    public static final KeyBinding OPEN_OLD_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.open_old_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "category.oreon.main"
    ));

    // Settings GUI key
    public static final KeyBinding OPEN_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.open_settings",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            "category.oreon.main"
    ));

    // Profile GUI key
    public static final KeyBinding OPEN_PROFILE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.open_profile",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "category.oreon.main"
    ));

    // Check/Review functions
    public static final KeyBinding OPEN_CHECK_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.open_check",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.check"
    ));

    public static final KeyBinding CLEAN_VERDICT = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.clean_verdict",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.check"
    ));

    // Report functions
    public static final KeyBinding OPEN_REPORT_EDITOR = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.report_editor",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.reports"
    ));

    public static final KeyBinding OPEN_BANSPEC_MANAGER = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.banspec_manager",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.reports"
    ));

    public static final KeyBinding OPEN_TEMPLATE_MANAGER = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.template_manager",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.reports"
    ));

    // Target menu functions
    public static final KeyBinding OPEN_TARGET_MENU = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.target_menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.targeting"
    ));

    public static final KeyBinding OPEN_TARGET_SELECT = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.target_select",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.targeting"
    ));

    // Function toggles
    public static final KeyBinding TOGGLE_FULLBRIGHT = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.toggle_fullbright",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.functions"
    ));

    public static final KeyBinding TOGGLE_HIDE_PLAYERS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oreon.toggle_hide_players",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.oreon.functions"
    ));

    /**
     * Initialize key bindings - called from main mod class
     */
    public static void init() {
        // Key bindings are automatically registered via KeyBindingHelper.registerKeyBinding
        OreonClientMod.LOGGER.info("Registered {} key bindings", 14);
    }
}