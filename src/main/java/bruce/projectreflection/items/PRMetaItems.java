package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.ProjectReflection;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;

@SuppressWarnings("rawtypes")
public class PRMetaItems extends StandardMetaItem {
    public static PRMetaItems INSTANCE = new PRMetaItems();
    public static MetaItem.MetaValueItem SMD_COMPONENT_PACKAGE;
    public static MetaItem.MetaValueItem INTEGRATED_NIC;
    public static MetaItem.MetaValueItem PROCESSOR_NIC;
    public static MetaItem.MetaValueItem NANO_NIC;
    public static MetaItem.MetaValueItem QUANTUM_NIC;
    public static MetaItem.MetaValueItem CRYSTAL_NIC;
    public static MetaItem.MetaValueItem WETWARE_NIC;
    public static MetaItem.MetaValueItem ADVANCED_SMD_PACKAGE;

    @Override
    public void registerSubItems() {
        super.registerSubItems();
        ProjectReflection.logger.info("projectreflection registersubitems");
        SMD_COMPONENT_PACKAGE = addItem(0, "smd_component_package");
        INTEGRATED_NIC = addItem(1, "nic.integrated");
        PROCESSOR_NIC = addItem(2, "nic.processor");
        NANO_NIC = addItem(3, "nic.nano");
        QUANTUM_NIC = addItem(4, "nic.quantum");
        CRYSTAL_NIC = addItem(5, "nic.crystal");
        WETWARE_NIC = addItem(6, "nic.wetware");
        ADVANCED_SMD_PACKAGE = addItem(10, "smd_component_package.advanced");
    }

    private PRMetaItems() {
        setRegistryName("pr_meta_item");
        setCreativeTab(PRConstants.tab);
    }
}
