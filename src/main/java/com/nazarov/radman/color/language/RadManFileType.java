package com.nazarov.radman.color.language;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RadManFileType extends LanguageFileType {

    public static final RadManFileType INSTANCE = new RadManFileType();

    private RadManFileType() {
        super(RadManLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "RadMan File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "RadMan";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "rad";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return RadManIcon.FILE;
    }

}
