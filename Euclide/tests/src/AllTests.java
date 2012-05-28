import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * File: 	AllTests.java
 * Project: Euclide
 * 
 * Distributed under the LGPL License.
 *
 * Created: 23 févr. 10
 */

/**
 * @author dlegland
 *
 */
@RunWith(Suite.class)
@SuiteClasses(value={
		dynamic.AllTests.class,
		io.AllTests.class,
		gui.AllTests.class,
		model.AllTests.class,
})
public class AllTests {
}
