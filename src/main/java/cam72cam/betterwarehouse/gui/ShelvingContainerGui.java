package cam72cam.betterwarehouse.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ShelvingContainerGui extends ContainerGuiBase {
	
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	private ShelvingContainer container;

    public ShelvingContainerGui(ShelvingContainer container) {
        super(container);
        this.container = container;
        this.xSize = paddingRight + container.horizSlots * slotSize + paddingLeft;
        this.ySize = 114 + container.numRows * slotSize;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
    	
    	int currY = j;
        
    	currY = drawTopBar(i, currY, container.horizSlots);
    	currY = drawSlotBlock(i, currY, container.horizSlots, container.numRows, container.numSlots);
    	currY = drawPlayerInventoryConnector(i, currY, width, container.horizSlots);
    	currY = drawPlayerInventory((width - playerXSize) / 2, currY);
    }
}