package parkingfield;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CarTest {
	Car car=new Car("HA12345",120);

	@Test
	public void testGetPlateNo() {
		assertEquals("HA12345", car.getPlateNo());
	}

	@Test
	public void testGetWidth() {
		assertEquals(120, car.getWidth());
	}

}
