/*-
 * +======================================================================+
 * Iono Pi Java library
 * ---
 * Copyright (C) 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.libs.iono_pi;

import cc.sferalabs.libs.iono_pi.jni.IonoPiJNI;
import cc.sferalabs.libs.iono_pi.jni.IonoPiJNIConstants;

/**
 *
 * @author Giampiero Baggiani
 *
 */
public class Led {

	Led() {
	}

	/**
	 * @param on
	 */
	public void set(boolean on) {
		IonoPiJNI.ionoPiDigitalWrite(IonoPiJNIConstants.LED,
				on ? IonoPiJNIConstants.ON : IonoPiJNIConstants.OFF);
	}

	/**
	 * 
	 */
	public boolean isOn() {
		return IonoPiJNI.ionoPiDigitalRead(IonoPiJNIConstants.LED) == IonoPiJNIConstants.ON;
	}

}
