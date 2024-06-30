package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import gregtech.api.items.metaitem.StandardMetaItem;

@SuppressWarnings("rawtypes")
public class PRMetaItems extends StandardMetaItem {
    //public static boolean registered = false;
    public static PRMetaItems INSTANCE = new PRMetaItems();

    @Override
    public void registerSubItems() {
    }

    private PRMetaItems() {
        setRegistryName(PRConstants.modid, "pr_meta_item");
        setCreativeTab(PRConstants.tab);
    }
}
