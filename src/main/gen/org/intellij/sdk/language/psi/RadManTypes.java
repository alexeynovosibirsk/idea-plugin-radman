// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.nazarov.radman.color.language.RadManElementType;
import com.nazarov.radman.color.language.RadManTokenType;
import org.intellij.sdk.language.psi.impl.RadManPropertyImpl;

public interface RadManTypes {

  IElementType PROPERTY = new RadManElementType("PROPERTY");

  IElementType COMMENT = new RadManTokenType("COMMENT");
  IElementType CRLF = new RadManTokenType("CRLF");
  IElementType KEY = new RadManTokenType("KEY");
  IElementType SEPARATOR = new RadManTokenType("SEPARATOR");
  IElementType VALUE = new RadManTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY) {
        return new RadManPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
