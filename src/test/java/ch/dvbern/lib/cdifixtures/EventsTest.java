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

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ch.dvbern.lib.cdifixtures.events.FixtureBeanConfigured;
import ch.dvbern.lib.cdifixtures.events.PersistentConfigurationStarted;
import ch.dvbern.lib.cdifixtures.testbeans.Address;
import ch.dvbern.lib.cditest.event.TestFinished;
import ch.dvbern.lib.cditest.runner.CDIRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Testclass for events
 */
@RunWith(CDIRunner.class)
@Singleton
public class EventsTest {

	@SuppressWarnings("CdiInjectionPointsInspection")
	@Inject
	private FixtureObject<Address> addressFixtureObject;

	@SuppressWarnings("CdiInjectionPointsInspection")
	@Inject
	@Persisted
	private FixtureObject<Address> persistedAdreFixtureObject;

	private Address address;

	private boolean isPersistentConfigurationRunning = false;

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	public void onFixtureBeanConfigured(@Observes final FixtureBeanConfigured beanConfigured) {
		if (beanConfigured.getFixtureBean() instanceof Address) {
			this.address = (Address) beanConfigured.getFixtureBean();
		}
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	public void onPersistentConfigurationStarted(
		@Observes final PersistentConfigurationStarted persistentConfigurationStarted) {
		isPersistentConfigurationRunning = true;
	}

	public void onTestFinished(@Observes final TestFinished testFinished) {
		this.address = null;
		isPersistentConfigurationRunning = false;
	}

	@Test
	public void testFixtureBeanConfiguredEvent() {
		assertNull(address);
		addressFixtureObject.get();
		assertNotNull(address);
		assertSame(address, addressFixtureObject.get());
	}

	@Test
	public void testPersistentConfiguration() {
		assertFalse(isPersistentConfigurationRunning);
		persistedAdreFixtureObject.get();
		assertTrue(isPersistentConfigurationRunning);
	}
}
