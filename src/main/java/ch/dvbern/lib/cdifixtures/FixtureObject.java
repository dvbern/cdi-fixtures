/*
 * Copyright 2017 DV Bern AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */

package ch.dvbern.lib.cdifixtures;

/**
 * Ermöglicht den Zugriff auf das eigentliche Bean.
 */
public interface FixtureObject<T> {

	/**
	 * Erzeugt ein Bean vom Typ T, konfiguriert es entsprechend und gibt dieses zurück.
	 * <p>
	 * Wenn diese Methode mehrmals aufgerufen wird wird stets dieselbe Instanz zurückgegeben, so dass
	 * {@code fixtureObject.get() == fixtureObject.get() } gilt.
	 * <p>
	 * Convenience-Methode für {@link #get(boolean)} mit {@code false} als Parameter
	 *
	 * @return eine Instanz von T, konfiguriert.
	 * @see #get(boolean)
	 */
	T get();

	/**
	 * Erzeugt ein Bean vom Typ T, konfiguriert es entsprechend und gibt dieses zurück. Mit dem Parameter
	 * <tt>createNew</tt>
	 * wird definiert ob das Bean neu erstellt und konfiguriert werden soll:
	 *
	 * <pre>
	 *     {@code
	 * fixtureObject.get(false) == fixtureObject.get(false)
	 * fixtureObject.get(false) != fixtureObject.get(true)
	 * fixtureObject.get(true) != fixtureObject.get(true)
	 * }
	 * </pre>
	 * <p>
	 * Convenience-Methode für {@link #get(boolean, Configurator)} mit {@code null} als 2. Parameter
	 *
	 * @param createNew wenn {@code true} wird eine neue Instanz von T erzeugt und konfiguriert. Bei {@code false}
	 * wird eine allenfalls bereits erzeuge Instanz ohne neue Konfiguration zurückgegeben.
	 * @return eine Instanz von T, konfiguriert.
	 */
	T get(final boolean createNew);

	/**
	 * Erzeugt ein Bean vom Typ T, konfiguriert es zuerst mit dem definierten {@link Configurator} und danach mit dem
	 * gegebenen {@link Configurator}und gibt dieses zurück.
	 * <p>
	 * Mit dem Parameter <tt>createNew</tt>
	 * wird definiert ob das Bean neu erstellt und konfiguriert werden soll:
	 *
	 * <pre>
	 *     {@code
	 * fixtureObject.get(false) == fixtureObject.get(false)
	 * fixtureObject.get(false) != fixtureObject.get(true)
	 * fixtureObject.get(true) != fixtureObject.get(true)
	 * }
	 * </pre>
	 * <p>
	 * Convenience-Methode für {@link #get(boolean, Configurator)} mit {@code false} als 1. Parameter
	 *
	 * @param configurator den anzuwendenden {@link Configurator}. Darf auch {@code null} sein.
	 * @return eine Instanz von T, konfiguriert.
	 */
	T get(final Configurator<T> configurator);

	/**
	 * Erzeugt ein Bean vom Typ T, konfiguriert es zuerst mit dem definierten {@link Configurator}
	 * (Parameter <tt>configurator</tt>) und danach mit dem
	 * gegebenen {@link Configurator}und gibt dieses zurück.
	 * <p>
	 * Mit dem Parameter <tt>createNew</tt>
	 * wird definiert ob das Bean neu erstellt und konfiguriert werden soll:
	 *
	 * <pre>
	 *     {@code
	 * 			fixtureObject.get(false) == fixtureObject.get(false)
	 * 			fixtureObject.get(false) != fixtureObject.get(true)
	 *          fixtureObject.get(true) != fixtureObject.get(true)
	 * }
	 * </pre>
	 * <p>
	 *
	 * @param createNew creates and configures new bean when {@code TRUE}
	 * @param configurator den anzuwendenden {@link Configurator}
	 * @return eine Instanz von T, konfiguriert.
	 */
	T get(final boolean createNew, final Configurator<T> configurator);

}
