package bruce.projectreflection.recipes.routines;

import bruce.projectreflection.ProjectReflection;
import bruce.projectreflection.items.PRMetaItems;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.MarkerMaterial;
import gregtech.api.unification.material.MarkerMaterials;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.items.MetaItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static gregtech.common.items.MetaItems.*;

public class CircuitRoutine {
    public static void init() {
        ProjectReflection.logger.info("projectreflection init circuitroutine");

        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(PRMetaItems.INTEGRATED_NIC.getStackForm(2))
                .inputs(MetaItems.INTEGRATED_LOGIC_CIRCUIT.getStackForm(1),
                        OreDictUnifier.get(OrePrefix.wireFine, Materials.Gold, 4),
                        OreDictUnifier.get(OrePrefix.bolt, Materials.Silver, 4),
                        OreDictUnifier.get(OrePrefix.bolt, Materials.Tin, 2))
                .EUt(30)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.FORMING_PRESS_RECIPES.recipeBuilder()
                .outputs(MetaItems.INTEGRATED_CIRCUIT_MV.getStackForm(1))
                .inputs(MetaItems.ELECTRONIC_CIRCUIT_MV.getStackForm(1),
                        PRMetaItems.INTEGRATED_NIC.getStackForm(1))
                .EUt(30)
                .duration(400)
                .buildAndRegister();

        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(PRMetaItems.PROCESSOR_NIC.getStackForm(1))
                .inputs(MetaItems.PLASTIC_CIRCUIT_BOARD.getStackForm(2),
                        MetaItems.CENTRAL_PROCESSING_UNIT.getStackForm(1),
                        OreDictUnifier.get(OrePrefix.component, MarkerMaterials.Component.Capacitor, 12),
                        OreDictUnifier.get(OrePrefix.component, MarkerMaterials.Component.Inductor, 4),
                        MetaItems.RANDOM_ACCESS_MEMORY.getStackForm(2))
                .EUt(90)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.FORMING_PRESS_RECIPES.recipeBuilder()
                .outputs(MetaItems.PROCESSOR_ASSEMBLY_HV.getStackForm(1))
                .inputs(MetaItems.INTEGRATED_CIRCUIT_HV.getStackForm(1),
                        PRMetaItems.PROCESSOR_NIC.getStackForm(1))
                .EUt(GTValues.VA[GTValues.HV])
                .duration(20)
                .buildAndRegister();
        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(PRMetaItems.SMD_COMPONENT_PACKAGE.getStackForm(1))
                .inputs(new ItemStack(Items.PAPER),
                        SMD_RESISTOR.getStackForm(32),
                        SMD_TRANSISTOR.getStackForm(32),
                        SMD_CAPACITOR.getStackForm(64),
                        SMD_DIODE.getStackForm(16),
                        SMD_INDUCTOR.getStackForm(16))
                .fluidInputs(Materials.Glue.getFluid(100))
                .EUt(GTValues.VA[GTValues.HV])
                .duration(20)
                .buildAndRegister();

        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(PRMetaItems.NANO_NIC.getStackForm(1))
                .inputs(MetaItems.ADVANCED_CIRCUIT_BOARD.getStackForm(5),
                        //MetaItems.MAINFRAME_IV.getStackForm(1),
                        MetaItems.NANO_CENTRAL_PROCESSING_UNIT.getStackForm(2),
                        MetaItems.NOR_MEMORY_CHIP.getStackForm(4),
                        PRMetaItems.SMD_COMPONENT_PACKAGE.getStackForm(1)
                )
                .EUt(GTValues.VA[GTValues.EV])
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.FORMING_PRESS_RECIPES.recipeBuilder()
                .outputs(MetaItems.NANO_COMPUTER_IV.getStackForm(1))
                .inputs(MetaItems.MAINFRAME_IV.getStackForm(1),
                        PRMetaItems.NANO_NIC.getStackForm(1)
                )
                .EUt(GTValues.VA[GTValues.IV])
                .duration(20)
                .buildAndRegister();

        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(PRMetaItems.QUANTUM_NIC.getStackForm(1))
                .inputs(MetaItems.EXTREME_CIRCUIT_BOARD.getStackForm(5),
                        //MetaItems.NANO_MAINFRAME_LUV.getStackForm(1),
                        MetaItems.QUBIT_CENTRAL_PROCESSING_UNIT.getStackForm(2),
                        OreDictUnifier.get(OrePrefix.wireGtSingle, Materials.Platinum, 22)
                )
                .EUt(GTValues.VA[GTValues.IV])
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(400)
                .buildAndRegister();
        RecipeMaps.FORMING_PRESS_RECIPES.recipeBuilder()
                .outputs(QUANTUM_COMPUTER_LUV.getStackForm(1))
                .inputs(NANO_MAINFRAME_LUV.getStackForm(1),
                        PRMetaItems.QUANTUM_NIC.getStackForm(1))
                .EUt(GTValues.VA[GTValues.LuV])
                .duration(20)
                .buildAndRegister();

        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(PRMetaItems.ADVANCED_SMD_PACKAGE.getStackForm(1))
                .inputs(new ItemStack(Items.PAPER),
                        MetaItems.ADVANCED_SMD_INDUCTOR.getStackForm(2),
                        MetaItems.ADVANCED_SMD_CAPACITOR.getStackForm(7),
                        MetaItems.ADVANCED_SMD_TRANSISTOR.getStackForm(3)
                )
                .fluidInputs(Materials.Glue.getFluid(1000))
                .EUt(GTValues.VA[GTValues.IV])
                .duration(20)
                .buildAndRegister();

        RecipeMaps.CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder()
                .outputs(PRMetaItems.CRYSTAL_NIC.getStackForm(1))
                .inputs(MetaItems.ELITE_CIRCUIT_BOARD.getStackForm(5),
                        //MetaItems.QUANTUM_MAINFRAME_ZPM.getStackForm(1),
                        MetaItems.CRYSTAL_CENTRAL_PROCESSING_UNIT.getStackForm(2),
                        MetaItems.NAND_MEMORY_CHIP.getStackForm(64),
                        MetaItems.NOR_MEMORY_CHIP.getStackForm(16),
                        PRMetaItems.ADVANCED_SMD_PACKAGE.getStackForm(4),
                        OreDictUnifier.get(OrePrefix.wireGtSingle, Materials.NiobiumTitanium, 20)
                )
                //.solderMultiplier(8)
                .cleanroom(CleanroomType.CLEANROOM)
                .EUt(GTValues.VA[GTValues.IV])
                .duration(400)
                .buildAndRegister();
        RecipeMaps.FORMING_PRESS_RECIPES.recipeBuilder()
                .outputs(CRYSTAL_COMPUTER_ZPM.getStackForm(1))
                .inputs(QUANTUM_MAINFRAME_ZPM.getStackForm(1),
                        PRMetaItems.CRYSTAL_NIC.getStackForm(1))
                .EUt(GTValues.VA[GTValues.ZPM])
                .duration(20)
                .buildAndRegister();
        RecipeMaps.ASSEMBLY_LINE_RECIPES.recipeBuilder()
                .outputs(PRMetaItems.WETWARE_NIC.getStackForm(1))
                .inputs(MetaItems.WETWARE_CIRCUIT_BOARD.getStackForm(3),
                        MetaItems.NEURO_PROCESSOR.getStackForm(2),
                        //MetaItems.CRYSTAL_MAINFRAME_UV.getStackForm(1),
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
        RecipeMaps.FORMING_PRESS_RECIPES.recipeBuilder()
                .outputs(WETWARE_SUPER_COMPUTER_UV.getStackForm(1))
                .inputs(CRYSTAL_MAINFRAME_UV.getStackForm(1),
                        PRMetaItems.WETWARE_NIC.getStackForm(1))
                .EUt(GTValues.VA[GTValues.UV])
                .duration(20)
                .buildAndRegister();
        //切板，启动！
        //MV->LV->ULV
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(INTEGRATED_CIRCUIT_LV.getStackForm(2))
                .inputs(INTEGRATED_CIRCUIT_MV.getStackForm(1))
                .EUt(16)
                .duration(200)
                .buildAndRegister();
        RecipeMaps.CUTTER_RECIPES.recipeBuilder()
                .outputs(NAND_CHIP_ULV.getStackForm(1))
                .inputs(INTEGRATED_CIRCUIT_LV.getStackForm(1))
                .EUt(120)
                .duration(300)
                .buildAndRegister();
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
