package com.nazarov.radman.color.language;

import com.intellij.lang.Language;

public class RadManLanguage  extends Language {

        public static final RadManLanguage INSTANCE = new RadManLanguage();

        private RadManLanguage() {
            super("RadMan");
        }
}
