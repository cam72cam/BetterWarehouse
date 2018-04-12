package cam72cam.betterwarehouse.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class ContainerGuiBase extends GuiContainer {
    public static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

	public ContainerGuiBase(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

	public static final int slotSize = 18;
    public static final int topOffset = 17;
    public static final int bottomOffset = 7;
    public static final int textureHeight = 222;
    public static final int paddingRight = 7;
    public static final int paddingLeft = 7;
    public static final int stdUiHorizSlots = 9;
    public static final int playerXSize = paddingRight + stdUiHorizSlots * slotSize + paddingLeft;
	private static final int midBarOffset = 4;
	private static final int midBarHeight = 4;
    
    public int drawTopBar(int x, int y, int slots) {
    	this.drawTexturedModalRect(x, y, 0, 0, paddingLeft, topOffset);
        // Top Bar
        for (int k = 1; k <= slots; k++) {
        	this.drawTexturedModalRect(x + paddingLeft + (k-1) * slotSize, y, paddingLeft, 0, slotSize, topOffset);
        }
        // Top Right Corner
    	this.drawTexturedModalRect(x + paddingLeft + slots * slotSize, y, paddingLeft + stdUiHorizSlots * slotSize, 0, paddingRight, topOffset);
    	
    	return y + topOffset;
    }
    
    public void drawSlot(int x, int y) {
    	this.drawTexturedModalRect(x, y, paddingLeft, topOffset, slotSize, slotSize);
    }
    
    public int drawSlotRow(int x, int y, int slots, int numSlots) {
    	// Left Side
        this.drawTexturedModalRect(x, y, 0, topOffset, paddingLeft, slotSize);
        // Middle Slots
        for (int k = 1; k <= slots; k++) {
        	if (k <= numSlots) {
        		drawSlot(x + paddingLeft + (k-1) * slotSize, y);
        	} else {
        		Gui.drawRect(x + paddingLeft + (k-1) * slotSize, y, x + paddingLeft + (k-1) * slotSize + slotSize, y + slotSize, 0xFF444444);
        	}
        }
		GL11.glColor4f(1, 1, 1, 1);
        // Right Side
    	this.drawTexturedModalRect(x + paddingLeft + slots * slotSize, y, paddingLeft + stdUiHorizSlots * slotSize, topOffset, paddingRight, slotSize);
    	return y + slotSize;
    }

	public int drawSlotBlock(int x, int y, int slotX, int slotY, int numSlots) {
		for (int i = 0; i < slotY; i++) {
			y = drawSlotRow(x, y, slotX, numSlots);
			numSlots -= slotX;
		}
		return y;
	}

	public int drawBottomBar(int x, int y, int slots) { 
    	// Left Bottom
        this.drawTexturedModalRect(x, y, 0, textureHeight - bottomOffset, paddingLeft, bottomOffset);
        // Middle Bottom
        for (int k = 1; k <= slots; k++) {
        	this.drawTexturedModalRect(x + paddingLeft + (k-1) * slotSize, y, paddingLeft, textureHeight - bottomOffset, slotSize, bottomOffset);
        }
        // Right Bottom
    	this.drawTexturedModalRect(x + paddingLeft + slots * slotSize, y, paddingLeft + 9 * slotSize, textureHeight - bottomOffset, paddingRight, bottomOffset);
    	
    	return y + bottomOffset;
	}

	public int drawPlayerTopBar(int x, int y) {
        this.drawTexturedModalRect(x, y, 0, 0, playerXSize, bottomOffset);
		return y + bottomOffset;
	}

	public int drawPlayerMidBar(int x, int y) {
		this.drawTexturedModalRect(x, y, 0, midBarOffset, playerXSize, midBarHeight);
		return y + midBarHeight;
	}

	public int drawPlayerInventory(int x, int y) {        
        this.drawTexturedModalRect(x, y, 0, 126+4, playerXSize, 96);
		return y+96;
	}

	public int drawPlayerInventoryConnector(int x, int y, int aboveWidth, int horizSlots) {
    	if (horizSlots > 9) {
    		return drawBottomBar(x, y, horizSlots);
    	} else if (horizSlots < 9){
    		return drawPlayerTopBar((aboveWidth - playerXSize) / 2, y);
    	} else {
    		return drawPlayerMidBar((aboveWidth - playerXSize) / 2, y);
    	}
	}
}
