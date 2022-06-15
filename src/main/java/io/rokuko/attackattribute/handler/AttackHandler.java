package io.rokuko.attackattribute.handler;

import io.rokuko.attackattribute.AttackAttribute;
import io.rokuko.attackattribute.api.AttackAttributeEvent;
import io.rokuko.attackattribute.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class AttackHandler implements Listener {

    private AttackAttribute attackAttribute;

    public AttackHandler(AttackAttribute attackAttribute) {
        this.attackAttribute = attackAttribute;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            Player player = (Player) event.getDamager();
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            HashMap<String, String> attributes = hasAttributes(
                    itemInMainHand,
                    attackAttribute.config.attackIntervalName(),
                    attackAttribute.config.attackIntervalReductionName(),
                    attackAttribute.config.attackDistanceName());

            // Get the attribute lore from the item in main hand
            String attackIntervalAttributeLore = attributes.get(attackAttribute.config.attackIntervalName());
            String attackIntervalReductionAttributeLore = attributes.get(attackAttribute.config.attackIntervalReductionName());

            Double attackInterval = attackIntervalAttributeLore.isEmpty()? 0 : StringUtils.extractDouble(attackIntervalAttributeLore);
            Double attackIntervalReduction = attackIntervalReductionAttributeLore.isEmpty()? 0 : StringUtils.extractDouble(attackIntervalReductionAttributeLore);

            AttackAttributeEvent.PreAttack preAttackEvent = new AttackAttributeEvent.PreAttack(
                    player,
                    event.getEntity(),
                    attackInterval,
                    attackIntervalReduction);

            Bukkit.getServer().getPluginManager().callEvent(preAttackEvent);

            if (preAttackEvent.isCancelled()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    public String hasAttribute(ItemStack itemStack, String attributeName) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            for (String lore : itemStack.getItemMeta().getLore()) {
                if (lore.contains(attributeName)) return lore;
            }
        }
        return "";
    }

    public HashMap<String, String> hasAttributes(ItemStack itemStack, String...attributeNames) {
        HashMap<String, String> attributeLores = new HashMap<>();
        List<String> attributeNameList = new ArrayList<>(Arrays.asList(attributeNames));

        for (String attributeName : attributeNameList) {
            attributeLores.put(attributeName, "");
        }

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            for (String lore : itemStack.getItemMeta().getLore()) {
                String tmp = "";
                for (String attributeName: attributeNameList){
                    if (lore.contains(attributeName)) {
                        attributeLores.put(attributeName, lore);
                        tmp = attributeName;
                    }
                }
                attributeNameList.remove(tmp);
            }
        }

        return attributeLores;
    }

    @EventHandler
    public void onChange(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if (itemStack == null) return;

        String attackDistanceAttributeLore = hasAttribute(itemStack, attackAttribute.config.attackDistanceName());
        if (itemStack != null && !attackDistanceAttributeLore.isEmpty()){
            Double attackDistance = attackDistanceAttributeLore.isEmpty()? 0 : StringUtils.extractDouble(attackDistanceAttributeLore);
        }
    }

    private HashMap<UUidItemStackHolder, Long> lastAttackTimeHashMap = new HashMap<>();

    public class PostHandler implements Listener{

        @EventHandler
        public void onAttackAttributeEvent(AttackAttributeEvent.PreAttack event){
            Player player = event.getPlayer();
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            double attackInterval = event.getAttackInterval();
            double attackIntervalReduction = event.getAttackIntervalReduction() / 100f;
            UUidItemStackHolder holder = UUidItemStackHolder.of(player.getUniqueId(), itemInMainHand);
            if (lastAttackTimeHashMap.containsKey(holder)){
                long lastAttackTime = AttackHandler.this.lastAttackTimeHashMap.get(holder);
                System.out.println(lastAttackTime);
                double tickInterval = (System.currentTimeMillis() - lastAttackTime) / 50f * (1 - attackIntervalReduction);
                // If the player has not attacked in the last attackInterval, then he can attack
                if (tickInterval < attackInterval){
                    event.setCancelled(true);
                    player.sendMessage("攻击冷却: " + StringUtils.formatDouble(attackInterval - tickInterval));
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, 1, 1);
                } else {
                    player.setCooldown(itemInMainHand.getType(), (int) attackInterval);
                    lastAttackTimeHashMap.put(holder, System.currentTimeMillis());
                }
            } else {
                player.setCooldown(itemInMainHand.getType(), (int) attackInterval);
                lastAttackTimeHashMap.put(holder, System.currentTimeMillis());
            }
        }

    }

}
