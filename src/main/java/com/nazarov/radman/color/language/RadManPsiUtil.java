package com.nazarov.radman.color.language;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.intellij.sdk.language.psi.RadManProperty;
import org.intellij.sdk.language.psi.RadManTypes;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RadManPsiUtil {

  public static String getKey(RadManProperty element) {
    ASTNode keyNode = element.getNode().findChildByType(RadManTypes.KEY);
    if (keyNode != null) {
      // IMPORTANT: Convert embedded escaped spaces to simple spaces
      return keyNode.getText().replaceAll("\\\\ ", " ");
    } else {
      return null;
    }
  }

  public static String getValue(RadManProperty element) {
    ASTNode valueNode = element.getNode().findChildByType(RadManTypes.VALUE);
    if (valueNode != null) {
      return valueNode.getText();
    } else {
      return null;
    }
  }

  public static String getName(RadManProperty element) {
    return getKey(element);
  }

  public static PsiElement setName(RadManProperty element, String newName) {
    ASTNode keyNode = element.getNode().findChildByType(RadManTypes.KEY);
    if (keyNode != null) {
      RadManProperty property = RadManElementFactory.createProperty(element.getProject(), newName);
      ASTNode newKeyNode = property.getFirstChild().getNode();
      element.getNode().replaceChild(keyNode, newKeyNode);
    }
    return element;
  }

  public static PsiElement getNameIdentifier(RadManProperty element) {
    ASTNode keyNode = element.getNode().findChildByType(RadManTypes.KEY);
    if (keyNode != null) {
      return keyNode.getPsi();
    } else {
      return null;
    }
  }

  public static ItemPresentation getPresentation(final RadManProperty element) {
    return new ItemPresentation() {
      @Nullable
      @Override
      public String getPresentableText() {
        return element.getKey();
      }

      @Nullable
      @Override
      public String getLocationString() {
        PsiFile containingFile = element.getContainingFile();
        return containingFile == null ? null : containingFile.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return element.getIcon(0);
      }
    };
  }

}
