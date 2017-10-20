package pl.cubesoft.smagabakery.utils;

import java.util.Collection;
import java.util.List;

import pl.cubesoft.smagabakery.dialog.AlertDialogFragment;

/**
 * Created by CUBESOFT on 20.10.2017.
 */

public class CollectionUtils {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isEmpty(T[] items) {
        return items == null || items.length == 0;
    }

    public static boolean[] toBooleanArray(List<Boolean> items) {
        boolean[] result = new boolean[items.size()];
        for (int i=0; i<items.size(); ++i) {
            result[i] = items.get(i);
        }
        return result;
    }
}
