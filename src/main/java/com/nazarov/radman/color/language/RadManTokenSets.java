package com.nazarov.radman.color.language;

import com.intellij.psi.tree.TokenSet;
import org.intellij.sdk.language.psi.RadManTypes;

public interface RadManTokenSets {

  TokenSet IDENTIFIERS = TokenSet.create(RadManTypes.KEY);

  TokenSet COMMENTS = TokenSet.create(RadManTypes.COMMENT);

}
