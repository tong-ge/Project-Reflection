package bruce.projectreflection.network;

import bruce.projectreflection.ProjectReflection;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBSOD implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<MessageBSOD, IMessage> {
        @Override
        public IMessage onMessage(MessageBSOD message, MessageContext ctx) {
            ProjectReflection.proxy.blueScreenOfDeath();
            return null;
        }
    }
}
