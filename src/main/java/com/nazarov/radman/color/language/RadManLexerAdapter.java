package com.nazarov.radman.color.language;

import com.intellij.lexer.FlexAdapter;
import org.intellij.sdk.language.RadManLexer;

public class RadManLexerAdapter extends FlexAdapter {

  public RadManLexerAdapter() {
    super(new RadManLexer(null));
  }

}
