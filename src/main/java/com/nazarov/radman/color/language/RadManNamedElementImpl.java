package com.nazarov.radman.color.language;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class RadManNamedElementImpl extends ASTWrapperPsiElement implements RadManNamedElement {

  public RadManNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

}
