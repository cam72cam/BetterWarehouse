package cam72cam.betterwarehouse.gui;

import cam72cam.betterwarehouse.tile.ShelvingTile;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ShelvingContainer extends ContainerBase {
	
	protected ShelvingTile shelving;
	protected int numRows;
	protected int numSlots;
	protected int horizSlots;

	public ShelvingContainer(IInventory playerInventory, ShelvingTile shelving) {
		this.shelving = shelving;
        this.horizSlots = shelving.getSize() * ShelvingTile.INV_WIDTH_MULT;
		this.numRows = ShelvingTile.INV_ROWS;
		this.numSlots = numRows * horizSlots;
		
		IItemHandler itemHandler = this.shelving.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		int width = 0;
		int currY = 0;
		currY = offsetTopBar(0, currY, horizSlots);
		currY = addSlotBlock(itemHandler, numSlots, 0, currY, horizSlots);
    	currY = offsetPlayerInventoryConnector(0, currY, width, horizSlots);
    	currY = addPlayerInventory(playerInventory, currY, horizSlots);
	}

	@Override
	public int numSlots() {
		return numSlots;
	}
}
