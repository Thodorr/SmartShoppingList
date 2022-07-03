package core;

import androidx.room.TypeConverter;

import math.Vector2;

public class Vec2TypeConverter {
    private Vec2TypeConverter() {}

    @TypeConverter
    public static Vector2 fromFloatArray(String text) {
        String[] parts = text.split("x");
        return new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]));
    }

    @TypeConverter
    public static String toFloatArray(Vector2 vec) {
        return vec.x + "x" + vec.y;
    }
}
