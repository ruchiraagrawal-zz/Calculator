package test.java;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import calculator.Calculator;
import org.junit.Test;

public class CalculateTest {

    public Calculator calculator = new Calculator();
    
    @Test
    public void testBracketOperation() throws Exception {
        BigDecimal result = calculator.calculate("2*3/3+4*(3*2+21)/3*7");
        assertEquals(new BigDecimal("254"), result);
    }
    
    @Test
    public void testOrderOperation() throws Exception {
        assertEquals(new BigDecimal("184473634"),calculator.calculate("2*3/3+4*(3*2/2+21)/3*7^8"));
    }
    
    @Test
    public void testAdditionOperation() throws Exception {
        assertEquals(new BigDecimal("5"), calculator.calculate("1+4"));
    }
    
    @Test
    public void testDivisionOperation() throws Exception {
        assertEquals(new BigDecimal("2"), calculator.calculate("5/2"));
    }
    
    @Test
    public void testBODMASOperation() throws Exception {
        calculator.calculate("5*4+2");
    }
    
    @Test(expected=java.lang.UnsupportedOperationException.class)
    public void testDivideBy0Operation() throws Exception {
        calculator.calculate("1/0");
    }
    
}
