// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi;

import com.intellij.psi.PsiElement;
import com.nazarov.radman.color.language.RadManNamedElement;
import com.intellij.navigation.ItemPresentation;

public interface RadManProperty extends RadManNamedElement {

  String getKey();

  String getValue();

  String getName();

  PsiElement setName(String newName);

  PsiElement getNameIdentifier();

  ItemPresentation getPresentation();

}
