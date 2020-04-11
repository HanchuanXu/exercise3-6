package parkingfield;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LotTest {
	
	Lot lot = new Lot(1,150);
	@Test
	public void testGetLotNumber() {
		assertEquals(1, lot.getNumber());
	}

	@Test
	public void testGetWidth() {
		assertEquals(150, lot.getWidth());
	}

}
