package cam72cam.betterwarehouse.proxy;

import cam72cam.betterwarehouse.BetterWarehouse;
import cam72cam.betterwarehouse.render.ShelvingBlockRender;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = BetterWarehouse.MODID, value=Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		event.getModelRegistry().putObject(new ModelResourceLocation(BetterWarehouse.SHELVING_BLOCK.getRegistryName(), ""), new ShelvingBlockRender());
	}
}
