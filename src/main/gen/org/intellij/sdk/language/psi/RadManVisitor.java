// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.nazarov.radman.color.language.RadManNamedElement;
import org.jetbrains.annotations.NotNull;

public class RadManVisitor extends PsiElementVisitor {

  public void visitProperty(@NotNull RadManProperty o) {
    visitNamedElement(o);
  }

  public void visitNamedElement(@NotNull RadManNamedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
