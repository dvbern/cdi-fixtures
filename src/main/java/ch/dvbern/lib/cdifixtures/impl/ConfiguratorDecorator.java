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

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import ch.dvbern.lib.cdifixtures.Configurator;
import ch.dvbern.lib.cdifixtures.events.FixtureBeanConfigured;

/**
 * Decorator which fires a {@link FixtureBeanConfigured} event.
 */
@SuppressWarnings({ "CdiManagedBeanInconsistencyInspection" })
@Decorator
public abstract class ConfiguratorDecorator<T> implements Configurator<T> {

	@Inject
	@Delegate
	@Any
	private Configurator<T> configurator;

	@Inject
	private Event<FixtureBeanConfigured> configuredEvent;

	@Override
	public void configure(final T bean) {
		configurator.configure(bean);
		configuredEvent.fire(new FixtureBeanConfigured(bean, false));
	}
}
