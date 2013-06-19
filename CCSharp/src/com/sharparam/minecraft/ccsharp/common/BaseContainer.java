/*
 * BaseContainer.java
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

package com.sharparam.minecraft.ccsharp.common;

import com.sharparam.minecraft.ccsharp.entities.InventoryEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * User: Sharparam
 * Date: 2013-06-19
 * Time: 19:37
 */
public class BaseContainer extends Container {
    private static final int PLAYER_INVENTORY_COLS = 9;
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_HOTBAR_COLS = 9;

    private final InventoryEntity entity;

    public BaseContainer(InventoryPlayer playerInventory, InventoryEntity entity) {
        this.entity = entity;

        int cols = entity.getInventoryCols();
        int rows = entity.getInventoryRows();

        // Inventory of entity
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                addSlotToContainer(new Slot(entity, col + row * 3, 62 + col * 18, 17 + row * 18));

        bindPlayerInventory(playerInventory);
    }

    protected void bindPlayerInventory(InventoryPlayer playerInventory) {
        // Player's inventory
        for (int row = 0; row < PLAYER_INVENTORY_ROWS; row++)
            for (int col = 0; col < PLAYER_INVENTORY_COLS; col++)
                addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));

        // Player's hotbar
        for (int col = 0; col < PLAYER_HOTBAR_COLS; col++)
            addSlotToContainer(new Slot(playerInventory, col, 8 + col * 18, 142));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return entity.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot slotObj = (Slot) inventorySlots.get(slot);

        if (slotObj == null || !slotObj.getHasStack())
            return stack;

        ItemStack stackInSlot = slotObj.getStack();
        stack = stackInSlot.copy();

        if ((slot < 9 && !mergeItemStack(stackInSlot, 9, 45, true)) ||
                !mergeItemStack(stackInSlot, 0, 9, false))
            return null;

        if (stackInSlot.stackSize == 0)
            slotObj.putStack(null);
        else
            slotObj.onSlotChanged();

        if (stackInSlot.stackSize == stack.stackSize)
            return null;

        slotObj.onPickupFromSlot(player, stackInSlot);

        return stack;
    }
}
