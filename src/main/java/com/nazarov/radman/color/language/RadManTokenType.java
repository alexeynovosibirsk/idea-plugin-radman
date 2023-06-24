package com.nazarov.radman.color.language;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class RadManTokenType extends IElementType {

  public RadManTokenType(@NotNull @NonNls String debugName) {
    super(debugName, RadManLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "RadManTokenType." + super.toString();
  }

}
