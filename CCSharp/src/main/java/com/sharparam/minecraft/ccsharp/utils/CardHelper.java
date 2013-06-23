package com.sharparam.minecraft.ccsharp.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Sharparam
 * Date: 2013-06-23
 * Time: 03:20
 */
public final class CardHelper {
    private static final int MAX_NUM_RANGED_UPGRADES = 1024;

    private static final class CardWrapper extends CardWrapperImpl {
        private static final Pattern ENERGY_PATTERN = Pattern.compile("^_(\\d+)energy$");
        private static final Pattern MAX_ENERGY_PATTERN = Pattern.compile("^_(\\d+)maxStorage$");

        private final HashMap<String, Object> cardData;

        public CardWrapper(ItemStack card) {
            super(card);

            cardData = new HashMap<String, Object>();
        }

        private void put(String name, Object value) {
            cardData.put(name, value);
        }

        public HashMap<String, Object> getParsedMap() {
            return getParsedMap(cardData);
        }

        private HashMap<String, Object> getParsedMap(HashMap<String, Object> map) {
            HashMap<String, Object> result = new HashMap<String, Object>();
            HashMap<Double, HashMap<String, Object>> subCards = new HashMap<Double, HashMap<String, Object>>();

            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (key.equals("energyL") || key.equals("amount")) {
                    result.put("Current", value);
                } else if (key.equals("maxStorageL") || key.equals("capacity")) {
                    result.put("Max", value);
                } else if (key.equals("liquidId")) {
                    result.put("Id", value);
                } else if (key.equals("liquidMeta")) {
                    result.put("Meta", value);
                } else if (key.matches(ENERGY_PATTERN.pattern())) {
                    Matcher match = ENERGY_PATTERN.matcher(key);
                    Double id = Double.parseDouble(match.group(1));

                    if (!subCards.containsKey(id))
                        subCards.put(id, new HashMap<String, Object>());

                    subCards.get(id).put("Energy", value);
                    int energy = Integer.parseInt(value.toString());

                    if (result.containsKey("Energy"))
                        energy += Integer.parseInt(result.get("Energy").toString());

                    result.put("Energy", energy);
                } else if (key.matches(MAX_ENERGY_PATTERN.pattern())) {
                    Matcher match = MAX_ENERGY_PATTERN.matcher(key);
                    Double id = Double.parseDouble(match.group(1));

                    if (!subCards.containsKey(id))
                        subCards.put(id, new HashMap<String, Object>());

                    subCards.get(id).put("Max", value);
                    int max = Integer.parseInt(value.toString());

                    if (result.containsKey("Max"))
                        max += Integer.parseInt(result.get("Max").toString());

                    result.put("Max", max);
                } else {
                    result.put(key, value);
                }
            }

            if (!subCards.isEmpty()) {
                result.put("CardCount", subCards.size());
                result.put("Cards", subCards);
            }

            return result;
        }

        @Override
        public void commit(TileEntity panel) {
        }

        @Override
        public void setBoolean(String name, Boolean value) {
            put(name, value);
        }

        @Override
        public void setInt(String name, Integer value) {
            put(name, value);
        }

        @Override
        public void setLong(String name, Long value) {
            put(name, value);
        }

        @Override
        public void setString(String name, String value) {
            put(name, value);
        }
    }

    public static HashMap<String, Object> getCardInfo(TileEntity entity, ItemStack stack) {
        HashMap<String, Object> result = null;
        Item item = stack == null ? null : stack.getItem();
        if (item != null && item instanceof IPanelDataSource) {
            ICardWrapper wrapper = new CardWrapper(stack);
            IPanelDataSource dataSource = (IPanelDataSource) item;
            result = new HashMap<String, Object>();
            result.put("UUID", dataSource.getCardType().toString());
            result.put("State", dataSource.update(entity, wrapper, MAX_NUM_RANGED_UPGRADES).toString());
            result.put("Title", wrapper.getTitle());
            HashMap<String, Object> data = ((CardWrapper) wrapper).getParsedMap();
            for (String key : data.keySet())
                result.put(key, data.get(key));
        }
        return result;
    }
}
