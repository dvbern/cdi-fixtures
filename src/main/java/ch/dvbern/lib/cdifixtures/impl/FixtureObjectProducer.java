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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.event.Event;
import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

import ch.dvbern.lib.cdifixtures.Configurator;
import ch.dvbern.lib.cdifixtures.ConfiguratorClass;
import ch.dvbern.lib.cdifixtures.FixtureObject;
import ch.dvbern.lib.cdifixtures.Name;
import ch.dvbern.lib.cdifixtures.NonPersisted;
import ch.dvbern.lib.cdifixtures.Persisted;
import ch.dvbern.lib.cdifixtures.events.FixtureBeanConfigured;
import ch.dvbern.lib.cdifixtures.events.PersistentConfigurationFinished;
import ch.dvbern.lib.cdifixtures.events.PersistentConfigurationStarted;

/**
 * Producer for Fixture instances.
 */
@Singleton
public class FixtureObjectProducer {

	@SuppressWarnings("CdiInjectionPointsInspection")
	@Inject
	private Instance<Configurator<?>> configurator;

	@Inject
	private Event<PersistentConfigurationStarted> startedEvent;

	@Inject
	private Event<PersistentConfigurationFinished> finishedEvent;

	@Inject
	private Event<FixtureBeanConfigured> fixtureBeanConfiguredEvent;

	@Produces
	public <T> FixtureObject<T> produceConfigurator(final InjectionPoint injectionPoint) throws IllegalAccessException, InstantiationException {
		return produce(injectionPoint, false);
	}

	@Produces
	@Persisted
	public <T> FixtureObject<T> producePersistingConfigurator(final InjectionPoint injectionPoint) throws IllegalAccessException,
		InstantiationException {
		return produce(injectionPoint, true);
	}

	@SuppressWarnings("unchecked")
	private <T> FixtureObject<T> produce(final InjectionPoint injectionPoint, final boolean persistent) {
		final Set<ConfiguratorInfo<?>> configuratorsWithMatchingType = new HashSet<ConfiguratorInfo<?>>();
		final Type typeNeeded = getFirstGenericType(injectionPoint.getType());
		if (typeNeeded != null) {
			for (final Configurator<?> cfgr : configurator) {
				final Type genericSuperclass = getFirstGenericType(getConfiguratorInterface(cfgr.getClass()));
				if (genericSuperclass != null && genericSuperclass.equals(typeNeeded)) {
					configuratorsWithMatchingType.add(new ConfiguratorInfo(cfgr));
				}
			}
		} else {
			throw new UnsatisfiedResolutionException("FixtureObject " + injectionPoint.getMember() + " must have the bean to be produced as "
				+ "generic type");
		}
		final Configurator<?> configurators = filter(configuratorsWithMatchingType, typeNeeded,
			injectionPoint.getAnnotated().getAnnotation(Name.class),
			injectionPoint.getAnnotated().getAnnotation(ConfiguratorClass.class), injectionPoint);
		final boolean nonPersistent = injectionPoint.getAnnotated().getAnnotation(NonPersisted.class) != null;
		FixtureObject<T> returnValue = null;
		if (configurators != null) {
			if (persistent) {
				returnValue = new FixtureObjectImpl(new DefaultConstructorBeanInstantiator((Class<T>) typeNeeded), nonPersistent,
					fixtureBeanConfiguredEvent,
					startedEvent, finishedEvent,
					configurators);
			} else {
				returnValue = new FixtureObjectImpl(new DefaultConstructorBeanInstantiator((Class<T>) typeNeeded), nonPersistent,
					fixtureBeanConfiguredEvent,
					null, null,
					configurators);
			}
		}
		return returnValue;
	}

	private Configurator<?> filter(final Set<ConfiguratorInfo<?>> configuratorsWithMatchingType, final Type typeNeeded, final Name annotation,
		final
	ConfiguratorClass configuratorClass, final InjectionPoint injectionPoint) {
		final List<Configurator<?>> retValList = new ArrayList<Configurator<?>>();

		for (final ConfiguratorInfo<?> configuratorInfo : configuratorsWithMatchingType) {
			if ((configuratorClass != null && configuratorInfo.matchesClass(configuratorClass))
				||
				configuratorClass == null && configuratorInfo.matchesName(annotation)) {
				retValList.add(configuratorInfo.getConfigurator());
			}
		}
		if (retValList.size() > 1) {
			throw new AmbiguousResolutionException("Ambiguous configurations: " + retValList + " to be injected into " + injectionPoint
				.getMember());
		} else if (retValList.isEmpty()) {
			throw new UnsatisfiedResolutionException("Could not find an instance of " + Configurator.class + " with generic type " + typeNeeded
				+ " to be injected into " + injectionPoint.getMember());
		}
		return retValList.get(0);
	}

	private Type getFirstGenericType(final Type type) {
		Type retVal = null;
		if (type instanceof ParameterizedType) {
			final ParameterizedType paramType = (ParameterizedType) type;
			if (paramType.getActualTypeArguments().length == 1) {
				retVal = paramType.getActualTypeArguments()[0];
			}
		}
		return retVal;
	}

	private Type getConfiguratorInterface(final Class<?> aClass) {
		Type retVal = null;
		for (final Type type : aClass.getGenericInterfaces()) {
			if (type instanceof ParameterizedType) {
				final ParameterizedType parameterizedType = (ParameterizedType) type;
				if (parameterizedType.getRawType().equals(Configurator.class)) {
					retVal = type;
				}
			}
		}
		if (retVal == null && !aClass.equals(Object.class)) {
			retVal = getConfiguratorInterface(aClass.getSuperclass());
		}
		return retVal;
	}

}
