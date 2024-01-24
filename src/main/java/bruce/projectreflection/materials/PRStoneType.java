package bruce.projectreflection.materials;

import com.shinoow.abyssalcraft.api.block.ACBlocks;
import com.shinoow.abyssalcraft.common.blocks.BlockACStone;
import gregtech.api.unification.ore.StoneType;
import net.minecraft.block.SoundType;

public class PRStoneType {
    public static final StoneType DARKSTONE = new StoneType(64, "darkstone",
            SoundType.STONE, PROrePrefixes.oreDark, FirstTierMaterials.DARK_STONE, () -> ACBlocks.stone.getDefaultState().withProperty(BlockACStone.TYPE, BlockACStone.EnumStoneType.DARKSTONE),
            state -> state.getBlock() instanceof BlockACStone && state.getValue(BlockACStone.TYPE) == BlockACStone.EnumStoneType.DARKSTONE, true);
    public static final StoneType ABYSSAL_STONE = new StoneType(65, "abyssal_stone",
            SoundType.STONE, PROrePrefixes.oreAbyssal, FirstTierMaterials.ABYSSAL_STONE, () -> ACBlocks.stone.getDefaultState().withProperty(BlockACStone.TYPE, BlockACStone.EnumStoneType.ABYSSAL_STONE),
            state -> state.getBlock() instanceof BlockACStone && state.getValue(BlockACStone.TYPE) == BlockACStone.EnumStoneType.ABYSSAL_STONE, true);
    public static final StoneType DREAD_STONE = new StoneType(66, "dreadstone",
            SoundType.STONE, PROrePrefixes.oreDread, FirstTierMaterials.DREAD_STONE, () -> ACBlocks.stone.getDefaultState().withProperty(BlockACStone.TYPE, BlockACStone.EnumStoneType.DREADSTONE),
            state -> state.getBlock() instanceof BlockACStone && state.getValue(BlockACStone.TYPE) == BlockACStone.EnumStoneType.DREADSTONE, true);
    public static final StoneType ABYSSALNITE_STONE = new StoneType(67, "abyssalnite_stone",
            SoundType.STONE, PROrePrefixes.orePurifiedDread, FirstTierMaterials.ABYSSALNITE_STONE, () -> ACBlocks.stone.getDefaultState().withProperty(BlockACStone.TYPE, BlockACStone.EnumStoneType.ABYSSALNITE_STONE),
            state -> state.getBlock() instanceof BlockACStone && state.getValue(BlockACStone.TYPE) == BlockACStone.EnumStoneType.ABYSSALNITE_STONE, true);
    public static final StoneType OMOTHOL_STONE = new StoneType(68, "omothol_stone",
            SoundType.STONE, PROrePrefixes.oreOmothol, FirstTierMaterials.OMOTHOL_STONE, () -> ACBlocks.stone.getDefaultState().withProperty(BlockACStone.TYPE, BlockACStone.EnumStoneType.OMOTHOL_STONE),
            state -> state.getBlock() instanceof BlockACStone && state.getValue(BlockACStone.TYPE) == BlockACStone.EnumStoneType.OMOTHOL_STONE, true);
}
