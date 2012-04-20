package miniRSA;

import static org.junit.Assert.*;

import org.junit.Test;

public class MiniRSATest {
	MiniRSA rsa = new MiniRSA();
	
	@Test
	public void testGCD() {
		assertTrue(rsa.GCD(100, 10) == 10);
		assertTrue(rsa.GCD(10, 100) == 10);
		assertTrue(rsa.GCD(100, 13) == 1);
		assertTrue(rsa.GCD(100, 100) == 100);
		assertTrue(rsa.GCD(13, 39) == 13);
		assertTrue(rsa.GCD(120, 35) == 5);
		assertTrue(rsa.GCD(1001, 77) == 77);
		assertTrue(rsa.GCD(1001, 33) == 11);
	}

	@Test
	public void testMod_Inverse() {
		assertTrue(rsa.mod_inverse(3, 17) == 6);
		assertTrue(rsa.mod_inverse(55, 123) == 85);
		assertTrue(rsa.mod_inverse(45, 223) == 114);
		assertTrue(rsa.mod_inverse(2342, 92830457) == 75588250);
		assertTrue(rsa.mod_inverse(3, 30) == 0);
	}
	
	@Test
	public void testEndencrypt() {
	    assertEquals(rsa.endecrypt(2, 0, 55), 1);
	    assertEquals(rsa.endecrypt(2, 4, 1000), 16);
	    assertEquals(rsa.endecrypt(2, 6, 5), 4);
	    assertEquals(rsa.endecrypt(78, 45, 37), 36);
	    assertEquals(rsa.endecrypt(89, 1232, 4623), 1);
	    assertEquals(rsa.endecrypt(254, 234, 123), 4);
	    assertEquals(rsa.endecrypt(2349723, 423424, 12345), 696);
		assertEquals(rsa.endecrypt(72, 451, 2623), 1148);
	}
	
	@Test
	public void testCaluculateC(){
		int[] array = rsa.calculateC(2623);
		assertEquals(61, array[0]);
		assertEquals(43, array[1]);
	}
}
