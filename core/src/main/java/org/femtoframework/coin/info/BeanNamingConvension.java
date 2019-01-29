package org.femtoframework.coin.info;

/**
 * Bean Naming Conversion
 */
public interface BeanNamingConvension {

    /**
     * @return true iff s is a syntactically valid Java identifier
     *         without regard to Java reserved words.
     */
    static boolean isValidJavaIdentifier(String s)
    {
        if (s == null) {
            return false;
        }
        final int len = s.length();
        if (len == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
            return false;
        }
        for (int i = 1; i < len; i++) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true iff s is a syntactically valid Java type name,
     *         including multidimensional arrays, and without regard to
     *         Java reserved words.
     */
    static boolean isValidJavaTypeName(String s)
    {
        if (s == null) {
            return false;
        }
        final int len = s.length();
        // Arrays begin with a series of '['...
        int i = 0;
        while (i < len) {
            if (s.charAt(i) == '[') {
                i++;
            }
            else {
                break;
            }
        }
        // If i==len, we have only '[' characters => not a valid type.
        if (i == len) {
            return false;
        }

        // If i=0, the type doesn't start with '[', so it is not an array.
        // => check that it is a valid Java type identifier
        //
        if (i == 0) {
            return isValidJavaTypeIdentifier(s);
        }

        // We have an array.
        //
        final char c = s.charAt(i);
        switch (c) {
            case 'L':
                // Check that we have stg like "[[[[Lxxx.yyyy.zzzz;"
                if ((i < len - 1) && s.charAt(len - 1) == ';') {
                    return isValidJavaTypeIdentifier(s.substring(i, len - 1));
                }
                else {
                    return false;
                }
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return i == len - 1;
            default:
                return false;
        }
    }

    /**
     * @return true iff s is a syntactically valid Java type identifier,
     *         excluding arrays, and without regard to Java reserved words.
     */
    static boolean isValidJavaTypeIdentifier(String s)
    {
        if (s == null) {
            return false;
        }
        final int len = s.length();
        boolean needLetter = true;
        for (int i = 0; i < len; i++) {
            final char c = s.charAt(i);
            if (needLetter) {
                if (!Character.isJavaIdentifierStart(c)) {
                    return false;
                }
                needLetter = false;
            }
            else if (c == '.') {
                needLetter = true;
            }
            else if (!Character.isJavaIdentifierPart(c)) {
                return false;
            }
        }
        return !needLetter;
    }

    static void mustBeValidMBeanTypeName(String s)
    {
        if (!isValidJavaTypeIdentifier(s)) {
            throw new IllegalArgumentException("BeanInfo: Not a valid Java Bean type name: " + s);
        }
    }

    static void mustBeValidJavaTypeName(String s)
    {
        if (!isValidJavaTypeName(s)) {
            throw new IllegalArgumentException("BeanInfo: Not a valid Java type name: " + s);
        }
    }

    static void mustBeValidJavaIdentifier(String s)
    {
        if (!isValidJavaIdentifier(s)) {
            throw new IllegalArgumentException("BeanInfo: Not a valid Java identifier: " + s);
        }
    }

}
