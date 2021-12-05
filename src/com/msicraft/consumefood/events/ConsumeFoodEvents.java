package com.msicraft.consumefood.events;

import com.msicraft.consumefood.ConsumeFood;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ConsumeFoodEvents implements Listener {
    Plugin plugin = ConsumeFood.getPlugin(ConsumeFood.class);

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        ItemStack itemstack = e.getItem();
        int appleF = plugin.getConfig().getInt("Apple.FoodLevel");
        float appleS = (float) plugin.getConfig().getDouble("Apple.Saturation");
        if (itemstack.getType() == Material.APPLE) {
            if (player.getInventory().getItemInOffHand().getType() == Material.APPLE) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.APPLE, 1));
                player.setFoodLevel((player.getFoodLevel() + appleF));
                player.setSaturation((player.getSaturation() + appleS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int beefF = plugin.getConfig().getInt("Beef.FoodLevel");
        float beefS = (float) plugin.getConfig().getDouble("Beef.Saturation");
        if (itemstack.getType() == Material.BEEF) {
            if (player.getInventory().getItemInOffHand().getType() == Material.BEEF) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.BEEF, 1));
                player.setFoodLevel((player.getFoodLevel() + beefF));
                player.setSaturation((player.getSaturation() + beefS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int beetrootF = plugin.getConfig().getInt("Beetroot.FoodLevel");
        float beetrootS = (float) plugin.getConfig().getDouble("Beetroot.Saturation");
        if (itemstack.getType() == Material.BEETROOT) {
            if (player.getInventory().getItemInOffHand().getType() == Material.BEETROOT) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.BEETROOT, 1));
                player.setFoodLevel((player.getFoodLevel() + beetrootF));
                player.setSaturation((player.getSaturation() + beetrootS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Baked_potatoF = plugin.getConfig().getInt("Baked_potato.FoodLevel");
        float Baked_potatoS = (float) plugin.getConfig().getDouble("Baked_potato.Saturation");
        if (itemstack.getType() == Material.BAKED_POTATO) {
            if (player.getInventory().getItemInOffHand().getType() == Material.BAKED_POTATO) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.BAKED_POTATO, 1));
                player.setFoodLevel((player.getFoodLevel() + Baked_potatoF));
                player.setSaturation((player.getSaturation() + Baked_potatoS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int BreadF = plugin.getConfig().getInt("Bread.FoodLevel");
        float BreadS = (float) plugin.getConfig().getDouble("Bread.Saturation");
        if (itemstack.getType() == Material.BREAD) {
            if (player.getInventory().getItemInOffHand().getType() == Material.BREAD) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.BREAD, 1));
                player.setFoodLevel((player.getFoodLevel() + BreadF));
                player.setSaturation((player.getSaturation() + BreadS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Beetroot_SoupF = plugin.getConfig().getInt("Beetroot_Soup.FoodLevel");
        float Beetroot_SoupS = (float) plugin.getConfig().getDouble("Beetroot_Soup.Saturation");
        if (itemstack.getType() == Material.BEETROOT_SOUP) {
            if (player.getInventory().getItemInOffHand().getType() == Material.BEETROOT_SOUP) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.BEETROOT_SOUP, 1));
                player.setFoodLevel((player.getFoodLevel() + Beetroot_SoupF));
                player.setSaturation((player.getSaturation() + Beetroot_SoupS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int ChickenF = plugin.getConfig().getInt("Chicken.FoodLevel");
        float ChickenS = (float) plugin.getConfig().getDouble("Chicken.Saturation");
        if (itemstack.getType() == Material.CHICKEN) {
            if (player.getInventory().getItemInOffHand().getType() == Material.CHICKEN) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.CHICKEN, 1));
                player.setFoodLevel((player.getFoodLevel() + ChickenF));
                player.setSaturation((player.getSaturation() + ChickenS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int CodF = plugin.getConfig().getInt("Cod.FoodLevel");
        float CodS = (float) plugin.getConfig().getDouble("Cod.Saturation");
        if (itemstack.getType() == Material.COD) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COD) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COD, 1));
                player.setFoodLevel((player.getFoodLevel() + CodF));
                player.setSaturation((player.getSaturation() + CodS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int CarrotF = plugin.getConfig().getInt("Carrot.FoodLevel");
        float CarrotS = (float) plugin.getConfig().getDouble("Carrot.Saturation");
        if (itemstack.getType() == Material.CARROT) {
            if (player.getInventory().getItemInOffHand().getType() == Material.CARROT) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.CARROT, 1));
                player.setFoodLevel((player.getFoodLevel() + CarrotF));
                player.setSaturation((player.getSaturation() + CarrotS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Cooked_ChickenF = plugin.getConfig().getInt("Cooked_Chicken.FoodLevel");
        float Cooked_ChickenS = (float) plugin.getConfig().getDouble("Cooked_Chicken.Saturation");
        if (itemstack.getType() == Material.COOKED_CHICKEN) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COOKED_CHICKEN) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COOKED_CHICKEN, 1));
                player.setFoodLevel((player.getFoodLevel() + Cooked_ChickenF));
                player.setSaturation((player.getSaturation() + Cooked_ChickenS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Cooked_RabbitF = plugin.getConfig().getInt("Cooked_Rabbit.FoodLevel");
        float Cooked_RabbitS = (float) plugin.getConfig().getDouble("Cooked_Rabbit.Saturation");
        if (itemstack.getType() == Material.COOKED_RABBIT) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COOKED_RABBIT) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COOKED_RABBIT, 1));
                player.setFoodLevel((player.getFoodLevel() + Cooked_RabbitF));
                player.setSaturation((player.getSaturation() + Cooked_RabbitS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Cooked_PorkchopF = plugin.getConfig().getInt("Cooked_Porkchop.FoodLevel");
        float Cooked_PorkchopS = (float) plugin.getConfig().getDouble("Cooked_Porkchop.Saturation");
        if (itemstack.getType() == Material.COOKED_PORKCHOP) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COOKED_PORKCHOP) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COOKED_PORKCHOP, 1));
                player.setFoodLevel((player.getFoodLevel() + Cooked_PorkchopF));
                player.setSaturation((player.getSaturation() + Cooked_PorkchopS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Cooked_BeefF = plugin.getConfig().getInt("Cooked_Beef.FoodLevel");
        float Cooked_BeefS = (float) plugin.getConfig().getDouble("Cooked_Beef.Saturation");
        if (itemstack.getType() == Material.COOKED_BEEF) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COOKED_BEEF) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COOKED_BEEF, 1));
                player.setFoodLevel((player.getFoodLevel() + Cooked_BeefF));
                player.setSaturation((player.getSaturation() + Cooked_BeefS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Cooked_MuttonF = plugin.getConfig().getInt("Cooked_Mutton.FoodLevel");
        float Cooked_MuttonS = (float) plugin.getConfig().getDouble("Cooked_Mutton.Saturation");
        if (itemstack.getType() == Material.COOKED_MUTTON) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COOKED_MUTTON) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COOKED_MUTTON, 1));
                player.setFoodLevel((player.getFoodLevel() + Cooked_MuttonF));
                player.setSaturation((player.getSaturation() + Cooked_MuttonS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Cooked_CodF = plugin.getConfig().getInt("Cooked_Cod.FoodLevel");
        float Cooked_CodS = (float) plugin.getConfig().getDouble("Cooked_Cod.Saturation");
        if (itemstack.getType() == Material.COOKED_COD) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COOKED_COD) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COOKED_COD, 1));
                player.setFoodLevel((player.getFoodLevel() + Cooked_CodF));
                player.setSaturation((player.getSaturation() + Cooked_CodS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Cooked_SalmonF = plugin.getConfig().getInt("Cooked_Salmon.FoodLevel");
        float Cooked_SalmonS = (float) plugin.getConfig().getDouble("Cooked_Salmon.Saturation");
        if (itemstack.getType() == Material.COOKED_SALMON) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COOKED_SALMON) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COOKED_SALMON, 1));
                player.setFoodLevel((player.getFoodLevel() + Cooked_SalmonF));
                player.setSaturation((player.getSaturation() + Cooked_SalmonS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int CookieF = plugin.getConfig().getInt("Cookie.FoodLevel");
        float CookieS = (float) plugin.getConfig().getDouble("Cookie.Saturation");
        if (itemstack.getType() == Material.COOKIE) {
            if (player.getInventory().getItemInOffHand().getType() == Material.COOKIE) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.COOKIE, 1));
                player.setFoodLevel((player.getFoodLevel() + CookieF));
                player.setSaturation((player.getSaturation() + CookieS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Dried_KelpF = plugin.getConfig().getInt("Dried_Kelp.FoodLevel");
        float Dried_KelpS = (float) plugin.getConfig().getDouble("Dried_Kelp.Saturation");
        if (itemstack.getType() == Material.DRIED_KELP) {
            if (player.getInventory().getItemInOffHand().getType() == Material.DRIED_KELP) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.DRIED_KELP, 1));
                player.setFoodLevel((player.getFoodLevel() + Dried_KelpF));
                player.setSaturation((player.getSaturation() + Dried_KelpS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Glow_BerriesF = plugin.getConfig().getInt("Glow_Berries.FoodLevel");
        float Glow_BerriesS = (float) plugin.getConfig().getDouble("Glow_Berries.Saturation");
        if (itemstack.getType() == Material.GLOW_BERRIES) {
            if (player.getInventory().getItemInOffHand().getType() == Material.GLOW_BERRIES) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.GLOW_BERRIES, 1));
                player.setFoodLevel((player.getFoodLevel() + Glow_BerriesF));
                player.setSaturation((player.getSaturation() + Glow_BerriesS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Honey_BottleF = plugin.getConfig().getInt("Honey_Bottle.FoodLevel");
        float Honey_BottleS = (float) plugin.getConfig().getDouble("Honey_Bottle.Saturation");
        if (itemstack.getType() == Material.HONEY_BOTTLE) {
            if (player.getInventory().getItemInOffHand().getType() == Material.HONEY_BOTTLE) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.HONEY_BOTTLE, 1));
                player.setFoodLevel((player.getFoodLevel() + Honey_BottleF));
                player.setSaturation((player.getSaturation() + Honey_BottleS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int MuttonF = plugin.getConfig().getInt("Mutton.FoodLevel");
        float MuttonS = (float) plugin.getConfig().getDouble("Mutton.Saturation");
        if (itemstack.getType() == Material.MUTTON) {
            if (player.getInventory().getItemInOffHand().getType() == Material.MUTTON) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.MUTTON, 1));
                player.setFoodLevel((player.getFoodLevel() + MuttonF));
                player.setSaturation((player.getSaturation() + MuttonS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Melon_SliceF = plugin.getConfig().getInt("Melon_Slice.FoodLevel");
        float Melon_SliceS = (float) plugin.getConfig().getDouble("Melon_Slice.Saturation");
        if (itemstack.getType() == Material.MELON_SLICE) {
            if (player.getInventory().getItemInOffHand().getType() == Material.MELON_SLICE) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.MELON_SLICE, 1));
                player.setFoodLevel((player.getFoodLevel() + Melon_SliceF));
                player.setSaturation((player.getSaturation() + Melon_SliceS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Mushroom_StewF = plugin.getConfig().getInt("Mushroom_Stew.FoodLevel");
        float Mushroom_StewS = (float) plugin.getConfig().getDouble("Mushroom_Stew.Saturation");
        if (itemstack.getType() == Material.MUSHROOM_STEW) {
            if (player.getInventory().getItemInOffHand().getType() == Material.MUSHROOM_STEW) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.MUSHROOM_STEW, 1));
                player.setFoodLevel((player.getFoodLevel() + Mushroom_StewF));
                player.setSaturation((player.getSaturation() + Mushroom_StewS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int PotatoF = plugin.getConfig().getInt("Potato.FoodLevel");
        float PotatoS = (float) plugin.getConfig().getDouble("Potato.Saturation");
        if (itemstack.getType() == Material.POTATO) {
            if (player.getInventory().getItemInOffHand().getType() == Material.POTATO) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.POTATO, 1));
                player.setFoodLevel((player.getFoodLevel() + PotatoF));
                player.setSaturation((player.getSaturation() + PotatoS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int PorkChopF = plugin.getConfig().getInt("PorkChop.FoodLevel");
        float PorkChopS = (float) plugin.getConfig().getDouble("PorkChop.Saturation");
        if (itemstack.getType() == Material.PORKCHOP) {
            if (player.getInventory().getItemInOffHand().getType() == Material.PORKCHOP) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.PORKCHOP, 1));
                player.setFoodLevel((player.getFoodLevel() + PorkChopF));
                player.setSaturation((player.getSaturation() + PorkChopS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Pumpkin_pieF = plugin.getConfig().getInt("Pumpkin_pie.FoodLevel");
        float Pumpkin_pieS = (float) plugin.getConfig().getDouble("Pumpkin_pie.Saturation");
        if (itemstack.getType() == Material.PUMPKIN_PIE) {
            if (player.getInventory().getItemInOffHand().getType() == Material.PUMPKIN_PIE) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.PUMPKIN_PIE, 1));
                player.setFoodLevel((player.getFoodLevel() + Pumpkin_pieF));
                player.setSaturation((player.getSaturation() + Pumpkin_pieS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int RabbitF = plugin.getConfig().getInt("Rabbit.FoodLevel");
        float RabbitS = (float) plugin.getConfig().getDouble("Rabbit.Saturation");
        if (itemstack.getType() == Material.RABBIT) {
            if (player.getInventory().getItemInOffHand().getType() == Material.RABBIT) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.RABBIT, 1));
                player.setFoodLevel((player.getFoodLevel() + RabbitF));
                player.setSaturation((player.getSaturation() + RabbitS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Rabbit_StewF = plugin.getConfig().getInt("Rabbit_Stew.FoodLevel");
        float Rabbit_StewS = (float) plugin.getConfig().getDouble("Rabbit_Stew.Saturation");
        if (itemstack.getType() == Material.RABBIT_STEW) {
            if (player.getInventory().getItemInOffHand().getType() == Material.RABBIT_STEW) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.RABBIT_STEW, 1));
                player.setFoodLevel((player.getFoodLevel() + Rabbit_StewF));
                player.setSaturation((player.getSaturation() + Rabbit_StewS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int SalmonF = plugin.getConfig().getInt("Salmon.FoodLevel");
        float SalmonS = (float) plugin.getConfig().getDouble("Salmon.Saturation");
        if (itemstack.getType() == Material.SALMON) {
            if (player.getInventory().getItemInOffHand().getType() == Material.SALMON) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.SALMON, 1));
                player.setFoodLevel((player.getFoodLevel() + SalmonF));
                player.setSaturation((player.getSaturation() + SalmonS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Sweet_BerriesF = plugin.getConfig().getInt("Sweet_Berries.FoodLevel");
        float Sweet_BerriesS = (float) plugin.getConfig().getDouble("Sweet_Berries.Saturation");
        if (itemstack.getType() == Material.SWEET_BERRIES) {
            if (player.getInventory().getItemInOffHand().getType() == Material.SWEET_BERRIES) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.SWEET_BERRIES, 1));
                player.setFoodLevel((player.getFoodLevel() + Sweet_BerriesF));
                player.setSaturation((player.getSaturation() + Sweet_BerriesS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
        int Tropical_FishF = plugin.getConfig().getInt("Tropical_Fish.FoodLevel");
        float Tropical_FishS = (float) plugin.getConfig().getDouble("Tropical_Fish.Saturation");
        if (itemstack.getType() == Material.TROPICAL_FISH) {
            if (player.getInventory().getItemInOffHand().getType() == Material.TROPICAL_FISH) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.getInventory().removeItem(new ItemStack(Material.TROPICAL_FISH, 1));
                player.setFoodLevel((player.getFoodLevel() + Tropical_FishF));
                player.setSaturation((player.getSaturation() + Tropical_FishS));
                if (player.getFoodLevel() >= 21) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 21) {
                    player.setSaturation(20);
                }
            }
        }
        //
    }
}


