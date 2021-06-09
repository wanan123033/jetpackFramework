package com.jetpackframework.arouter;

import android.net.Uri;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by CaoDongping on 6/1/16.
 */
public class UriCompact {

    /**
     * Call Uri#getQueryParameterNames() below api 11.
     *
     * @param uriString Uri
     * @return Set
     */
    public static Set<String> getQueryParameterNames(String uriString) {
        Uri uri = Uri.parse(uriString);
        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            names.add(Uri.decode(name));
            start = end + 1;
        } while (start < query.length());

        return Collections.unmodifiableSet(names);
    }
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);

    }
}
