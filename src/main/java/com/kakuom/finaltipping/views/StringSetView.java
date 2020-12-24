package com.kakuom.finaltipping.views;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class StringSetView {
    @NotEmpty
    private Set<String> stringSet;

    public StringSetView() {
    }

    public StringSetView(@NotEmpty Set<String> stringSet) {
        this.stringSet = stringSet;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }
}
