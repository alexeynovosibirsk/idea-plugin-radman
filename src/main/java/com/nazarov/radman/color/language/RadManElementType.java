package com.nazarov.radman.color.language;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class RadManElementType extends IElementType {

  public RadManElementType(@NotNull @NonNls String debugName) {
    super(debugName, RadManLanguage.INSTANCE);
  }

}
