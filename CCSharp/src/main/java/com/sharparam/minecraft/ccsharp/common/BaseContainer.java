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

    private static final int DEFAULT_OFFSET_X = 8;
    private static final int DEFAULT_OFFSET_Y = 18;
    private static final int DEFAULT_SLOT_OFFSET_X = 18;
    private static final int DEFAULT_SLOT_OFFSET_Y = 18;

    private static final int DEFAULT_PLAYER_OFFSET_X = 8;
    private static final int DEFAULT_PLAYER_OFFSET_Y = 86;
    private static final int DEFAULT_PLAYER_SLOT_OFFSET_X = 18;
    private static final int DEFAULT_PLAYER_SLOT_OFFSET_Y = 18;
    private static final int DEFAULT_PLAYER_HOTBAR_OFFSET_X = 8;
    private static final int DEFAULT_PLAYER_HOTBAR_OFFSET_Y = 144;
    private static final int DEFAULT_PLAYER_HOTBAR_SLOT_OFFSET = 18;

    private final InventoryEntity entity;

    private final int offsetX;
    private final int offsetY;
    private final int slotOffsetX;
    private final int slotOffsetY;
    private final int playerOffsetX;
    private final int playerOffsetY;
    private final int playerSlotOffsetX;
    private final int playerSlotOffsetY;
    private final int playerHotbarOffsetX;
    private final int playerHotbarOffsetY;
    private final int playerHotbarSlotOffset;

    public BaseContainer(InventoryPlayer playerInventory, InventoryEntity entity) {
        this(playerInventory, entity, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y, DEFAULT_SLOT_OFFSET_X, DEFAULT_SLOT_OFFSET_Y);
    }

    public BaseContainer(InventoryPlayer playerInventory, InventoryEntity entity,
                         int offsetX, int offsetY, int slotOffsetX, int slotOffsetY) {
        this(playerInventory, entity,
                offsetX, offsetY, slotOffsetX, slotOffsetY,
                DEFAULT_PLAYER_OFFSET_X, DEFAULT_PLAYER_OFFSET_Y, DEFAULT_PLAYER_SLOT_OFFSET_X,
                DEFAULT_PLAYER_SLOT_OFFSET_Y, DEFAULT_PLAYER_HOTBAR_OFFSET_X, DEFAULT_PLAYER_HOTBAR_OFFSET_Y,
                DEFAULT_PLAYER_HOTBAR_SLOT_OFFSET);
    }

    public BaseContainer(InventoryPlayer playerInventory, InventoryEntity entity,
                         int offsetX, int offsetY, int slotOffsetX, int slotOffsetY,
                         int plrOffsetX, int plrOffsetY, int plrSlotOffsetX, int plrSlotOffsetY,
                         int plrHotbarOffsetX, int plrHotbarOffsetY, int plrHotbarSlotOffset) {
        this.entity = entity;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.slotOffsetX = slotOffsetX;
        this.slotOffsetY = slotOffsetY;
        playerOffsetX = plrOffsetX;
        playerOffsetY = plrOffsetY;
        playerSlotOffsetX = plrSlotOffsetX;
        playerSlotOffsetY = plrSlotOffsetY;
        playerHotbarOffsetX = plrHotbarOffsetX;
        playerHotbarOffsetY = plrHotbarOffsetY;
        playerHotbarSlotOffset = plrHotbarSlotOffset;

        int cols = entity.getInventoryCols();
        int rows = entity.getInventoryRows();

        // Inventory of entity
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                addSlotToContainer(new Slot(entity,
                        col + row * cols, // Slot index
                        this.offsetX + col * this.slotOffsetX,   // X position of slot
                        this.offsetY + row * this.slotOffsetY)); // Y position of slot

        bindPlayerInventory(playerInventory);
    }

    protected void bindPlayerInventory(InventoryPlayer playerInventory) {
        // Player's inventory
        for (int row = 0; row < PLAYER_INVENTORY_ROWS; row++)
            for (int col = 0; col < PLAYER_INVENTORY_COLS; col++)
                addSlotToContainer(new Slot(playerInventory,
                        col + row * PLAYER_INVENTORY_COLS + PLAYER_HOTBAR_COLS,
                        playerOffsetX + col * playerSlotOffsetX,
                        playerOffsetY + row * playerSlotOffsetY));

        // Player's hotbar
        for (int col = 0; col < PLAYER_HOTBAR_COLS; col++)
            addSlotToContainer(new Slot(playerInventory,
                    col,
                    playerHotbarOffsetX + col * playerHotbarSlotOffset,
                    playerHotbarOffsetY));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return entity.isUseableByPlayer(player);
    }

    @Override
    public boolean mergeItemStack(ItemStack stack, int begin, int end, boolean backwards) {
        int i = backwards ? end - 1 : begin;
        int increment = backwards ? -1 : 1;
        boolean flag = false;

        while (stack.stackSize > 0 && i >= begin && i < end) {
            Slot slot = this.getSlot(i);
            ItemStack slotStack = slot.getStack();
            int slotStackLimit = i < entity.getSizeInventory() ? entity.getInventoryStackLimit() : 64;
            int totalLimit = slotStackLimit < stack.getMaxStackSize() ? slotStackLimit : stack.getMaxStackSize();

            if (slotStack == null) {
                int transfer = totalLimit < stack.stackSize ? totalLimit : stack.stackSize;
                ItemStack stackToPut = stack.copy();
                stackToPut.stackSize = transfer;
                slot.putStack(stackToPut);
                slot.onSlotChanged();
                stack.stackSize -= transfer;
                flag = true;
            } else if (slotStack.itemID == stack.itemID &&
                    (!stack.getHasSubtypes() || stack.getItemDamage() == slotStack.getItemDamage()) &&
                    ItemStack.areItemStackTagsEqual(stack, slotStack)) {
                int maxTransfer = totalLimit - slotStack.stackSize;
                int transfer = maxTransfer > stack.stackSize ? stack.stackSize : maxTransfer;
                slotStack.stackSize += transfer;
                slot.onSlotChanged();
                stack.stackSize -= transfer;
                flag = true;
            }

            i += increment;
        }

        return flag;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot slotObj = (Slot) inventorySlots.get(slot);

        if (slotObj == null || !slotObj.getHasStack())
            return stack;

        ItemStack stackInSlot = slotObj.getStack();
        stack = stackInSlot.copy();
        int invSize = entity.getSizeInventory();

        if (slot < invSize) {
            if (mergeItemStack(stackInSlot, invSize, inventorySlots.size(), true))
                slotObj.onSlotChange(stackInSlot, stack);
            else
                return null;
        } else {
            if (!mergeItemStack(stackInSlot, 0, invSize, false))
                return null;
        }

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
