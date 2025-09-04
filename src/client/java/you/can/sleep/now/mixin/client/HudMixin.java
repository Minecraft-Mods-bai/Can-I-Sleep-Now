package you.can.sleep.now.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugHud.class)
public class HudMixin {
    @Inject(method = "getRightText", at = @At("RETURN"), cancellable = true)
    private void onGetRightText(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = new ArrayList<>(cir.getReturnValue());

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            long time = client.world.getTimeOfDay();
            time = time % 24000;
            String message;

            if (client.world.isRaining() && time >= 12010) {
                message = "§aIt's raining, so you can sleep";
            } else if (time >= 12542 && time < 23460) {
                message = "§aGo ahead and sleep";
            } else {
                long tillSleep;
                if (time < 12542) {
                    tillSleep = 12542 - time;
                } else { // time >= 23460
                    tillSleep = 24000 + 12542 - time;
                }

                message = "§cYou can't sleep, wait " + (tillSleep / 20) + " seconds";
            }
            list.add(message);
        }
        cir.setReturnValue(list);
    }
}
