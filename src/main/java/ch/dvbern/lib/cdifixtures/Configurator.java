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
 * Configures a given Object of type T by setting stuff in the setter.
 */
public interface Configurator<T> {

	/**
	 * Configures an arbitrary Object of the given bean.
	 *
	 * @param bean the bean parameter - never {@code null}.
	 */
	void configure(T bean);

}
