package me.rubyboat.twodea;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ModInitializer {
	public static String ns = "2dmod";
	public static final Logger LOGGER = LogManager.getLogger(ns);
	public static KeyBinding changedistancekey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key." + ns + ".change_distance",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_K,
			"category." + ns + ".keys"
	));
	public static KeyBinding changedistancekey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key." + ns + ".firstperson",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_N,
			"category." + ns + ".keys"
	));
	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

	}
}
