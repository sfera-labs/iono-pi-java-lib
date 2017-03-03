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
import java.util.Scanner;

import cc.sferalabs.libs.iono_pi.IonoPi.DigitalInput;
import cc.sferalabs.libs.iono_pi.IonoPi.Wiegand;
import cc.sferalabs.libs.iono_pi.onewire.OneWireBusDevice;
import cc.sferalabs.libs.iono_pi.wiegand.WiegandListener;

/**
 *
 * @author Giampiero Baggiani
 *
 */
public class Main {

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try (Scanner in = new Scanner(System.in)) {
			IonoPi.init();

			System.out.println("\n== Iono Pi Utility ==\n\nCommands:"
					+ "\n   led on           Turn on the green LED"
					+ "\n   led off          Turn off the green LED"
					+ "\n   o <n> open       Open relay output o<n> (<n>=1..4)"
					+ "\n   o <n> close      Close relay output o<n> (<n>=1..4)"
					+ "\n   oc <n> open      Open open collector oc<n> (<n>=1..3)"
					+ "\n   oc <n> close     Close open collector oc<n> (<n>=1..3)"
					+ "\n   di <n>           Print the state (\"high\" or \"low\") of digital input di<n> (<n>=1..6)"
					+ "\n   di <n> <t>       Print the state of digital input di<n> now and on every change"
					+ "\n                    for <t> seconds"
					+ "\n   ai <n>           Print the voltage value (V) read from analog input ai<n> (<n>=1..4)"
					+ "\n   1wire bus        Print IDs and temperature values (\u00b0C) read from all the 1-Wire"
					+ "\n                    devices on the bus"
					+ "\n   1wire ttl <n>    Print temperature (\u00b0C) and humidity (%) values read from the"
					+ "\n                    MaxDetect 1-Wire sensor on TTL<n> (<n>=1..4)"
					+ "\n   wiegand <n> <t>  Print number of bits and value read from Wiegand interface <n> (<n>=1|2)"
					+ "\n                    whenever data is available for <t> seconds"
					+ "\n   quit             You can guess it...\n");

			String line;
			String[] parts;
			int n;
			int t;
			boolean run = true;
			while (run) {
				System.out.print("\n> ");
				line = in.nextLine().trim().toLowerCase();
				parts = line.split("\\s+");
				try {
					switch (parts[0]) {
					case "":
						break;

					case "led":
						boolean on = parts[1].equals("on") ? true : false;
						IonoPi.LED.set(on);
						break;

					case "o":
						boolean rel_closed = parts[2].startsWith("c") ? true : false;
						n = Integer.parseInt(parts[1]);
						switch (n) {
						case 1:
							IonoPi.Output.O1.set(rel_closed);
							break;

						case 2:
							IonoPi.Output.O2.set(rel_closed);
							break;

						case 3:
							IonoPi.Output.O3.set(rel_closed);
							break;

						case 4:
							IonoPi.Output.O4.set(rel_closed);
							break;

						default:
							throw new Exception("<n>=1..4");
						}
						break;

					case "oc":
						boolean oc_closed = parts[2].startsWith("c") ? true : false;
						n = Integer.parseInt(parts[1]);
						switch (n) {
						case 1:
							IonoPi.Output.OC1.set(oc_closed);
							break;

						case 2:
							IonoPi.Output.OC2.set(oc_closed);
							break;

						case 3:
							IonoPi.Output.OC3.set(oc_closed);
							break;

						default:
							throw new Exception("<n>=1..3");
						}
						break;

					case "di":
						n = Integer.parseInt(parts[1]);
						if (parts.length > 2) {
							t = Integer.parseInt(parts[2]);
						} else {
							t = -1;
						}
						switch (n) {
						case 1:
							if (t <= 0) {
								System.out
										.println(IonoPi.DigitalInput.DI1.isHigh() ? "high" : "low");
							} else {
								monitorDigitalInput(IonoPi.DigitalInput.DI1, t);
							}
							break;

						case 2:
							if (t <= 0) {
								System.out
										.println(IonoPi.DigitalInput.DI2.isHigh() ? "high" : "low");
							} else {
								monitorDigitalInput(IonoPi.DigitalInput.DI2, t);
							}
							break;

						case 3:
							if (t <= 0) {
								System.out
										.println(IonoPi.DigitalInput.DI3.isHigh() ? "high" : "low");
							} else {
								monitorDigitalInput(IonoPi.DigitalInput.DI3, t);
							}
							break;

						case 4:
							if (t <= 0) {
								System.out
										.println(IonoPi.DigitalInput.DI4.isHigh() ? "high" : "low");
							} else {
								monitorDigitalInput(IonoPi.DigitalInput.DI4, t);
							}
							break;

						case 5:
							if (t <= 0) {
								System.out
										.println(IonoPi.DigitalInput.DI5.isHigh() ? "high" : "low");
							} else {
								monitorDigitalInput(IonoPi.DigitalInput.DI5, t);
							}
							break;

						case 6:
							if (t <= 0) {
								System.out
										.println(IonoPi.DigitalInput.DI6.isHigh() ? "high" : "low");
							} else {
								monitorDigitalInput(IonoPi.DigitalInput.DI6, t);
							}
							break;

						default:
							throw new Exception("<n>=1..6");
						}
						break;

					case "ai":
						n = Integer.parseInt(parts[1]);
						switch (n) {
						case 1:
							System.out.println(IonoPi.AnalogInput.AI1.readVoltage());
							break;

						case 2:
							System.out.println(IonoPi.AnalogInput.AI2.readVoltage());
							break;

						case 3:
							System.out.println(IonoPi.AnalogInput.AI3.readVoltage());
							break;

						case 4:
							System.out.println(IonoPi.AnalogInput.AI4.readVoltage());
							break;

						default:
							throw new Exception("<n>=1..4");
						}
						break;

					case "1wire":
						if (parts[1].equals("bus")) {
							for (OneWireBusDevice d : IonoPi.OneWire.getBusDevices()) {
								System.out.println("ID: " + d.getId() + " - val: "
										+ d.readTemperature(3) / 1000.0);
							}
						} else if (parts[1].equals("ttl")) {
							n = Integer.parseInt(parts[2]);
							if (n < 1 || n > 4) {
								throw new Exception("<n>=1..4");
							}
							IonoPi.DigitalIO ttl = IonoPi.DigitalIO.values()[n - 1];
							int[] t_rh = IonoPi.OneWire.maxDetectRead(ttl, 3);
							System.out.println(t_rh == null ? "N/A"
									: "T: " + t_rh[0] / 10.0 + "\u00b0C - RH: " + t_rh[1] / 10.0
											+ "%");
						} else {
							throw new Exception("max or bus");
						}
						break;

					case "wiegand":
						n = Integer.parseInt(parts[1]);
						t = Integer.parseInt(parts[2]);
						switch (n) {
						case 1:
							monitorWiegand(IonoPi.Wiegand.W1, t);
							break;

						case 2:
							monitorWiegand(IonoPi.Wiegand.W2, t);
							break;

						default:
							throw new Exception("<n>=1|2");
						}
						break;

					case "quit":
						run = false;
						break;

					default:
						throw new Exception("unknown command");
					}
				} catch (Exception e) {
					System.out.println("Command error: " + e.getMessage());
				}
			}
		} finally {
			IonoPi.shutdown();
		}
	}

	/**
	 * 
	 * @param wg
	 * @param t
	 * @throws Exception
	 */
	private static void monitorWiegand(Wiegand wg, int t) throws Exception {
		Thread th = new Thread(new Runnable() {
			public void run() {
				System.out.println("Monitoring...");
				try {
					wg.monitor(new WiegandListener() {

						@Override
						public boolean onData(Wiegand wInterface, int count, long data) {
							System.out.println("Bits: " + count + " - Data: " + data);
							return true;
						}
					});
				} catch (IOException e) {
					System.out.println("Error: " + e.getMessage());
				}
				System.out.println("Done!");
			}
		});

		th.start();
		try {
			Thread.sleep(t * 1000);
		} catch (InterruptedException e) {
		}
		wg.stop();
		th.join();
	}

	/**
	 * 
	 * @param di
	 * @param t
	 */
	private static void monitorDigitalInput(DigitalInput di, int t) {
		di.setListener(new DigitalInputListener() {

			@Override
			public void onChange(DigitalInput input, boolean high) {
				System.out.println(high ? "high" : "low");
			}
		});
		System.out.println("Monitoring...");
		try {
			Thread.sleep(t * 1000);
		} catch (InterruptedException e) {
		}
		System.out.println("Done!");
		di.setListener(null);
	}

}
