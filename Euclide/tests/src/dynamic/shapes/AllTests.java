/**
 * File: 	AllTests.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 23 févr. 10
 */
package dynamic.shapes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author dlegland
 *
 */
@RunWith(Suite.class)
@SuiteClasses(value={
		StraightLine2Points2DTest.class,
		ShapeWrapper2DTest.class,
})
public class AllTests {
}
