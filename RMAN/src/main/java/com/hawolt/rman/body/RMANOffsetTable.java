package com.hawolt.rman.body;

import com.hawolt.generic.util.RandomAccessReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created: 15/08/2023 13:14
 * Author: Twitter @hawolt
 **/

public class RMANOffsetTable {
    public static List<Map<String, Object>> parseOffsetTable(
            RandomAccessReader reader,
            Map<String, String> fieldsToParse,
            BiFunction<RandomAccessReader, Map<String, String>, Map<String, Object>> parseFunction
    ) {
        List<Map<String, Object>> offsetEntries = new ArrayList<>();
        int count = reader.readInt();
        for (int i = 0; i < count; i++) {
            int current = reader.position();
            int offset = reader.readInt();
            reader.seek(current + offset);
            offsetEntries.add(parseFunction.apply(reader, fieldsToParse));
            reader.seek(current + 4);
        }
        return offsetEntries;
    }

}