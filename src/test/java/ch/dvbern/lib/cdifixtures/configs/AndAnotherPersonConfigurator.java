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

package ch.dvbern.lib.cdifixtures.configs;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import ch.dvbern.lib.cdifixtures.FixtureObject;
import ch.dvbern.lib.cdifixtures.Name;
import ch.dvbern.lib.cdifixtures.testbeans.Address;
import ch.dvbern.lib.cdifixtures.testbeans.Person;

/**
 * Named configuration for a Person
 */
@Name("Isidor")
public class AndAnotherPersonConfigurator extends AnotherPersonConfigurator {

	@Inject
	private Instance<FixtureObject<Address>> addressFixtureObject;

	@Override
	public void configure(final Person bean) {
		super.configure(bean);
		bean.setAddress(addressFixtureObject.get().get());
		bean.setFirstName("Isidor");
	}
}
