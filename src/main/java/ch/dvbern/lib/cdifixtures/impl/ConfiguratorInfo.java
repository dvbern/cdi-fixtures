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

import ch.dvbern.lib.cdifixtures.Configurator;
import ch.dvbern.lib.cdifixtures.ConfiguratorClass;
import ch.dvbern.lib.cdifixtures.Name;


class ConfiguratorInfo<T> {

	private final Configurator<T> configurator;

	public ConfiguratorInfo(final Configurator<T> cfgr) {
		this.configurator = cfgr;
	}

	public Configurator<T> getConfigurator() {
		return configurator;
	}

	public boolean matchesName(final Name annotation) {
		Name configName = configurator.getClass().getAnnotation(Name.class);
		if (configName == null) {
			configName = configurator.getClass().getSuperclass().getAnnotation(Name.class);
		}
		return ((configName == null && annotation == null)
			||
			(configName != null && annotation != null
				&& configName.value().equals(annotation.value()))
		);
	}

	public boolean matchesClass(@Nonnull final ConfiguratorClass configuratorClass) {
		return configuratorClass.value().equals(configurator.getClass());
	}
}
