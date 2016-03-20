package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
//import java.util.regex.Pattern;
//import java.util.regex.PatternSyntaxException;

public class RegExGenerator {

    private int maxLength = 10; // longitud maxima de la expresion regular
    private int total = maxLength;
    private int many = 5; // longitud maxima de cuantificadores, el "muchos"
    private int securedCounter = 0; // cantidad de posiciones minimas requeridas
    private ArrayList<String> lstChars = new ArrayList<>();
    private ArrayList<String> lstQuantifiers = new ArrayList<>();
    private String partChain = "";
    int indexRE; // Indice para recorrer la expresion regular

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    private boolean isQuantifier(Character character) {
        if (character == '?' || character == '*' || character == '+') {
            if (character == '+') {
                securedCounter++;
            }
            return true;
        } else {
            return false;
        }
    }

    private void putListQuantifier(String regEx) {
        if (indexRE < regEx.length()) {
            if (isQuantifier((regEx.charAt(indexRE)))) {
                lstQuantifiers.add(Character.toString(regEx.charAt(indexRE)));
                indexRE++;
            } else {
                securedCounter++;
                lstQuantifiers.add("");
            }
        } else {
            lstQuantifiers.add("");
            securedCounter++;
        }
    }

    // Obtiene los valores de los conjuntos
    private String getSet(String regEx) {
        String chain = "";
        char charRE;
        while (regEx.charAt(indexRE) != ']') {
            charRE = regEx.charAt(indexRE);
            chain = chain.concat(String.valueOf(charRE));
            indexRE++;
        }
        return chain;
    }

    private void processParser(String regEx) {
        indexRE = 0;
        while (indexRE <= regEx.length() - 1) {
            switch (regEx.charAt(indexRE)) {
                case '.':
                    lstChars.add(Character.toString(regEx.charAt(indexRE)));
                    break;
                case '\\':
                    indexRE++;
                    lstChars.add("\\" + Character.toString(regEx.charAt(indexRE)));
                    break;
                case '[':
                    indexRE++;
                    lstChars.add(getSet(regEx));
                    break;
                default: // es un literal, un caracter especifico
                    lstChars.add(Character.toString(regEx.charAt(indexRE)));
                    break;
            }
            indexRE++;
            putListQuantifier(regEx);
        }
    }

    private int evaluateQuantifier(String quantifier, int maxRepeat) {
        Random numRan = new Random();
        switch (quantifier) {
            case "?":
                maxRepeat = numRan.nextInt(2);
                total = total - maxRepeat;
                break;
            case "+":
                maxRepeat = numRan.nextInt(maxRepeat) + 1;
                total = total - maxRepeat + 1;
                break;
            case "*":
                maxRepeat = numRan.nextInt(maxRepeat + 1);
                total = total - maxRepeat;
                break;
            default:
                maxRepeat = 1;
                break;
        }
        return maxRepeat;
    }

    private int getMaxRepeat(String quantifier) {
        int maxRepeat;

        if (quantifier.compareTo("") == 0) {
            maxRepeat = 1;
        } else {
            if (many < total) {
                maxRepeat = many;
            } else {
                maxRepeat = total;
            }
            maxRepeat = evaluateQuantifier(quantifier, maxRepeat);
        }
        return maxRepeat;
    }

    private String getMaxStingCharacter(String possibleChar, int maxRepeat) {
        String retS = "";
        String ascii;
        int asciiValue;
        int positionChar;
        int indRep = 1;
        Random numRan = new Random();

        while (indRep <= maxRepeat) {
            if (possibleChar.compareTo(".") == 0) { // es cualquier caracter ascii
                asciiValue = numRan.nextInt(255) + 1;
                ascii = String.valueOf(Character.toChars(asciiValue));
            } else {
                if (possibleChar.length() == 1) { // es un literal
                    ascii = possibleChar;
                } else { // es un conjunto de caracteres
                    positionChar = numRan.nextInt(possibleChar.length());
                    ascii = Character.toString(possibleChar.charAt(positionChar));
                }
            }
            retS = retS.concat(ascii);
            indRep++;
        }
        return retS;
    }

    private String getMaxString(String possibleChar, int maxRepeat) {
        String ascii;
        String retS = "";

        if (possibleChar.length() == 2) { // es un escape
            ascii = Character.toString(possibleChar.charAt(possibleChar.length() - 1));
            retS = retS.concat(ascii);
        } else {
            retS = getMaxStingCharacter(possibleChar, maxRepeat);
/*
            while (indRep <= maxRepeat) {
                if (possibleChar.compareTo(".") == 0) { // es cualquier caracter ascii
                    asciiValue = numRan.nextInt(255) + 1;
                    ascii = String.valueOf(Character.toChars(asciiValue));
                } else {
                    if (possibleChar.length() == 1) { // es un literal
                        ascii = possibleChar;
                    } else { // es un conjunto de caracteres
                        positionChar = numRan.nextInt(possibleChar.length());
                        ascii = Character.toString(possibleChar.charAt(positionChar));
                    }
                }
                retS = retS.concat(ascii);
                indRep++;
            }
*/
        }
        return  retS;
    }

    private String getChain(String possibleChar, String quantifier) {
        int maxRepeat;
        String retS;

        maxRepeat = getMaxRepeat(quantifier);
        retS = getMaxString(possibleChar, maxRepeat);
        return retS;
    }

    public List<String> generate(String regEx, int numberOfResults) {
/*
        try {
            Pattern pattern = Pattern.compile("^" + regEx + "$");
            //System.out.println("Pattern created: "+pattern.pattern());
        } catch (PatternSyntaxException errorExpression) {
            System.out.println("This string could not compile: " + errorExpression.getPattern());
            System.out.println(errorExpression.getMessage());
            //System.exit(0);
        }
*/
        return new ArrayList<String>() {
            {
                processParser(regEx);
                total = total - securedCounter;

                for (int i = 1; i <= numberOfResults; i++) {
                    partChain = "";
                    Iterator<String> charsIterator = lstChars.iterator();
                    Iterator<String> quantityIterator = lstQuantifiers.iterator();
                    while (charsIterator.hasNext()) {
                        partChain = partChain + getChain(charsIterator.next(), quantityIterator.next());
                    }
                    add(partChain);
                }
            }
        };
    }
}