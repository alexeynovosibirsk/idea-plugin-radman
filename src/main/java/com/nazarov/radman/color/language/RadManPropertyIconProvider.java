package com.nazarov.radman.color.language;

import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import org.intellij.sdk.language.psi.RadManProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RadManPropertyIconProvider extends IconProvider {

  @Override
  public @Nullable Icon getIcon(@NotNull PsiElement element, int flags) {
    return element instanceof RadManProperty ? RadManIcon.FILE : null;
  }

}
