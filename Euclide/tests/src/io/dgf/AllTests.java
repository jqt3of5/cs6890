/**
 * File: 	AllTests.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 23 f�vr. 10
 */
package io.dgf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author dlegland
 *
 */
@RunWith(Suite.class)
@SuiteClasses(value={
		EuclideDGFReaderTest.class,
})
public class AllTests {
}
