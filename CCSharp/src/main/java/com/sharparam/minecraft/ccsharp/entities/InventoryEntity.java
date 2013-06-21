/*
 * InventoryEntity.java
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

package com.sharparam.minecraft.ccsharp.entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * User: Sharparam
 * Date: 2013-06-19
 * Time: 18:36
 */
public class InventoryEntity extends BaseEntity implements IInventory {
    private static final byte DEFAULT_STACK_SIZE = 64;
    private static final int MAX_PLAYER_DISTANCE = 64;

    private final int inventoryCols;
    private final int inventoryRows;
    private final int inventorySize;
    private final int stackLimit;
    private final ItemStack[] inventory;

    public InventoryEntity(String id, String name, int cols, int rows) {
        this(id, name, cols, rows, DEFAULT_STACK_SIZE);
    }

    public InventoryEntity(String id, String name, int cols, int rows, int stackLimit) {
        super(id, name);
        inventoryCols = cols;
        inventoryRows = rows;
        inventorySize = inventoryCols * inventoryRows;
        this.stackLimit = stackLimit;
        inventory = new ItemStack[inventorySize];
    }

    public int getInventoryCols() {
        return inventoryCols;
    }

    public int getInventoryRows() {
        return inventoryRows;
    }

    @Override
    public int getSizeInventory() {
        return inventorySize;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = getStackInSlot(slot);

        if (stack == null)
            return stack;

        if (stack.stackSize <= amount)
            setInventorySlotContents(slot, null);
        else {
            stack = stack.splitStack(amount);
            if (stack.stackSize == 0)
                setInventorySlotContents(slot, null);
        }

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
            setInventorySlotContents(slot, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
    }

    @Override
    public String getInvName() {
        return name;
    }

    @Override
    public int getInventoryStackLimit() {
        return stackLimit;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < MAX_PLAYER_DISTANCE;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList list = tagCompound.getTagList("items");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);
            byte slot = tag.getByte("slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack == null)
                continue;
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("slot", (byte) i);
            stack.writeToNBT(tag);
            list.appendTag(tag);
        }
        tagCompound.setTag("items", list);
    }
}
