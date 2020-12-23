package com.kakuom.finaltipping.views;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class StringSetView {
    @NotBlank
    private Set<String> StringSet;

    public Set<String> getStringSet() {
        return StringSet;
    }

    public void setStringSet(Set<String> stringSet) {
        StringSet = stringSet;
    }
}
