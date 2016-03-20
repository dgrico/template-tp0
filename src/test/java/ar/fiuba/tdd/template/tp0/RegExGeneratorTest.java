package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private boolean validate(String regEx, int numberOfResults) {
        RegExGenerator generator = new RegExGenerator(numberOfResults);
        List<String> results = generator.generate(regEx, numberOfResults);
        System.out.println(results);
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");

        return results
                .stream()
                .reduce(true,
                    (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                    (item1, item2) -> item1 && item2);
    }

    @Test
    public void testAnyCharacter() {
        assertTrue(validate(".", 1));
    }

    @Test
    public void testMultipleCharacters() {
        assertTrue(validate("...", 3));
    }

    @Test
    public void testLiteral() {
        assertTrue(validate("\\@", 1));
    }

    @Test
    public void testLiteralDotCharacter() {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testZeroOrOneCharacter() {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testZeroOrManyCharacter() {
        assertTrue(validate("\\@.h*", 1));
    }

    @Test
    public void testExplamplePDF() {
        assertTrue(validate("..+[ab]*d?c", 1));
    }

    @Test
    public void testExplamplePDFPositionObligatory() {
        assertTrue(validate("..+[ab]+d+c", 1));
    }

    @Test
    public void testExplamplePDFAsterisk() {
        assertTrue(validate("..*[ab]*d*c", 1));
    }

    @Test
    public void testCharacterSet() {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifierMore() {
        assertTrue(validate("[abc]+", 1));
    }

    @Test
    public void testCharacterSetWithQuantifierAsterisk() {
        assertTrue(validate("[abc]*", 1));
    }

    @Test
    public void testCharacterSetWithQuantifierQuestion() {
        assertTrue(validate("[abc]?", 1));
    }

    @Test
    public void testNumerics() {
        assertTrue(validate("1*2*3*4*5*6*7*8*9*0*", 1));
    }

    @Test
    public void testOneNumber() {
        assertTrue(validate("[0123456789]", 1));
    }

    @Test
    public void testVariarity() {
        assertTrue(validate("[ABCDEFGHIJKLNMÑOPQRSTUVWXYZ][0123456789][abcdefghijklmnñopqrstuvwxyz]", 10));
    }

    @Test
    public void testAnyone() {
        assertTrue(validate(".*.?.+", 1));
    }

    @Test
    public void testPatent() {
        assertTrue(validate("[ABCDEFGHI][JKLNMOPQ][RSTUVWXYZ] [0123456789][0123456789][0123456789]", 100));
    }

    @Test
    public void testDirectionMail() {
        assertTrue(validate("[abcdefghijklmnñopqrstuvwxyz]+@[abcdefghijklmnñopqrstuvwxyz]+\\.com", 1));
    }

    @Test
    public void testBar() {
        assertTrue(validate("\\. es un punto", 1));
    }

    @Test
    public void testSpecialCharacter() {
        assertTrue(validate("\\?\\+\\* son especiales", 1));
    }
/*
    @Test
    public void testSpecialCharactera() {
        assertTrue(validate("\\a", 1));
    }

    @Test
    public void testUnsupported() {
        assertFalse(validate("\\d", 1));
    }

    @Test
    public void testUnclosedGorup() {
        assertTrue(validate("[[]", 1));
    }
*/
}
