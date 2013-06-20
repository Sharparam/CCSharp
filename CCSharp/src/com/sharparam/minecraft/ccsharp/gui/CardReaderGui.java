/*
 * CardReaderGui.java
 *
 * Copyright © 2013 by Adam Hellberg <adam.hellberg@sharparam.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.sharparam.minecraft.ccsharp.gui;

import com.sharparam.minecraft.ccsharp.common.BaseContainer;
import com.sharparam.minecraft.ccsharp.entities.CardReaderEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * User: Sharparam
 * Date: 2013-06-19
 * Time: 20:00
 */
public class CardReaderGui extends GuiContainer {
    private static final String BACKGROUND_TEXTURE = "/mods/CCSharp/textures/gui/cardReader.png";

    private static final int WIDTH = 176;
    private static final int HEIGHT = 168;

    private static final int NAME_COLOR = 4210752;
    private static final int NAME_OFFSET_X = 8;
    private static final int NAME_OFFSET_Y = 6;
    private static final String NAME = "Card Reader";

    private static final int INV_COLOR = NAME_COLOR;
    private static final int INV_OFFSET_X = NAME_OFFSET_X;
    private static final int INV_OFFSET_Y = 74;

    public CardReaderGui(InventoryPlayer playerInventory, CardReaderEntity entity) {
        super(new BaseContainer(playerInventory, entity));

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(NAME, NAME_OFFSET_X, NAME_OFFSET_Y, NAME_COLOR);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), INV_OFFSET_X, INV_OFFSET_Y, INV_COLOR);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        int texture = mc.renderEngine.getTexture(BACKGROUND_TEXTURE);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
