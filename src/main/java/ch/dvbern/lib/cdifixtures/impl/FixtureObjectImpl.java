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

package ch.dvbern.lib.cdifixtures.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.event.Event;

import ch.dvbern.lib.cdifixtures.ConfigurationException;
import ch.dvbern.lib.cdifixtures.Configurator;
import ch.dvbern.lib.cdifixtures.FixtureObject;
import ch.dvbern.lib.cdifixtures.events.FixtureBeanConfigured;
import ch.dvbern.lib.cdifixtures.events.PersistentConfigurationFinished;
import ch.dvbern.lib.cdifixtures.events.PersistentConfigurationStarted;

/**
 * Implementation des {@link FixtureObject}
 */
class FixtureObjectImpl<T> implements FixtureObject<T> {

	private T t;

	private final Configurator<T>[] configurators;

	private boolean configured = false;

	private final Event<PersistentConfigurationStarted> startedEvent;
	private final Event<PersistentConfigurationFinished> finishedEvent;
	private final Event<FixtureBeanConfigured> fixtureBeanConfiguredEvent;
	private final boolean nonPersistent;
	private final BeanInstantiator<T> beanInstantiator;

	FixtureObjectImpl(@Nonnull final BeanInstantiator<T> beanInstantiator,
		final boolean nonPersistent,
		@Nonnull final Event<FixtureBeanConfigured> fixtureBeanConfiguredEvent,
		@Nullable final Event<PersistentConfigurationStarted> startedEvent,
		@Nullable final Event<PersistentConfigurationFinished> finishedEvent,
		@Nonnull final Configurator<T>... configurators) {
		this.configurators = configurators;
		this.beanInstantiator = beanInstantiator;
		this.nonPersistent = nonPersistent;
		this.fixtureBeanConfiguredEvent = fixtureBeanConfiguredEvent;
		this.startedEvent = startedEvent;
		this.finishedEvent = finishedEvent;
		if (!((startedEvent == null && finishedEvent == null) || startedEvent != null && finishedEvent != null)) {
			throw new IllegalStateException("Parameters startedEvent and finishedEvent must be both null or both" +
				" non-null");
		}
	}

	private void assertConfigured(final Configurator<T> adHocConfigurator) {
		if (!configured) {
			t = beanInstantiator.create();
			if (startedEvent != null) {
				startedEvent.fire(new PersistentConfigurationStarted(t));
			}
			try {
				for (final Configurator<T> configurator : configurators) {
					configurator.configure(t);
				}
				if (adHocConfigurator != null) {
					adHocConfigurator.configure(t);
				}
				fixtureBeanConfiguredEvent.fire(new FixtureBeanConfigured(t, nonPersistent));
			} catch (RuntimeException x) {
				throw new ConfigurationException(x);
			} finally {
				if (finishedEvent != null) {
					finishedEvent.fire(new PersistentConfigurationFinished(t));
				}
				configured = true;
			}
		}
	}

	@Override
	public T get() {
		return get(false);
	}

	@Override
	public T get(final boolean createNew) {
		return get(createNew, null);
	}

	@Override
	public T get(final Configurator<T> tConfigurator) {
		return get(false, tConfigurator);
	}

	@Override
	public T get(final boolean createNew, @Nullable final Configurator<T> tConfigurator) {
		if (createNew) {
			configured = false;
		}
		assertConfigured(tConfigurator);
		return t;
	}
}
