package com.nazarov.radman.color.language;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.util.Map;

public class RadManColorSettingsPage implements ColorSettingsPage {

  private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
          new AttributesDescriptor("Stream URL", RadManSyntaxHighlighter.KEY),
          new AttributesDescriptor("Etcetera", RadManSyntaxHighlighter.VALUE),
          new AttributesDescriptor("Comment", RadManSyntaxHighlighter.COMMENT)
  };

  @Nullable
  @Override
  public Icon getIcon() {
    return RadManIcon.FILE;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new RadManSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return "# This is an example of colors for radman \".rad\" files.\n" +
            "http://streamRadio/listen.mp3  | And then if present: RadioName | https://radio.home.page | US | Lang: | Votes:5\n" +
            "https://streamRadio/lsten.aac  | And then if present: RadioName | http://radio.home.page | RU | Lang: Russian | Votes:100\n" +
            "# Some comment...";
  }

  @Nullable
  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return null;
  }

  @Override
  public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
    return DESCRIPTORS;
  }

  @Override
  public ColorDescriptor @NotNull [] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "RadMan";
  }

}
