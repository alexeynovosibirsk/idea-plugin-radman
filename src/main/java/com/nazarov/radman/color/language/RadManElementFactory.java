package com.nazarov.radman.color.language;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import org.intellij.sdk.language.psi.RadManProperty;

public class RadManElementFactory {

  public static RadManProperty createProperty(Project project, String name) {
    final RadManFile file = createFile(project, name);
    return (RadManProperty) file.getFirstChild();
  }

  public static RadManFile createFile(Project project, String text) {
    String name = "dummy.simple";
    return (RadManFile) PsiFileFactory.getInstance(project).createFileFromText(name, RadManFileType.INSTANCE, text);
  }

  public static RadManProperty createProperty(Project project, String name, String value) {
    final RadManFile file = createFile(project, name + " = " + value);
    return (RadManProperty) file.getFirstChild();
  }

  public static PsiElement createCRLF(Project project) {
    final RadManFile file = createFile(project, "\n");
    return file.getFirstChild();
  }

}
