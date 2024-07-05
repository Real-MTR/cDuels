package xyz.atomdev.cduels.util;

public enum ItemDataColor {
    WHITE((short) 0),
    GOLD((short) 1),
    MAGENTA((short) 2),
    AQUA((short) 3),
    YELLOW((short) 4),
    GREEN((short) 5),
    LIGHT_PURPLE((short) 6),
    DARK_GRAY((short) 7),
    GRAY((short) 8),
    DARK_AQUA((short) 9),
    DARK_PURPLE((short) 10),
    DARK_BLUE((short) 11),
    BLUE((short) 11),
    BROWN((short) 12),
    DARK_GREEN((short) 13),
    DARK_RED((short) 14),
    RED((short) 14),
    BLACK((short) 15);
    
    private final short value;

    ItemDataColor(short value) {
        this.value = value;
    }

    public short getValue() {
        return this.value;
    }

    public static ItemDataColor getByValue(short value) {
        ItemDataColor[] values;
        for (ItemDataColor color : values()) {
            if (value == color.value) {
                return color;
            }
        }
        return null;
    }
}
