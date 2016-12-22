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

import java.io.IOException;
import java.util.Objects;

import cc.sferalabs.libs.iono_pi.jni.IonoPiJNI;
import cc.sferalabs.libs.iono_pi.jni.IonoPiJNIConstants;
import cc.sferalabs.libs.iono_pi.jni.WiegandListenerJNIWrapper;
import cc.sferalabs.libs.iono_pi.onewire.OneWire;
import cc.sferalabs.libs.iono_pi.wiegand.WiegandListener;

/**
 *
 * @author Giampiero Baggiani
 *
 */
public abstract class IonoPi {

	public static final Led LED = new Led();

	public static final OneWire OneWire = new OneWire();

	/**
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		IonoPiJNI.init();
	}

	/**
	 * 
	 */
	public static void shutdown() {
		Wiegand.W1.stop();
		Wiegand.W2.stop();
		for (DigitalInput di : DigitalInput.values()) {
			di.setListener(null);
		}
	}

	/**
	 *
	 */
	public enum Output {
		O1(IonoPiJNIConstants.O1), O2(IonoPiJNIConstants.O2), O3(IonoPiJNIConstants.O3), O4(
				IonoPiJNIConstants.O4), OC1(IonoPiJNIConstants.OC1), OC2(
						IonoPiJNIConstants.OC2), OC3(IonoPiJNIConstants.OC3);

		private final int pin;

		private Output(int pin) {
			this.pin = pin;
		}

		/**
		 * @param closed
		 */
		public void set(boolean closed) {
			IonoPiJNI.ionoPiDigitalWrite(pin,
					closed ? IonoPiJNIConstants.CLOSED : IonoPiJNIConstants.OPEN);
		}

		/**
		 * 
		 */
		public boolean isClosed() {
			IonoPiJNI.ionoPiPinMode(pin, IonoPiJNIConstants.INPUT);
			boolean state = IonoPiJNI.ionoPiDigitalRead(pin) == IonoPiJNIConstants.CLOSED;
			IonoPiJNI.ionoPiPinMode(pin, IonoPiJNIConstants.OUTPUT);
			return state;
		}
	}

	/**
	 *
	 */
	public enum DigitalInput {
		DI1(IonoPiJNIConstants.DI1), DI2(IonoPiJNIConstants.DI2), DI3(IonoPiJNIConstants.DI3), DI4(
				IonoPiJNIConstants.DI4), DI5(IonoPiJNIConstants.DI5), DI6(IonoPiJNIConstants.DI6);

		public final int pin;

		private DigitalInput(int pin) {
			this.pin = pin;
		}

		/**
		 * 
		 * @return
		 */
		public boolean isHigh() {
			return IonoPiJNI.ionoPiDigitalRead(pin) == IonoPiJNIConstants.HIGH;
		}

		/**
		 * @param listener
		 */
		public void setListener(DigitalInputListener listener) {
			IonoPiJNI.ionoPiSetDigitalInputListener(this, IonoPiJNIConstants.INT_EDGE_BOTH,
					listener);
		}
	}

	/**
	 *
	 */
	public enum DigitalIO {
		TTL1(IonoPiJNIConstants.TTL1), TTL2(IonoPiJNIConstants.TTL2), TTL3(
				IonoPiJNIConstants.TTL3), TTL4(IonoPiJNIConstants.TTL4);

		public final int pin;

		private DigitalIO(int pin) {
			this.pin = pin;
		}

		/**
		 * @return
		 */
		public boolean isHigh() {
			IonoPiJNI.ionoPiPinMode(pin, IonoPiJNIConstants.INPUT);
			return IonoPiJNI.ionoPiDigitalRead(pin) == IonoPiJNIConstants.HIGH;
		}

		/**
		 * @param high
		 */
		public void set(boolean high) {
			IonoPiJNI.ionoPiPinMode(pin, IonoPiJNIConstants.OUTPUT);
			IonoPiJNI.ionoPiDigitalWrite(pin,
					high ? IonoPiJNIConstants.HIGH : IonoPiJNIConstants.LOW);
		}
	}

	/**
	 *
	 */
	public enum AnalogInput {
		AI1(IonoPiJNIConstants.AI1), AI2(IonoPiJNIConstants.AI2), AI3(IonoPiJNIConstants.AI3), AI4(
				IonoPiJNIConstants.AI4);

		public final int pin;

		private AnalogInput(int pin) {
			this.pin = pin;
		}

		/**
		 * 
		 * @return
		 * @throws IOException
		 */
		public float read() throws IOException {
			float val = IonoPiJNI.ionoPiAnalogRead(pin);
			if (val < 0) {
				throw new IOException("read error");
			}
			return val;
		}
	}

	/**
	 *
	 */
	public enum Wiegand {
		W1(1), W2(2);

		private final int wInterface;

		/**
		 * 
		 */
		private Wiegand(int wInterface) {
			this.wInterface = wInterface;
		}

		/**
		 * 
		 * @param listener
		 * @throws IOException
		 * @throws NullPointerException
		 *             if {@code listener} is {@code null}
		 */
		public void monitor(WiegandListener listener) throws IOException, NullPointerException {
			Objects.requireNonNull(listener, "listener must not be null");
			if (!IonoPiJNI.ionoPiWiegandMonitor(wInterface,
					new WiegandListenerJNIWrapper(listener))) {
				throw new IOException("Wiegand error");
			}
		}

		/**
		 * 
		 */
		public void stop() {
			IonoPiJNI.ionoPiWiegandStop(wInterface);
		}

	}

}
