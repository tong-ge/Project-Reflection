package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;

public class PRMetaItems extends StandardMetaItem {
    //public static boolean registered = false;
    public static PRMetaItems INSTANCE = new PRMetaItems();
    public static MetaItem<?>.MetaValueItem BLUEPRINT;
    public static MetaItem<?>.MetaValueItem CLOCKWORK_SPRING;
    @Override
    public void registerSubItems() {
        BLUEPRINT = addItem(1, "blueprint");
        CLOCKWORK_SPRING = addItem(2, "clockwork_spring").addComponents();
    }

    private PRMetaItems() {
        setRegistryName(PRConstants.modid, "pr_meta_item");
        setCreativeTab(PRConstants.tab);
    }
}
