package it.bisumto.wetoxidation;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WetOxidation implements ModInitializer {
	public static final String MOD_ID = "wet-oxidation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Wet Oxidation mod loaded.");
	}
}