package com.nazarov.radman.validate;

import com.intellij.openapi.util.NlsSafe;

public class CharacterValidator implements ValidatorInput{

    @Override
    public boolean checkInput(@NlsSafe String inputString) {
        int stringSize = inputString.length();
        if (stringSize == 0) {
            return false;
        }
        for (int i = 0; i < stringSize; ++i) {
            if (Character.isDigit(inputString.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canClose(@NlsSafe String inputString) {
        if (checkInput(inputString)) {
            return true;
        } else {
            return false;
        }
    }
}
