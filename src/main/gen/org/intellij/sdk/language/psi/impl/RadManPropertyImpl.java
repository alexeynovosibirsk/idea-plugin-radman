// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.nazarov.radman.color.language.RadManNamedElementImpl;
import org.intellij.sdk.language.psi.*;
import com.nazarov.radman.color.language.RadManPsiUtil;
import com.intellij.navigation.ItemPresentation;

public class RadManPropertyImpl extends RadManNamedElementImpl implements RadManProperty {

  public RadManPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull RadManVisitor visitor) {
    visitor.visitProperty(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof RadManVisitor) accept((RadManVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public String getKey() {
    return RadManPsiUtil.getKey(this);
  }

  @Override
  public String getValue() {
    return RadManPsiUtil.getValue(this);
  }

  @Override
  public String getName() {
    return RadManPsiUtil.getName(this);
  }

  @Override
  public PsiElement setName(String newName) {
    return RadManPsiUtil.setName(this, newName);
  }

  @Override
  public PsiElement getNameIdentifier() {
    return RadManPsiUtil.getNameIdentifier(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return RadManPsiUtil.getPresentation(this);
  }

}
