package it.bisumto.wetoxidation.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(targets = "net/minecraft/core/dispenser/DispenseItemBehavior$18")
public abstract class DispenseItemBehaviorMixin extends DefaultDispenseItemBehavior {
    @Inject(method = "execute",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/core/dispenser/DefaultDispenseItemBehavior;dispense(Lnet/minecraft/core/dispenser/BlockSource;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 1),
            cancellable = true)
    private void beforeDispense(BlockSource blockSource, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
        ServerLevel serverLevel = blockSource.level();
        BlockPos blockPos = blockSource.pos();
        BlockPos blockPos2 = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
        BlockState blockState =  serverLevel.getBlockState(blockPos2);
        Optional<Block> block = WeatheringCopper.getNext(blockState.getBlock());
        if (block.isPresent()) {
            serverLevel.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);
            serverLevel.setBlockAndUpdate(blockPos2, block.get().withPropertiesOf(blockState));
            cir.setReturnValue(this.consumeWithRemainder(blockSource, itemStack, new ItemStack(Items.GLASS_BOTTLE)));
        }
    }
}
