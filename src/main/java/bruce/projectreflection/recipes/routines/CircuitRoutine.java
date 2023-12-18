package bruce.projectreflection.recipes.routines;

import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.MarkerMaterial;
import gregtech.api.unification.material.MarkerMaterials;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;

import static gregtech.common.items.MetaItems.*;

public class CircuitRoutine {
    public static void init() {
        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(MetaItems.INTEGRATED_CIRCUIT_MV.getStackForm(2))
                .inputs(MetaItems.ELECTRONIC_CIRCUIT_MV.getStackForm(2),
                        MetaItems.INTEGRATED_LOGIC_CIRCUIT.getStackForm(1),
                        OreDictUnifier.get(OrePrefix.wireFine, Materials.Gold, 4),
                        OreDictUnifier.get(OrePrefix.bolt, Materials.Silver, 4),
                        OreDictUnifier.get(OrePrefix.bolt, Materials.Tin, 2))
                .EUt(30)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(MetaItems.PROCESSOR_ASSEMBLY_HV.getStackForm(1))
                .inputs(MetaItems.PLASTIC_CIRCUIT_BOARD.getStackForm(2),
                        MetaItems.INTEGRATED_CIRCUIT_HV.getStackForm(1),
                        MetaItems.CENTRAL_PROCESSING_UNIT.getStackForm(1),
                        //MetaItems.CAPACITOR.getStackForm(12),
                        //MetaItems.INDUCTOR.getStackForm(4),
                        OreDictUnifier.get(OrePrefix.component, MarkerMaterials.Component.Capacitor, 12),
                        OreDictUnifier.get(OrePrefix.component, MarkerMaterials.Component.Inductor, 4),
                        MetaItems.RANDOM_ACCESS_MEMORY.getStackForm(2))
                .EUt(90)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(MetaItems.NANO_COMPUTER_IV.getStackForm(1))
                .inputs(MetaItems.ADVANCED_CIRCUIT_BOARD.getStackForm(5),
                        MetaItems.MAINFRAME_IV.getStackForm(1),
                        MetaItems.NANO_CENTRAL_PROCESSING_UNIT.getStackForm(2),
                        MetaItems.NOR_MEMORY_CHIP.getStackForm(4)
                        //TODO 贴片元件包：32个电阻、32个三极管、64个电容、16个电感、16个二极管，
                        // 用纸和胶水包装，右击可取用
                )
                .EUt(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(MetaItems.QUANTUM_COMPUTER_LUV.getStackForm(1))
                .inputs(MetaItems.EXTREME_CIRCUIT_BOARD.getStackForm(5),
                        MetaItems.NANO_MAINFRAME_LUV.getStackForm(1),
                        MetaItems.QUBIT_CENTRAL_PROCESSING_UNIT.getStackForm(2),
                        OreDictUnifier.get(OrePrefix.wireGtSingle, Materials.Platinum, 22)
                )
                .EUt(2400)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.ASSEMBLY_LINE_RECIPES.recipeBuilder()
                .outputs(MetaItems.CRYSTAL_COMPUTER_ZPM.getStackForm(1))
                .inputs(MetaItems.ELITE_CIRCUIT_BOARD.getStackForm(5),
                        MetaItems.QUANTUM_MAINFRAME_ZPM.getStackForm(1),
                        MetaItems.CRYSTAL_CENTRAL_PROCESSING_UNIT.getStackForm(2),
                        MetaItems.NAND_MEMORY_CHIP.getStackForm(64),
                        MetaItems.NOR_MEMORY_CHIP.getStackForm(16),
                        MetaItems.ADVANCED_SMD_INDUCTOR.getStackForm(8),
                        MetaItems.ADVANCED_SMD_CAPACITOR.getStackForm(28),
                        MetaItems.ADVANCED_SMD_TRANSISTOR.getStackForm(12),
                        OreDictUnifier.get(OrePrefix.wireGtSingle, Materials.NiobiumTitanium, 20)
                )
                .fluidInputs(Materials.SolderingAlloy.getFluid(1152))
                .scannerResearch(scannerRecipeBuilder -> scannerRecipeBuilder
                        .researchStack(MetaItems.QUANTUM_MAINFRAME_ZPM.getStackForm())
                        .EUt(7680)
                )
                .EUt(9600)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.ASSEMBLY_LINE_RECIPES.recipeBuilder()
                .outputs(MetaItems.WETWARE_SUPER_COMPUTER_UV.getStackForm(1))
                .inputs(MetaItems.WETWARE_CIRCUIT_BOARD.getStackForm(3),
                        MetaItems.NEURO_PROCESSOR.getStackForm(2),
                        MetaItems.CRYSTAL_MAINFRAME_UV.getStackForm(1),
                        OreDictUnifier.get(OrePrefix.wireGtSingle, Materials.YttriumBariumCuprate, 18),
                        OreDictUnifier.get(OrePrefix.foil, Materials.Polybenzimidazole, 32),
                        OreDictUnifier.get(OrePrefix.plate, Materials.Europium, 4)
                )
                .fluidInputs(Materials.SolderingAlloy.getFluid(1152))
                .EUt(38400)
                .duration(400)
                .stationResearch(
                        stationRecipeBuilder -> stationRecipeBuilder
                                .researchStack(MetaItems.CRYSTAL_MAINFRAME_UV.getStackForm())
                                .EUt(30720)
                                .CWUt(16, 64000)
                )
                .buildAndRegister();
        //切板，启动！
        //HV->MV->LV
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(MetaItems.PROCESSOR_MV.getStackForm(2))
                .inputs(MetaItems.PROCESSOR_ASSEMBLY_HV.getStackForm(1))
                .EUt(60)
                .duration(200)
                .buildAndRegister();
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(MetaItems.MICROPROCESSOR_LV.getStackForm(3))
                .inputs(MetaItems.PROCESSOR_MV.getStackForm(2))
                .EUt(60)
                .duration(200)
                .buildAndRegister();
        //IV->EV->HV
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(MetaItems.NANO_PROCESSOR_ASSEMBLY_EV.getStackForm(2))
                .inputs(MetaItems.NANO_COMPUTER_IV.getStackForm(1))
                .EUt(600)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(MetaItems.NANO_PROCESSOR_HV.getStackForm(2))
                .inputs(MetaItems.NANO_PROCESSOR_ASSEMBLY_EV.getStackForm(1))
                .EUt(600)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();
        //LuV->IV->EV
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(QUANTUM_ASSEMBLY_IV.getStackForm(2))
                .inputs(QUANTUM_COMPUTER_LUV.getStackForm(1))
                .EUt(2400)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(QUANTUM_PROCESSOR_EV.getStackForm(2))
                .inputs(QUANTUM_ASSEMBLY_IV.getStackForm(1))
                .EUt(2400)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();
        //ZPM->LuV->IV
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(CRYSTAL_ASSEMBLY_LUV.getStackForm(2))
                .inputs(CRYSTAL_COMPUTER_ZPM.getStackForm(1))
                .EUt(9600)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(CRYSTAL_PROCESSOR_IV.getStackForm(2))
                .inputs(CRYSTAL_ASSEMBLY_LUV.getStackForm(1))
                .EUt(9600)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();
        //UV->ZPM->LuV
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(WETWARE_PROCESSOR_ASSEMBLY_ZPM.getStackForm(2))
                .inputs(WETWARE_SUPER_COMPUTER_UV.getStackForm(1))
                .EUt(38400)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(WETWARE_PROCESSOR_LUV.getStackForm(2))
                .inputs(WETWARE_PROCESSOR_ASSEMBLY_ZPM.getStackForm(1))
                .EUt(38400)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .buildAndRegister();
    }
}
