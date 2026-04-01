package com.execcexrrvycvtvtv.ancient_horizons.item;

import com.execcexrrvycvtvtv.ancient_horizons.entity.custom.mob.misc.AbstractEagleEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class FalconryGloveItem extends Item {
    public FalconryGloveItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.level();

        if (target instanceof AbstractEagleEntity eagle && eagle.getOwner() == player) {
            if (!level.isClientSide) {
                if (eagle.hasItems()) {
                    eagle.dropInventoryContents();
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (!level.isClientSide) {
            boolean isEnemy = true;
            if (target instanceof OwnableEntity ownable) {
                if (player.getUUID().equals(ownable.getOwnerUUID())) {
                    isEnemy = false;
                }
            }

            if (isEnemy) {
                AABB area = player.getBoundingBox().inflate(32.0);
                List<AbstractEagleEntity> myEagles = level.getEntitiesOfClass(
                        AbstractEagleEntity.class,
                        area,
                        eagle -> eagle.isTame() && player.getUUID().equals(eagle.getOwnerUUID())
                );

                if (!myEagles.isEmpty()) {
                    for (AbstractEagleEntity eagle : myEagles) {
                        eagle.setTarget(target);

                        if (eagle.isOrderedToSit()) {
                            eagle.setOrderedToSit(false);
                        }
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}