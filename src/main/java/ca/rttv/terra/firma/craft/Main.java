package ca.rttv.terra.firma.craft;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		TFCBlocks.init();
		TFCItems.init();
		TFCBlockEntityType.init();
		TFCScreenHandlerType.init();
	}
}
