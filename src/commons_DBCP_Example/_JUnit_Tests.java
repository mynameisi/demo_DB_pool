package commons_DBCP_Example;

import static org.junit.Assert.*;

import org.junit.Test;

public class _JUnit_Tests {

	@Test
	public void testRegular() {
		assertTrue(T.regular());
	}
	
	@Test
	public void dbcp_pool_Threading() {
		assertTrue(T.dbcp_pool_Threading());
	}
	
	@Test
	public void bonecp_pool_Threading() {
		assertTrue(T.bonecp_pool_Threading());
	}
	
	@Test
	public void bonecp_pool_non_Threading() {
		assertTrue(T.bonecp_pool_non_Threading());
	}
	
	@Test
	public void dbcp_pool_non_Threading() {
		assertTrue(T.dbcp_pool_non_Threading());
	}

}
