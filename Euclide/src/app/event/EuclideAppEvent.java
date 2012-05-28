/* File EuclideAppEvent.java 
 *
 * Project : Euclide
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * Created 12 janv. 08
 */

package app.event;

import model.EuclideDoc;
import app.EuclideApp;

/**
 * An event indicating a modification was made in the application
 * (doc changed, global option changed, doc closed...)
 * @author dlegland
 *
 */
public abstract class EuclideAppEvent {

	public final static int DOCUMENT_ADDED = 1;
	
	public final static int DOCUMENT_REMOVED = 2;
	
	public final static int CURRENTDOC_CHANGED = 3;
	
	public abstract int getState();
	
	/**
	 * Returns the application that launched this event.
	 */
	public abstract EuclideApp getAppli();

	/**
	 * Returns the document concerned by this modification, or null if the modif
	 * does not concern a document.
	 */
	public abstract EuclideDoc getDoc();
}
