package com.nazarov.radman.color.language;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class RadManFile extends PsiFileBase {

  public RadManFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, RadManLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return RadManFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "RadMan File";
  }

}
