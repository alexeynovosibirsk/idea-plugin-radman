package com.nazarov.util;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.NlsSafe;

public class ValidatorInput implements InputValidator {



    public ValidatorInput() {


    }

    @Override
    public boolean checkInput(@NlsSafe String inputString) {

            return true;

    }

    @Override
    public boolean canClose(@NlsSafe String inputString) {
        return false;
    }


}
