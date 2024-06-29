package bruce.projectreflection.items;

import bruce.projectreflection.PRConstants;
import bruce.projectreflection.PRLabs;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;

@SuppressWarnings("rawtypes")
public class PRMetaItems extends StandardMetaItem {
    public static boolean registered = false;
    public static PRMetaItems INSTANCE = new PRMetaItems();

    @Override
    public void registerSubItems() {
        if (!registered) {
            registered = true;
            super.registerSubItems();
            PRLabs.logger.info("projectreflection registersubitems");
        }

    }

    private PRMetaItems() {
        setRegistryName(PRConstants.modid, "pr_meta_item");
        setCreativeTab(PRConstants.tab);
    }
}
