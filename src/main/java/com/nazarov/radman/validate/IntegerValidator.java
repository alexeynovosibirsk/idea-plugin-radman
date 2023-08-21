package com.nazarov.radman.validate;

import com.intellij.openapi.util.NlsSafe;
import org.apache.commons.lang3.StringUtils;

public class IntegerValidator implements ValidatorInput{
    @Override
    public boolean checkInput(@NlsSafe String inputString) {

        if(StringUtils.isNumeric(inputString)) {

            return true;
        } else {

            return false;
        }
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
