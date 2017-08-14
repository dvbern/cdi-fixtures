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

import javax.inject.Inject;

import ch.dvbern.lib.cdifixtures.configs.AnotherPersonConfigurator;
import ch.dvbern.lib.cdifixtures.testbeans.Address;
import ch.dvbern.lib.cdifixtures.testbeans.Person;
import ch.dvbern.lib.cditest.runner.CDIRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

@RunWith(CDIRunner.class)
public class FixtureTest {

	@Inject
	private FixtureObject<Person> person;

	@Inject
	@Name("other")
	private FixtureObject<Person> person2;

	@Inject
	@Name("Isidor")
	private FixtureObject<Person> person3;

	@Inject
	@ConfiguratorClass(AnotherPersonConfigurator.class)
	private FixtureObject<Person> person4;

	@Inject
	private FixtureObject<Address> addressFixtureObject;

	@Test
	public void testFixture() {
		assertNotNull(person.get());
		assertEquals(34, person.get().getAge());
	}

	@Test
	public void testPerson2() {
		assertNotNull(person2);
		assertEquals(67, person2.get().getAge());
	}

	@Test
	public void testInjectionByConfiguratorClass() {
		assertNotNull(person4);
		assertNotSame(person2.get(), person4.get());
		assertEquals(person2.get(), person4.get());
	}

	@Test
	public void testCreateNew() {
		final Person person1 = person.get();
		final Person person2 = person.get();
		assertSame(person1, person2);
		final Person personNew = person.get(true);
		assertNotSame(person1, personNew);
		assertEquals(person1, personNew);
		final Person personNew1 = person.get();
		assertSame(personNew, personNew1);
	}
}
