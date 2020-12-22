package com.kakuom.finaltipping.enums;

public enum Comp {
    NRL("nrl"), AFL("afl");
    private String comp;

    Comp(String comp) {
        this.comp = comp;
    }

    public String getComp() {
        return comp;
    }

    public static Comp getFromDb(String alias) {
        switch (alias) {
            case "nrl":
                return Comp.NRL;
            case "afl":
                return Comp.AFL;
            default:
                throw new IllegalArgumentException("not in records");
        }
    }
}
