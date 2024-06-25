package com.nazarov.radman.validate;

public class IntegerValidator implements ValidatorInput{

    @Override
    public boolean checkInput(String inputString) {
        if(isNumeric(inputString)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canClose(String inputString) {
        if (checkInput(inputString)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.isEmpty();
    }

}


